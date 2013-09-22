package ananas.jing.lite.core;

import java.io.File;

import ananas.jing.lite.core.client.JingClient;
import ananas.jing.lite.core.impl.JingEndpointFactoryImpl;
import ananas.jing.lite.core.server.JingServer;

public interface JingEndpointFactory {

	JingServer newServer(File repo, String url);

	JingClient newClient(File repo, String url);

	class Agent {

		public static JingEndpointFactory getInstance() {
			return new JingEndpointFactoryImpl();
		}
	}
}
