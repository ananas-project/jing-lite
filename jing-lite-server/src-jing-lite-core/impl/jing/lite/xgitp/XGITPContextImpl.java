package impl.jing.lite.xgitp;

import java.net.URI;

import ananas.jing.lite.core.xgitp.XGITPContext;

public class XGITPContextImpl implements XGITPContext {

	private String _url_ep;
	private final String _url_raw;
	private String _url_mid;

	public XGITPContextImpl(URI url) {
		this._url_raw = url.toString();
	}

	@Override
	public String getEndpointURL() {
		return this._url_ep;
	}

	@Override
	public String getOriginalURL() {
		return this._url_raw;
	}

	@Override
	public String getMiddleURL() {
		return this._url_mid;
	}

	@Override
	public void setMiddleURL(String url) {
		if (url != null)
			this._url_mid = url;
	}

	@Override
	public void setEndpointURL(String url) {
		if (url != null)
			this._url_ep = url;
	}

}
