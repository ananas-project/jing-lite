package ananas.jing.lite.core.xgitp;

import java.io.InputStream;
import java.io.OutputStream;

public interface XGITPRequest {

	XGITPContext getContext();

	String getSHA1();

	String getOriginURL();

	XGITPResponse pull(OutputStream out);

	XGITPResponse push(InputStream in);

	XGITPResponse head();

	XGITPResponse discovery();

}
