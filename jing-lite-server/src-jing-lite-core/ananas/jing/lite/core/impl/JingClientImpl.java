package ananas.jing.lite.core.impl;

import java.io.File;

import ananas.jing.lite.core.JingRepo;
import ananas.jing.lite.core.XGitObject;
import ananas.jing.lite.core.client.JingClient;

public class JingClientImpl implements JingClient {

	private final JingRepo _repo;

	public JingClientImpl(File repo, String url) {
		this._repo = new DefaultJingRepo(repo);
	}

	@Override
	public JingRepo getRepo() {
		return this._repo;
	}

	@Override
	public Exception push(XGitObject go) {
		// TODO Auto-generated method stub
		return null;
	}

}
