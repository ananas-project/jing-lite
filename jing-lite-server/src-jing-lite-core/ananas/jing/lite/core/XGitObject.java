package ananas.jing.lite.core;

import java.io.File;

public interface XGitObject {

	File getFile();

	String getSha1();

	boolean exists();
}
