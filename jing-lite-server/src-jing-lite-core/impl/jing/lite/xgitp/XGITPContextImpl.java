package impl.jing.lite.xgitp;

import ananas.jing.lite.core.xgitp.XGITPContext;

public class XGITPContextImpl implements XGITPContext {

	private final String _url_ep;

	public XGITPContextImpl(String url) {
		this._url_ep = url;
	}

	@Override
	public String getEndpointURL() {
		return this._url_ep;
	}

}
