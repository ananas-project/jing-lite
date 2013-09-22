package ananas.jing.lite.core.impl;

import java.io.File;

import ananas.jing.lite.core.JingRepo;
import ananas.jing.lite.core.XGitObject;

public class DefaultJingRepo extends DefaultXGitRepo implements JingRepo {

	public DefaultJingRepo(File base) {
		super(base);
	}

	@Override
	public XGitObject getXGitObject(String sha1) {
		// TODO Auto-generated method stub
		return null;
	}

}
