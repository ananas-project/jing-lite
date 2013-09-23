package ananas.jing.lite.core;

import java.io.File;

import ananas.jing.lite.core.xgit.XGitRepo;

public interface LocalXGitObject extends XGitObject {

	File getFile();

	XGitRepo getRepo();
}
