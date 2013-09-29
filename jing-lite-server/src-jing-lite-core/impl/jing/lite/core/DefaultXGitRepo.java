package impl.jing.lite.core;

import java.io.File;

import ananas.jing.lite.core.LocalXGitObject;
import ananas.jing.lite.core.xgit.XGitApiH;
import ananas.jing.lite.core.xgit.XGitApiL;
import ananas.jing.lite.core.xgit.XGitRepo;

public class DefaultXGitRepo extends DefaultFileManager implements XGitRepo {

	private XGitApiL _api_low;
	private XGitApiH _api_high;

	public DefaultXGitRepo(File base) {
		super(base);
	}

	@Override
	public XGitApiL getApiL() {
		XGitApiL api = this._api_low;
		if (api == null) {
			api = new XGitApiLowImpl(this);
			this._api_low = api;
		}
		return api;
	}

	@Override
	public XGitApiH getApiH() {
		XGitApiH api = this._api_high;
		if (api == null) {
			api = new XGitApiHighImpl(this);
			this._api_high = api;
		}
		return api;
	}

	@Override
	public LocalXGitObject getXGitObject(String sha1) {
		XGitRepo repo = this;
		return new XGitObjectImpl(repo, sha1);
	}

}
