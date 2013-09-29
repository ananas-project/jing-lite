package ananas.jing.lite.core;

import ananas.jing.lite.core.xgit.XGitRepo;

public interface JingRepo extends XGitRepo {

	String dir_temp = "xgit/temp";

	String dir_sms = "xgit/sms";
	String dir_sms_buffer = "xgit/sms/buffer";
	String dir_sms_objects = "xgit/sms/objects";

	// XGitObject getXGitObject(String sha1);

}
