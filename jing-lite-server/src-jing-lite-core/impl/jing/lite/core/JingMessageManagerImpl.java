package impl.jing.lite.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import ananas.jing.lite.core.Const;
import ananas.jing.lite.core.JingRepo;
import ananas.jing.lite.core.client.JingClient;
import ananas.jing.lite.core.client.JingMessageManager;
import ananas.jing.lite.core.client.JingMessageOverview;

public class JingMessageManagerImpl implements JingMessageManager {

	private final JingClient _client;

	public JingMessageManagerImpl(JingClient client) {
		this._client = client;
	}

	@Override
	public void sendMessage(String to, String overview, Properties src) {

		try {

			final Properties dest = new Properties();
			dest.setProperty(Const.Jing.direction, Const.Jing.direction_tx);
			dest.setProperty(Const.Jing.text_overview, overview);
			dest.setProperty(Const.Jing.addr_to, to);

			if (src != null) {
				Set<Object> keys = src.keySet();
				for (Object k : keys) {
					String key = k.toString();
					String value = src.getProperty(key);
					dest.setProperty(key, value);
				}
			}

			String prefix = "jing-tx";
			File file = this.__gen_sms_buffer_file_path(prefix);
			OutputStream out = new FileOutputStream(file);
			dest.store(out, file.getName());
			out.flush();
			out.close();
			System.out.println("write to " + file);

			this.__do_msg_rt();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void receiveMessage(String from, String text) {
		try {
			String url = null;
			text = text.trim();
			if (!text.startsWith("jing:")) {
				System.err.println("it's not a jing-sms : " + from + " << "
						+ text);
				return;
			} else {
				int index = text.indexOf("http");
				url = text.substring(index);
			}

			// gen rx task
			Properties prop = new Properties();
			prop.setProperty(Const.Jing.direction, Const.Jing.direction_rx);
			prop.setProperty(Const.Jing.addr_from, from);
			prop.setProperty(Const.Jing.message_url, url);

			String prefix = "jing-rx";
			File file = this.__gen_sms_buffer_file_path(prefix);
			File dir = file.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			OutputStream out = new FileOutputStream(file);
			prop.store(out, file.getName());
			out.flush();
			out.close();

			this.__do_msg_rt();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private File __gen_sms_buffer_file_path(String prefix) {
		long now = System.currentTimeMillis();
		JingRepo repo = _client.getRepo();
		File dir = repo.getFile(JingRepo.dir_sms_task);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (prefix == null) {
			return new File(dir, "default");
		}
		return new File(dir, "tmp_" + prefix + now + ".properties");
	}

	@Override
	public void sendMessage() {
		this.__do_msg_rt();
	}

	@Override
	public void receiveMessage() {
		this.__do_msg_rt();
	}

	private void __do_msg_rt() {
		JingClient client = _client;
		Runnable runn = new MessageRT(client);
		(new Thread(runn)).start();
	}

	@Override
	public List<JingMessageOverview> listAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
