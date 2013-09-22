package ananas.jing.lite.core.xgit;

import ananas.jing.lite.core.XGitObject;
import ananas.jing.lite.core.fileman.FileManager;

public interface XGitRepo extends FileManager {

	String dir_workspace = "..";
	String dir_objects = "objects";
	String dir_refs = "refs";
	String dir_branches = "branches";

	XGitObject getXGitObject(String sha1);

	XGitApiL getApiL();

	XGitApiH getApiH();

}
