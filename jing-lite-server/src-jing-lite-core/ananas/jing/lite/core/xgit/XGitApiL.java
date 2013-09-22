package ananas.jing.lite.core.xgit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ananas.jing.lite.core.XGitObject;

public interface XGitApiL {

	boolean addZippedObject(XGitObject go, InputStream in);

	boolean getZippedObject(XGitObject go, OutputStream out);

	XGitObject addRawObject(String type, File file);

	File newTempFile();

	XGitCheckout checkout(XGitObject go) throws IOException;

}
