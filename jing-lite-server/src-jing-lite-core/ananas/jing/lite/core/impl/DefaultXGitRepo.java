package ananas.jing.lite.core.impl;

import java.io.File;

import ananas.jing.lite.core.xgit.XGitApiH;
import ananas.jing.lite.core.xgit.XGitApiL;
import ananas.jing.lite.core.xgit.XGitRepo;

public class DefaultXGitRepo extends DefaultFileManager implements XGitRepo {

	public DefaultXGitRepo(File base) {
		super(base);
	}

	@Override
	public XGitApiL getApiL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XGitApiH getApiH() {
		// TODO Auto-generated method stub
		return null;
	}

}
