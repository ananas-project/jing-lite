package ananas.jing.lite.core;

import ananas.jing.lite.core.xgit.XGitRepo;

public interface JingRepo extends XGitRepo {

	String dir_temp = "xgit/temp";

	XGitObject getXGitObject(String sha1);

}
