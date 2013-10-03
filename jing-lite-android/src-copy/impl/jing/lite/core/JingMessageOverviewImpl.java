package impl.jing.lite.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import ananas.jing.lite.core.Const;
import ananas.jing.lite.core.JingRepo;
import ananas.jing.lite.core.client.JingClient;
import ananas.jing.lite.core.client.JingMessageDetail;
import ananas.jing.lite.core.client.JingMessageOverview;

public class JingMessageOverviewImpl implements JingMessageOverview {

	private JingClient _client;
	private File _file;
	private Properties _prop;

	public JingMessageOverviewImpl(JingClient client, File file, Properties prop) {
		this._client = client;
		this._file = file;
		this._prop = prop;
	}

	@Override
	public JingMessageDetail getDetail() {
		return new MyDetail();
	}

	class MyDetail implements JingMessageDetail {

		private String _text;

		@Override
		public String getText() {
			String text = this._text;
			if (text == null) {
				text = this.__load_text();
			}
			return text;
		}

		private String __load_text() {
			try {
				final JingMessageOverviewImpl parent = JingMessageOverviewImpl.this;
				String sha1 = parent._prop.getProperty(Const.Jing.message_sha1);
				File dir = parent._client.getRepo().getFile(
						JingRepo.dir_sms_remote);
				File file = new File(dir, sha1);
				InputStream in = new FileInputStream(file);
				Properties prop = new Properties();
				in.close();
				return prop.getProperty(Const.Jing.text_detail) + "";
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
	}

	@Override
	public void load() {
		try {
			InputStream in = new FileInputStream(this._file);
			Properties prop = new Properties();
			prop.load(in);
			in.close();
			this._prop = prop;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save() {
		try {
			Properties prop = this._prop;
			String duty = prop.getProperty(Const.Jing.status_duty);
			if (duty == null)
				return;
			else
				prop.remove(Const.Jing.status_duty);
			OutputStream out = new FileOutputStream(_file);
			prop.store(out, _file.getName());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String __getString(String key) {
		String s = _prop.getProperty(key);
		if (s == null)
			return "";
		return s;
	}

	private long __getLong(String key) {
		String s = _prop.getProperty(key);
		if (s == null)
			return 0;
		return Long.parseLong(s);
	}

	private boolean __getBoolean(String key) {
		String s = _prop.getProperty(key);
		if (s == null)
			return false;
		s = s.trim();
		return (s.equals("yes") || s.equals("1") || s.equals("true"));
	}

	@Override
	public long getTimeRx() {
		return this.__getLong(Const.Jing.rx_time);
	}

	@Override
	public long getTimeTx() {
		return this.__getLong(Const.Jing.tx_time);
	}

	@Override
	public String getTitle() {
		return this.__getString(Const.Jing.text_title);
	}

	@Override
	public String getOverview() {
		return this.__getString(Const.Jing.text_overview);
	}

	@Override
	public boolean isWrite() {
		return this.__getBoolean(Const.Jing.status_write);
	}

	@Override
	public boolean isRead() {
		return this.__getBoolean(Const.Jing.status_read);
	}

	@Override
	public boolean isSend() {
		return this.__getBoolean(Const.Jing.status_send);
	}

	@Override
	public boolean isReceive() {
		return this.__getBoolean(Const.Jing.status_recv);
	}

	@Override
	public boolean isRx() {
		String s = __getString(Const.Jing.direction);
		return s.equals(Const.Jing.direction_rx);
	}

	@Override
	public boolean isTx() {
		String s = __getString(Const.Jing.direction);
		return s.equals(Const.Jing.direction_tx);
	}

	@Override
	public void delete() {
		_file.delete();
	}

	@Override
	public String getFromAddress() {
		return this.__getString(Const.Jing.tx_addr);
	}

	@Override
	public String getToAddress() {
		return this.__getString(Const.Jing.rx_addr);
	}

}
