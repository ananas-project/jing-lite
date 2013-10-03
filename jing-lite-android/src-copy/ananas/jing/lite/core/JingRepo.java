package ananas.jing.lite.core;

import ananas.jing.lite.core.xgit.XGitRepo;

public interface JingRepo extends XGitRepo {

	String dir_temp = "xgit/temp";

	String dir_sms_overview = "xgit/sms/overview";
	String dir_sms_task = "xgit/sms/buffer";
	String dir_sms_remote = "xgit/sms/objects";

	// XGitObject getXGitObject(String sha1);

}
