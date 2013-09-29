package impl.jing.lite.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import ananas.jing.lite.core.JingRepo;
import ananas.jing.lite.core.LocalXGitObject;
import ananas.jing.lite.core.util.DoubleOutputStream;
import ananas.jing.lite.core.util.Sha1OutputStream;
import ananas.jing.lite.core.util.StreamPump;
import ananas.jing.lite.core.xgit.XGitApiL;
import ananas.jing.lite.core.xgit.XGitCheckout;
import ananas.jing.lite.core.xgit.XGitRepo;

public class XGitApiLowImpl implements XGitApiL {

	private final XGitRepo _repo;
	private int _tmp_file_index;

	public XGitApiLowImpl(XGitRepo repo) {
		this._repo = repo;
	}

	@Override
	public boolean addZippedObject(LocalXGitObject go, InputStream in) {
		try {
			File file = go.getFile();
			if (file.exists()) {
				return true;
			}
			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			OutputStream out = new FileOutputStream(file);
			(new StreamPump(in, out)).run();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean getZippedObject(LocalXGitObject go, OutputStream out) {
		try {
			File file = go.getFile();
			InputStream in = new FileInputStream(file);
			(new StreamPump(in, out)).run();
			in.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public LocalXGitObject addRawObject(String type, File file) {

		InputStream in = null;
		OutputStream out = null;
		File tmpOutFile = null;
		String sha1 = null;

		XGitRepo repo = this._repo;

		try {

			tmpOutFile = repo.getApiL().newTempFile();
			File parent = tmpOutFile.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}

			out = new FileOutputStream(tmpOutFile);
			in = new FileInputStream(file);

			OutputStream zip_out = new DeflaterOutputStream(out);
			Sha1OutputStream sha1_out = new Sha1OutputStream();

			byte[] head = (type + " " + file.length() + '\0').getBytes();
			OutputStream out2 = new DoubleOutputStream(sha1_out, zip_out);
			out2.write(head);
			(new StreamPump(in, out2)).run();

			out2.flush();
			out2.close();
			sha1 = sha1_out.getSha1();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (in != null)
				in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (out != null)
				out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (sha1 == null) {
			tmpOutFile.delete();
			return null;
		} else {
			LocalXGitObject go = repo.getXGitObject(sha1);
			if (go.exists()) {
				tmpOutFile.delete();
			} else {
				File dest = go.getFile();
				File parent = dest.getParentFile();
				if (!parent.exists()) {
					parent.mkdirs();
				}
				tmpOutFile.renameTo(dest);
			}
			return go;
		}

	}

	@Override
	public File newTempFile() {
		File temp_dir = this._repo.getFile(JingRepo.dir_temp);
		long now = System.currentTimeMillis();
		int index = (this._tmp_file_index++);
		String name = "file_" + this.hashCode() + "-" + index + "_" + now
				+ ".tmp";
		return new File(temp_dir, name);
	}

	@Override
	public XGitCheckout checkout(LocalXGitObject go) throws IOException {

		File file = go.getFile();
		if (file.exists())
			if (!file.isDirectory()) {

				InputStream in = new FileInputStream(file);
				InputStream in2 = new InflaterInputStream(in);
				StringBuilder sb = new StringBuilder();
				for (int b = in2.read(); b > 0; b = in2.read()) {
					sb.append((char) b);
				}
				String s = sb.toString();
				int index = s.indexOf(' ');
				String type = s.substring(0, index);
				long length = Long.parseLong(s.substring(index + 1));
				return new XGitCheckoutImpl(type, length, in2);

			}
		return null;
	}
}
