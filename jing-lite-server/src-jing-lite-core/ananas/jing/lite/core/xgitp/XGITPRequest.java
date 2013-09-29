package ananas.jing.lite.core.xgitp;

import java.io.InputStream;
import java.io.OutputStream;

public interface XGITPRequest {

	XGITPContext getContext();

	String getSHA1();

	void pull(OutputStream out);

	void push(InputStream in);

	void head();

	int getHttpResponseCode();

	int getXGitpResponseCode();

	String getHttpResponseMessage();

	String getXGitpResponseMessage();

	long getLength();

	String getType();

	String getLongURL();

	String getShortURL();

}
