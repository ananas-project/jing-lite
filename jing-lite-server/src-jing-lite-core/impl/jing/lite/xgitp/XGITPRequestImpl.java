package impl.jing.lite.xgitp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import ananas.jing.lite.core.Const;
import ananas.jing.lite.core.util.StreamPump;
import ananas.jing.lite.core.xgitp.XGITPContext;
import ananas.jing.lite.core.xgitp.XGITPRequest;

public class XGITPRequestImpl implements XGITPRequest {

	private String _sha1;
	private final XGITPContext _context;
	private String _http_resp_message;
	private int _http_resp_code;
	private String _xgitp_resp_message;
	private int _xgitp_resp_code;
	private long _resp_length;
	private String _resp_type;
	private String _resp_long_url;
	private String _resp_short_url;

	public XGITPRequestImpl(URI url) {
		this._sha1 = null;
		this._context = new XGITPContextImpl(url);
	}

	public XGITPRequestImpl(String sha1, URI url) {
		this._sha1 = sha1;
		this._context = new XGITPContextImpl(url);
	}

	public XGITPRequestImpl(String sha1, XGITPContext context) {
		this._sha1 = sha1;
		this._context = context;
	}

	@Override
	public XGITPContext getContext() {
		return this._context;
	}

	@Override
	public String getSHA1() {
		return this._sha1;
	}

	@Override
	public void pull(OutputStream out) {

		try {
			String url = null;
			HttpURLConnection conn = this.__doGet(Const.XGITP.method_get, url,
					10);
			if (conn != null) {
				InputStream in = conn.getInputStream();
				(new StreamPump(in, out)).run();
				in.close();
				conn.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HttpURLConnection __doGet(String method, String url, int redirLimit)
			throws MalformedURLException, IOException {

		if (redirLimit < 0) {
			throw new RuntimeException(
					"the http(xgitp) redirection is too many!");
		}

		XGITPContext context = this.getContext();
		if (url == null) {
			url = context.getEndpointURL();
		}
		if (url == null) {
			url = context.getMiddleURL();
		}
		if (url == null) {
			url = context.getOriginalURL();
		}
		HttpURLConnection conn = (HttpURLConnection) (new URL(url))
				.openConnection();

		conn.setRequestMethod("GET");
		conn.setRequestProperty(Const.XGITP.method, method);
		final String sha1 = this._sha1;
		if (sha1 != null)
			conn.setRequestProperty(Const.XGITP.object_sha1, sha1);

		int code = conn.getResponseCode();
		String msg = conn.getResponseMessage();
		this._http_resp_code = code;
		this._http_resp_message = msg;
		if (code == 200) {
			// http is ok
			String ver = conn.getHeaderField(Const.XGITP.version);
			if (ver == null) {
				conn.disconnect();
				throw new RuntimeException("the url is not a xgitp service : "
						+ url);
			}
			String s = conn.getHeaderField(Const.XGITP.status_code);
			code = Integer.parseInt(s);
			msg = conn.getHeaderField(Const.XGITP.status_message);
			this._xgitp_resp_code = code;
			this._xgitp_resp_message = msg;
		}

		String location = conn.getHeaderField("Location");
		String res___ep = conn.getHeaderField(Const.XGITP.endpoint);
		String res__len = conn.getHeaderField(Const.XGITP.object_length);
		String res_type = conn.getHeaderField(Const.XGITP.object_type);
		String res_furl = conn.getHeaderField(Const.XGITP.object_url_full);
		String res_surl = conn.getHeaderField(Const.XGITP.object_url_short);
		String res_sha1 = conn.getHeaderField(Const.XGITP.object_sha1);

		if (res___ep != null) {
			context.setEndpointURL(res___ep);
		}

		switch (code) {
		case HttpURLConnection.HTTP_OK: {

			if (res_sha1 == null) {
				conn.disconnect();
				return null;
			} else {

				if (sha1 == null) {
					this._sha1 = res_sha1;
				} else {

					if (!sha1.equalsIgnoreCase(res_sha1)) {
						conn.disconnect();
						throw new RuntimeException(
								"the sha1 in request & response is different!");
					}

				}

				if (res__len != null)
					this._resp_length = Long.parseLong(res__len);
				this._resp_type = res_type;
				this._resp_short_url = res_surl;
				this._resp_long_url = res_furl;

				return conn;
			}

		}
		case HttpURLConnection.HTTP_MOVED_PERM: {
			conn.disconnect();
			if (location != null) {
				context.setMiddleURL(location);
			}
			return this.__doGet(method, location, redirLimit - 1);
		}
		case HttpURLConnection.HTTP_MOVED_TEMP: {
			conn.disconnect();
			return this.__doGet(method, location, redirLimit - 1);
		}
		default:
			conn.disconnect();
			System.err.println("XGITP: " + url + " ==>> HTTP " + code + " "
					+ msg);
		}

		return null;

	}

	@Override
	public void push(InputStream in) {

		this.head();
		if (this.getXGitpResponseCode() == 200) {
			// lazy push
			return;
		}
		try {
			String sha1 = this._sha1 + "";
			String url = this.getContext().getEndpointURL();
			if (sha1.length() < 20) {
				throw new IOException("No sha1 for the XGITP request! ");
			}
			if (url == null) {
				throw new IOException("No Endpoint for the XGITP request! ");
			}
			HttpURLConnection conn = (HttpURLConnection) (new URL(url))
					.openConnection();

			conn.setRequestMethod("POST");
			conn.setRequestProperty(Const.XGITP.method, Const.XGITP.method_put);
			conn.setRequestProperty(Const.XGITP.object_sha1, sha1);

			conn.setDoOutput(true);
			OutputStream out = conn.getOutputStream();
			(new StreamPump(in, out)).run();
			out.close();

			int code = conn.getResponseCode();
			String message = conn.getResponseMessage();
			conn.disconnect();
			this._http_resp_code = code;
			this._http_resp_message = message;
			if (code != 200) {
				throw new IOException("HTTP " + code + " " + message);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void head() {
		try {
			String url = null;
			HttpURLConnection conn = this.__doGet(Const.XGITP.method_head, url,
					10);
			if (conn != null) {
				conn.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public long getLength() {
		return this._resp_length;
	}

	@Override
	public String getType() {
		return this._resp_type;
	}

	@Override
	public String getLongURL() {
		return this._resp_long_url;
	}

	@Override
	public String getShortURL() {
		return this._resp_short_url;
	}

	@Override
	public int getHttpResponseCode() {
		return this._http_resp_code;
	}

	@Override
	public int getXGitpResponseCode() {
		return this._xgitp_resp_code;
	}

	@Override
	public String getHttpResponseMessage() {
		return this._http_resp_message;
	}

	@Override
	public String getXGitpResponseMessage() {
		return this._xgitp_resp_message;
	}

}
