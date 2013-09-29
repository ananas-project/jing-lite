package impl.jing.lite.xgitp;

import java.net.URI;

import ananas.jing.lite.core.xgitp.XGITPContext;
import ananas.jing.lite.core.xgitp.XGITPRequest;
import ananas.jing.lite.core.xgitp.XGITPRequestFactory;

public class XGITPRequestFactoryImpl implements XGITPRequestFactory {

	private static XGITPRequestFactory _inst;

	public static XGITPRequestFactory getInstance() {
		XGITPRequestFactory inst = _inst;
		if (inst == null) {
			_inst = inst = new XGITPRequestFactoryImpl();
		}
		return inst;
	}

	@Override
	public XGITPRequest request(String url) {
		URI uri = URI.create(url);
		return new XGITPRequestImpl(uri);
	}

	@Override
	public XGITPRequest request(String sha1, String url) {
		URI uri = URI.create(url);
		return new XGITPRequestImpl(sha1, uri);
	}

	@Override
	public XGITPRequest request(String sha1, XGITPContext context) {
		return new XGITPRequestImpl(sha1,  context );
	}

}
