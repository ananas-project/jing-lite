package ananas.jing.lite.core.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import ananas.jing.lite.core.Const;
import ananas.jing.lite.core.JingRepo;
import ananas.jing.lite.core.LocalXGitObject;
import ananas.jing.lite.core.RemoteXGitObject;
import ananas.jing.lite.core.client.JingClient;
import ananas.jing.lite.core.util.StreamPump;

public class JingClientImpl implements JingClient {

	private final JingRepo _repo;
	private final String _url;

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

			URL url = new URL(this._url);
			System.out.println(url + " << " + go.getSha1());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty(Const.XGITP.method, Const.XGITP.method_put);
			conn.setRequestProperty(Const.XGITP.object_sha1, go.getSha1());
			conn.setRequestProperty(Const.XGITP.object_type, go.getType());
			conn.setRequestProperty(Const.XGITP.object_length, go.getLength()
					+ "");
			conn.setDoOutput(true);

			OutputStream out = conn.getOutputStream();
			(new StreamPump(in, out)).run();

			in.close();

			int code = conn.getResponseCode();
			String msg = conn.getResponseMessage();
			System.out.println("HTTP " + code + " " + msg);

			RemoteXGitObject rgo = this.__buildRemoteXGitObject(conn);

			in = conn.getInputStream();
			in.close();
			out.close();
			conn.disconnect();

			return rgo;

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

}
