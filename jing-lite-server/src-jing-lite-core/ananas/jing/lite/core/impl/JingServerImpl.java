package ananas.jing.lite.core.impl;

import java.io.File;

import ananas.jing.lite.core.JingRepo;
import ananas.jing.lite.core.server.JingServer;

public class JingServerImpl implements JingServer {

	private final JingRepo _repo;

	public JingServerImpl(File repo, String url) {
		this._repo = new DefaultJingRepo(repo);
	}

	@Override
	public JingRepo getRepo() {
		return this._repo;
	}

}
