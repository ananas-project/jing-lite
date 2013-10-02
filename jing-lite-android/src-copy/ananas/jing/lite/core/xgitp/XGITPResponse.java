package ananas.jing.lite.core.xgitp;

public interface XGITPResponse {

	int getResponseCode();

	String getResponseMessage();

	long getLength();

	String getType();

	String getLongURL();

	String getShortURL();

	XGITPContext getContext();

	String getSHA1();

}
