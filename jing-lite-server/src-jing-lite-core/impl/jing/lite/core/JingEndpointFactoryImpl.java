package impl.jing.lite.core;

import java.io.File;

import ananas.jing.lite.core.JingEndpointFactory;
import ananas.jing.lite.core.client.JingClient;
import ananas.jing.lite.core.server.JingServer;

public class JingEndpointFactoryImpl implements JingEndpointFactory {

	@Override
	public JingServer newServer(File repo, String url) {
		return new JingServerImpl(repo, url);
	}

	@Override
	public JingClient newClient(File repo, String url) {
		return new JingClientImpl(repo, url);
	}

}
