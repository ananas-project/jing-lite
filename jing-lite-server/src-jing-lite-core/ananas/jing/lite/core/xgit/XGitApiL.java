package ananas.jing.lite.core.xgit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ananas.jing.lite.core.LocalXGitObject;

public interface XGitApiL {

	boolean addZippedObject(LocalXGitObject go, InputStream in);

	boolean getZippedObject(LocalXGitObject go, OutputStream out);

	LocalXGitObject addRawObject(String type, File file);

	File newTempFile();

	XGitCheckout checkout(LocalXGitObject go) throws IOException;

}
