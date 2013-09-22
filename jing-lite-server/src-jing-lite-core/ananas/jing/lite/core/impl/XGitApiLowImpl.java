package ananas.jing.lite.core.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

import ananas.jing.lite.core.JingRepo;
import ananas.jing.lite.core.XGitObject;
import ananas.jing.lite.core.util.DoubleOutputStream;
import ananas.jing.lite.core.util.Sha1OutputStream;
import ananas.jing.lite.core.util.StreamPump;
import ananas.jing.lite.core.xgit.XGitApiL;
import ananas.jing.lite.core.xgit.XGitRepo;

public class XGitApiLowImpl implements XGitApiL {

	private final XGitRepo _repo;
	private int _tmp_file_index;

	public XGitApiLowImpl(XGitRepo repo) {
		this._repo = repo;
	}

	@Override
	public boolean addZippedObject(XGitObject go, InputStream in) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getZippedObject(XGitObject go, OutputStream out) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public XGitObject addRawObject(String type, File file) {

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
			XGitObject go = repo.getXGitObject(sha1);
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
}
