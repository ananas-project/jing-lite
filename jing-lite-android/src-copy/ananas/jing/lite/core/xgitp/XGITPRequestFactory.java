package ananas.jing.lite.core.xgitp;

import impl.jing.lite.xgitp.XGITPRequestFactoryImpl;

public interface XGITPRequestFactory {

	XGITPRequest request(String url);

	XGITPRequest request(String sha1, String url);

	XGITPRequest request(String sha1, XGITPContext context);

	class Agent {

		public static XGITPRequestFactory getInstance() {
			return XGITPRequestFactoryImpl.getInstance();
		}
	}

}
