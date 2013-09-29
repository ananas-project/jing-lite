package ananas.jing.lite.core.xgitp;

public interface XGITPContext {

	String getOriginalURL();

	String getMiddleURL();

	String getEndpointURL();

	void setMiddleURL(String url);

	void setEndpointURL(String url);

}
