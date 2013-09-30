package impl.jing.lite.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import ananas.jing.lite.core.Const;
import ananas.jing.lite.core.JingRepo;
import ananas.jing.lite.core.LocalXGitObject;
import ananas.jing.lite.core.RemoteXGitObject;
import ananas.jing.lite.core.client.JingClient;

public class MessageRT implements Runnable {

	private final JingRepo _repo;
	private final JingClient _client;

	public MessageRT(JingClient client) {
		this._client = client;
		this._repo = client.getRepo();
	}

	@Override
	public void run() {

		File dir = _repo.getFile(JingRepo.dir_sms_buffer);
		if (!dir.exists())
			return;

		if (!dir.isDirectory())
			return;

		File[] list = dir.listFiles();
		if (list == null)
			return;

		for (File file : list) {

			if (file.length() > 1024000)
				continue;

			InputStream in = null;
			try {

				in = new FileInputStream(file);
				Properties prop = new Properties();
				prop.load(in);
				in.close();
				in = null;

				String direction = prop.getProperty(Const.Jing.direction);

				if (direction == null) {
				} else if (direction.equals(Const.Jing.direction_rx)) {
					this.__do_rx(file, prop);
				} else if (direction.equals(Const.Jing.direction_tx)) {
					this.__do_tx(file, prop);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (in != null)
					in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private void __do_rx(File tmpFile, Properties prop) {
		// TODO Auto-generated method stub

	}

	class SaveResult {

		private final LocalXGitObject _go;

		public SaveResult(File raw_file, LocalXGitObject go) {
			_go = go;
		}

		public LocalXGitObject getXGitObject() {
			return this._go;
		}
	}

	private void __do_tx(File tmpFile, final Properties prop0)
			throws IOException {

		// gen the message git-object

		Properties prop1 = new Properties();
		prop1.putAll(prop0);
		prop1.remove(Const.Jing.addr_to);
		prop1.remove(Const.Jing.addr_from);
		SaveResult sr1 = this.__save_to_repo(prop1);

		Properties prop2 = new Properties();
		prop2.putAll(prop1);
		prop2.remove(Const.Jing.text_detail);
		prop2.remove(Const.Jing.text_overview);
		prop2.setProperty(Const.Jing.message_sha1, sr1.getXGitObject()
				.getSha1());
		SaveResult sr2 = this.__save_to_repo(prop2);
		sr2.getXGitObject();

		File dir_sms = this._repo.getFile(JingRepo.dir_sms);
		File file_sms = new File(dir_sms, sr2.getXGitObject().getSha1());
		file_sms.createNewFile();

		// push to server
		RemoteXGitObject go2 = this._client.push(sr1.getXGitObject());
		String url = go2.getLongURL();
		String msg = "jing: " + url;
		String to = prop0.getProperty(Const.Jing.addr_to);
		this.__send_with_sms(to, msg);

		// remove the temp file
		tmpFile.delete();

	}

	private SaveResult __save_to_repo(Properties prop) throws IOException {

		File file = this._client.getRepo().getApiL().newTempFile();
		File dir = file.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(file);
		prop.store(out, "jing");
		out.close();

		LocalXGitObject go = this._client.getRepo().getApiL()
				.addRawObject("blob", file);
		File sms_obj_dir = this._repo.getFile(JingRepo.dir_sms_objects);
		File sms_obj_file = new File(sms_obj_dir, go.getSha1());
		if (!sms_obj_dir.exists()) {
			sms_obj_dir.mkdirs();
		}
		file.renameTo(sms_obj_file);

		return new SaveResult(sms_obj_file, go);
	}

	private void __send_with_sms(String to, String msg) {
		// TODO Auto-generated method stub
		System.out.println(to + " << " + msg);
		// throw new RuntimeException("no impl");
	}

}
