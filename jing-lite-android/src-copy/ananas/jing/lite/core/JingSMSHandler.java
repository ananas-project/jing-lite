package ananas.jing.lite.core;

import java.util.Properties;

import ananas.jing.lite.core.client.JingClient;

public interface JingSMSHandler {

	void sendSMS(String to, String msg);

	void onReceive(JingClient client, Properties overview);

}
