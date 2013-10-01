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
import ananas.jing.lite.core.xgitp.XGITPResponse;

public class XGITPRequestImpl implements XGITPRequest {

	private final String _origin_url;
	private String _sha1;
	private XGITPContext _context;

	public XGITPRequestImpl(URI uri) {
		this._origin_url = (uri == null) ? null : uri.toString();
	}

	public XGITPRequestImpl(String sha1, URI uri) {
		this._sha1 = sha1;
		this._origin_url = (uri == null) ? null : uri.toString();
	}

	public XGITPRequestImpl(String sha1, XGITPContext context) {
		this._sha1 = sha1;
		this._context = context;
		this._origin_url = context.getEndpointURL();
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
	public String getOriginURL() {
		return this._origin_url.toString();
	}

	private void __close_conn(HttpURLConnection conn) {

		conn.disconnect();

		if (this._sha1 == null) {
			this._sha1 = conn.getHeaderField(Const.XGITP.object_sha1);
		}

		if (this._context == null) {
			String ep = conn.getHeaderField(Const.XGITP.endpoint);
			if (ep != null) {
				this._context = new XGITPContextImpl(ep);
			}
		}

	}

	private XGITPResponse __build_response(HttpURLConnection conn, Exception ee) {
		return new XGITPResponseImpl(this.getContext(), conn, ee);
	}

	private HttpURLConnection __open_conn(String method)
			throws MalformedURLException, IOException {

		String sha1 = this._sha1;
		String url = this._origin_url;
		if (url == null) {
			XGITPContext context = this._context;
			url = context.getEndpointURL();
		}

		// /////////////////////

		HttpURLConnection conn = (HttpURLConnection) (new URL(url))
				.openConnection();

		if (sha1 != null)
			conn.setRequestProperty(Const.XGITP.object_sha1, sha1);
		conn.setRequestProperty(Const.XGITP.method, method);

		if (Const.XGITP.method_put.equals(method)) {
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
		} else {
			conn.setRequestMethod("GET");
		}
		return conn;
	}

	private void __proc_conn(HttpURLConnection conn, InputStream in,
			OutputStream out) throws IOException {

		// req

		if (in != null) {
			OutputStream out2 = conn.getOutputStream();
			(new StreamPump(in, out2)).run();
		}

		String url = conn.getURL().toString();
		int code = conn.getResponseCode();

		// resp

		if (code == 200) {
			String ep = conn.getHeaderField(Const.XGITP.endpoint);
			if (ep == null) {
				conn.disconnect();
				throw new RuntimeException("It's not a xgitp service : " + url);
			}

			if (out != null) {
				InputStream in2 = conn.getInputStream();
				(new StreamPump(in2, out)).run();
			}

		} else {
			conn.disconnect();
			String msg = conn.getResponseMessage();
			throw new RuntimeException("xgitp - HTTP " + code + " " + msg
					+ " : " + url);
		}

	}

	@Override
	public XGITPResponse push(InputStream in) {
		String method = Const.XGITP.method_put;
		HttpURLConnection conn = null;
		Exception ee = null;
		try {
			conn = this.__open_conn(method);
			this.__proc_conn(conn, in, null);
			this.__close_conn(conn);
		} catch (Exception e) {
			e.printStackTrace();
			ee = e;
		}
		return this.__build_response(conn, ee);
	}

	@Override
	public XGITPResponse head() {
		String method = Const.XGITP.method_head;
		HttpURLConnection conn = null;
		Exception ee = null;
		try {
			conn = this.__open_conn(method);
			this.__proc_conn(conn, null, null);
			this.__close_conn(conn);
		} catch (Exception e) {
			e.printStackTrace();
			ee = e;
		}
		return this.__build_response(conn, ee);
	}

	@Override
	public XGITPResponse pull(OutputStream out) {
		String method = Const.XGITP.method_get;
		HttpURLConnection conn = null;
		Exception ee = null;
		try {
			conn = this.__open_conn(method);
			this.__proc_conn(conn, null, out);
			this.__close_conn(conn);
		} catch (Exception e) {
			e.printStackTrace();
			ee = e;
		}
		return this.__build_response(conn, ee);
	}

	@Override
	public XGITPResponse discovery() {
		String method = Const.XGITP.method_disc;
		HttpURLConnection conn = null;
		Exception ee = null;
		try {
			conn = this.__open_conn(method);
			this.__proc_conn(conn, null, null);
			this.__close_conn(conn);
		} catch (Exception e) {
			e.printStackTrace();
			ee = e;
		}
		return this.__build_response(conn, ee);
	}

}
