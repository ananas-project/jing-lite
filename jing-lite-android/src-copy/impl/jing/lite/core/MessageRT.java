package impl.jing.lite.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import ananas.jing.lite.core.Const;
import ananas.jing.lite.core.JingRepo;
import ananas.jing.lite.core.JingSMSHandler;
import ananas.jing.lite.core.LocalXGitObject;
import ananas.jing.lite.core.RemoteXGitObject;
import ananas.jing.lite.core.client.JingClient;
import ananas.jing.lite.core.util.STRunner;
import ananas.jing.lite.core.util.StreamPump;
import ananas.jing.lite.core.xgit.XGitCheckout;

public class MessageRT implements Runnable {

	private final JingRepo _repo;
	private final JingClient _client;

	public MessageRT(JingClient client) {
		this._client = client;
		this._repo = client.getRepo();
	}

	@Override
	public void run() {

		File dir = _repo.getFile(JingRepo.dir_sms_task);
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

	private void __do_rx(File tmpFile, Properties prop) throws IOException {

		String addr_from = prop.getProperty(Const.Jing.tx_addr);
		String url = prop.getProperty(Const.Jing.message_url);
		LocalXGitObject res = _client.pull(url);

		if (res.exists()) {

			final String sha1 = res.getSha1();
			final File file_detail = this.__get_detail_file(sha1);
			final File file_overview = this.__get_overview_file(sha1);

			File dir = file_detail.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}

			// checkout
			XGitCheckout co = res.getRepo().getApiL().checkout(res);
			InputStream in = co.getInputStream();
			FileOutputStream out = new FileOutputStream(file_detail);
			(new StreamPump(in, out)).run();
			out.close();
			co.close();
			in.close();

			final Properties detail = new Properties();
			this.__load_prop(detail, file_detail);

			// build overview file
			final Properties overview = new Properties();
			{
				overview.putAll(detail);
				overview.remove(Const.Jing.text_detail);

				final long now = System.currentTimeMillis();

				overview.setProperty(Const.Jing.message_sha1, "" + sha1);
				overview.setProperty(Const.Jing.tx_addr, addr_from);
				overview.setProperty(Const.Jing.rx_time, "" + now);
				overview.setProperty(Const.Jing.direction,
						Const.Jing.direction_rx);
			}
			this.__save_prop(overview, file_overview);

			tmpFile.delete();

			JingSMSHandler h = this._client.getSMSHandler();
			if (h != null)
				h.onReceive(_client, overview);

		}

	}

	private void __save_prop(Properties prop, File file) throws IOException {
		OutputStream out = new FileOutputStream(file);
		prop.store(out, file.getName());
		out.close();
	}

	private void __load_prop(Properties prop, File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		prop.load(in);
		in.close();
	}

	private File __get_overview_file(String sha1) {
		File dir = _repo.getFile(JingRepo.dir_sms);
		return new File(dir, sha1 + ".txt");
	}

	private File __get_detail_file(String sha1) {
		File dir = _repo.getFile(JingRepo.dir_sms_remote);
		return new File(dir, sha1 + "");
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

		final Properties detail = new Properties();
		detail.putAll(prop0);
		detail.remove(Const.Jing.direction);
		detail.remove(Const.Jing.tx_addr);
		detail.remove(Const.Jing.rx_addr);
		detail.remove(Const.Jing.rx_time);
		SaveResult sr1 = this.__save_to_repo(detail);
		final String sha1 = sr1.getXGitObject().getSha1();

		final File detail_file = this.__get_detail_file(sha1);
		final File over_file = this.__get_overview_file(sha1);

		// make detail
		_repo.getApiL().checkout(sr1.getXGitObject(), detail_file);

		// make overview
		final Properties overview = new Properties();
		overview.putAll(prop0);
		overview.putAll(detail);
		overview.remove(Const.Jing.text_detail);
		overview.setProperty(Const.Jing.message_sha1, sha1);
		this.__save_prop(overview, over_file);

		// push to server
		RemoteXGitObject go2 = this._client.push(sr1.getXGitObject());
		String url = go2.getLongURL();
		String msg = "jing: " + url;
		String to = prop0.getProperty(Const.Jing.rx_addr);
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
		File sms_obj_dir = this._repo.getFile(JingRepo.dir_sms_remote);
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
		JingSMSHandler h = this._client.getSMSHandler();
		if (h != null)
			h.sendSMS(to, msg);
		else
			System.err.println("warning : no sms handler !");
		// throw new RuntimeException("no impl");
	}

}
