package impl.jing.lite.xgitp;

import java.net.HttpURLConnection;

import ananas.jing.lite.core.Const;
import ananas.jing.lite.core.xgitp.XGITPContext;
import ananas.jing.lite.core.xgitp.XGITPResponse;

public class XGITPResponseImpl implements XGITPResponse {

	private String _short_url;
	private int _code;
	private String _msg;
	private String _long_url;
	private String _type;
	private long _length;
	private final XGITPContext _context;
	private String _sha1;

	public XGITPResponseImpl(XGITPContext context, HttpURLConnection conn,
			Exception ee) {

		this._context = context;

		try {
			this._code = conn.getResponseCode();
			this._msg = conn.getResponseMessage();

			this._type = conn.getHeaderField(Const.XGITP.object_type);
			this._long_url = conn.getHeaderField(Const.XGITP.object_url_full);
			this._short_url = conn.getHeaderField(Const.XGITP.object_url_short);
			this._sha1 = conn.getHeaderField(Const.XGITP.object_sha1);

			String len = conn.getHeaderField(Const.XGITP.object_length);
			if (len != null)
				this._length = Long.parseLong(len);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (ee != null) {
			this._msg = ee.toString();
		}
	}

	@Override
	public int getResponseCode() {
		return this._code;
	}

	@Override
	public String getResponseMessage() {
		return this._msg;
	}

	@Override
	public long getLength() {
		return this._length;
	}

	@Override
	public String getType() {
		return this._type;
	}

	@Override
	public String getLongURL() {
		return this._long_url;
	}

	@Override
	public String getShortURL() {
		return this._short_url;
	}

	@Override
	public XGITPContext getContext() {
		return this._context;
	}

	@Override
	public String getSHA1() {
		return this._sha1;
	}

}
