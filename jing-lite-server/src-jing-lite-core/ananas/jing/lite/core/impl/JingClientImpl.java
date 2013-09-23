package ananas.jing.lite.core.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ananas.jing.lite.core.Const;
import ananas.jing.lite.core.JingRepo;
import ananas.jing.lite.core.RemoteXGitObject;
import ananas.jing.lite.core.LocalXGitObject;
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

			in = conn.getInputStream();
			in.close();
			out.close();
			conn.disconnect();

			// TODO ..return
			return null;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public LocalXGitObject pull(String ep_url, String sha1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalXGitObject pull(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RemoteXGitObject head(String ep_url, String sha1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RemoteXGitObject head(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalXGitObject pull(LocalXGitObject go) {
		// TODO Auto-generated method stub
		return null;
	}

}
