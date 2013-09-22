package ananas.jing.lite.core.xgit;

import java.io.IOException;
import java.io.InputStream;

public interface XGitCheckout {

	String getType();

	long getLength();

	InputStream getInputStream();

	void close() throws IOException;
}
