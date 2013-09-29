package impl.jing.lite.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import ananas.jing.lite.core.Const;
import ananas.jing.lite.core.JingRepo;
import ananas.jing.lite.core.LocalXGitObject;
import ananas.jing.lite.core.RemoteXGitObject;
import ananas.jing.lite.core.client.JingClient;
import ananas.jing.lite.core.xgitp.XGITPContext;
import ananas.jing.lite.core.xgitp.XGITPRequest;
import ananas.jing.lite.core.xgitp.XGITPRequestFactory;

public class JingClientImpl implements JingClient {

	private final JingRepo _repo;
	private final String _url;
	private XGITPContext _master_context;

	public JingClientImpl(File repo, String url) {
		this._repo = new DefaultJingRepo(repo);
		this._url = url;
	}

	@Override
	public JingRepo getRepo() {
		return this._repo;
	}

	@Override
	public RemoteXGitObject push(LocalXGitObject go) {

		try {
			InputStream in = new FileInputStream(go.getFile());

			XGITPRequestFactory factory = XGITPRequestFactory.Agent
					.getInstance();
			XGITPContext context = this.__getMasterContext();
			XGITPRequest req = factory.request(go.getSha1(), context);
			req.push(in);
			in.close();
			int code = req.getHttpResponseCode();
			if (code != 200) {
				String msg = req.getHttpResponseMessage();
				throw new IOException("HTTP " + code + " " + msg);
			}

			return this.__buildRemoteXGitObject(req);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private RemoteXGitObject __buildRemoteXGitObject(XGITPRequest req) {
		Map<String, String> map = new HashMap<String, String>();

		map.put(Const.XGITP.endpoint, req.getContext().getEndpointURL());
		map.put(Const.XGITP.object_length, req.getLength() + "");
		map.put(Const.XGITP.object_sha1, req.getSHA1());
		map.put(Const.XGITP.object_type, req.getType());
		map.put(Const.XGITP.object_url_full, req.getLongURL());
		map.put(Const.XGITP.object_url_short, req.getShortURL());

		return new DefaultRemoteXGitObject(map);
	}

	private XGITPContext __getMasterContext() {
		try {
			XGITPContext context = this._master_context;
			if (context != null) {
				return context;
			}

			XGITPRequestFactory factory = XGITPRequestFactory.Agent
					.getInstance();
			XGITPRequest req = factory.request(this._url);
			req.head();
			int code = req.getHttpResponseCode();
			if (code != 200) {
				String msg = req.getHttpResponseMessage();
				throw new IOException("HTTP " + code + " " + msg);
			}
			context = req.getContext();
			this._master_context = context;
			return context;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private RemoteXGitObject __buildRemoteXGitObject(HttpURLConnection conn) {
		Map<String, String> map = new HashMap<String, String>();
		__buildRemoteXGitObjectParam(map, conn, Const.XGITP.endpoint);
		__buildRemoteXGitObjectParam(map, conn, Const.XGITP.object_length);
		__buildRemoteXGitObjectParam(map, conn, Const.XGITP.object_sha1);
		__buildRemoteXGitObjectParam(map, conn, Const.XGITP.object_type);
		__buildRemoteXGitObjectParam(map, conn, Const.XGITP.object_url_full);
		__buildRemoteXGitObjectParam(map, conn, Const.XGITP.object_url_short);
		return new DefaultRemoteXGitObject(map);
	}

	private void __buildRemoteXGitObjectParam(Map<String, String> map,
			HttpURLConnection conn, String key) {
		String value = conn.getHeaderField(key);
		if (value != null) {
			map.put(key, value);
		}
	}

	private LocalXGitObject __pull(String ep_url, String sha1) {

		LocalXGitObject go = this.getRepo().getXGitObject(sha1);
		if (go.exists()) {
			return go;
		}

		HttpURLConnection conn = null;

		try {

			URL url = new URL(ep_url);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty(Const.XGITP.method, Const.XGITP.method_get);
			conn.setRequestProperty(Const.XGITP.object_sha1, sha1);

			int code = conn.getResponseCode();
			if (code != HttpURLConnection.HTTP_OK) {
				String htmsg = conn.getResponseMessage();
				String msg = "HTTP " + code + " " + htmsg + " ## xgit pull "
						+ sha1 + " from " + ep_url;
				throw new RuntimeException(msg);
			}

			InputStream in = conn.getInputStream();
			if (this.getRepo().getApiL().addZippedObject(go, in)) {
				go = this.getRepo().getXGitObject(sha1);
			} else {
				go = null;
			}
			in.close();

		} catch (Exception e) {
			go = null;
			e.printStackTrace();
		}

		if (conn != null) {
			conn.disconnect();
		}

		return go;
	}

	@Override
	public LocalXGitObject pull(String ep_url, String sha1) {
		return this.__pull(ep_url, sha1);
	}

	@Override
	public LocalXGitObject pull(String url) {
		RemoteXGitObject go = this.head(url);
		String ep = go.getEndpointURL();
		String sha1 = go.getSha1();
		return this.__pull(ep, sha1);
	}

	@Override
	public LocalXGitObject pull(LocalXGitObject go) {
		String sha1 = go.getSha1();
		String ep_url = this._url;
		return this.__pull(ep_url, sha1);
	}

	@Override
	public RemoteXGitObject head(String ep_url, String sha1) {
		HttpURLConnection conn = null;
		RemoteXGitObject go = null;
		try {
			URL url = new URL(ep_url);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty(Const.XGITP.method, Const.XGITP.method_head);
			conn.setRequestProperty(Const.XGITP.object_sha1, sha1);
			final int code = conn.getResponseCode();
			switch (code) {
			case HttpURLConnection.HTTP_OK: {
				go = this.__buildRemoteXGitObject(conn);
				break;
			}
			default:
				String msg = "HTTP " + code + " " + conn.getResponseMessage();
				throw new RuntimeException(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (conn != null) {
			conn.disconnect();
		}
		return go;
	}

	public RemoteXGitObject __head(String urlstr, int depthLimit) {

		HttpURLConnection conn = null;
		RemoteXGitObject go = null;

		try {
			URL url = new URL(urlstr);
			conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.setRequestProperty(Const.XGITP.method, Const.XGITP.method_head);

			final int code = conn.getResponseCode();
			switch (code) {
			case HttpURLConnection.HTTP_OK: {
				String ep_url = conn.getHeaderField(Const.XGITP.endpoint);
				String sha1 = conn.getHeaderField(Const.XGITP.object_sha1);
				go = this.head(ep_url, sha1);
				break;
			}
			case HttpURLConnection.HTTP_MOVED_PERM:
			case HttpURLConnection.HTTP_MOVED_TEMP: {
				String new_url = "";// TODO ...
				__head(new_url, depthLimit - 1);
				break;
			}
			default:
				String msg = "HTTP " + code + " " + conn.getResponseMessage();
				throw new RuntimeException(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (conn != null) {
			conn.disconnect();
		}

		return go;
	}

	@Override
	public RemoteXGitObject head(String url) {
		return __head(url, 8);
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
	public void receiveMessage(String from, String url) {
		try {

			Properties prop = new Properties();
			prop.setProperty(Const.Jing.direction, Const.Jing.direction_rx);
			prop.setProperty(Const.Jing.addr_from, from);
			prop.setProperty(Const.Jing.message_url, url);

			String prefix = "jing-rx";
			File file = this.__gen_sms_buffer_file_path(prefix);
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
		JingRepo repo = this.getRepo();
		File dir = repo.getFile(JingRepo.dir_sms_buffer);
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
		JingClient client  = this ;
		Runnable runn = new MessageRT( client );
		(new Thread(runn)).start();
	}

}
