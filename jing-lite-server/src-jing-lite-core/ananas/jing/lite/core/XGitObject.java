package ananas.jing.lite.core;

import java.io.File;

import ananas.jing.lite.core.xgit.XGitRepo;

public interface XGitObject {

	File getFile();

	String getSha1();

	String getType();

	long getLength();

	boolean exists();

	XGitRepo getRepo();
}
