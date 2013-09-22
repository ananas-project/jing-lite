package ananas.jing.lite.core.impl;

import java.io.File;

import ananas.jing.lite.core.JingRepo;

public class DefaultJingRepo extends DefaultXGitRepo implements JingRepo {

	public DefaultJingRepo(File base) {
		super(base);
	}

}
