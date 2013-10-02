package ananas.jing.lite.android.helper;

import java.io.File;
import java.util.Properties;

import ananas.jing.lite.core.JingEndpointFactory;
import ananas.jing.lite.core.JingSMSHandler;
import ananas.jing.lite.core.client.JingClient;

public class CoreAgentImpl implements CoreAgent {

	private JingClient _client;

	private CoreAgentImpl() {
	}

	@Override
	public JingClient getClient() {
		return this._client;
	}

	public static CoreAgent newInst() {
		CoreAgentImpl inst = new CoreAgentImpl();
		inst.init();
		return inst;
	}

	private void init() {

		File dir = android.os.Environment.getExternalStorageDirectory();
		File repo = new File(dir, "ananas/jing/lite/repo/.xgit");
		String url = "http://puyatech.com/jing/";

		JingEndpointFactory factory = JingEndpointFactory.Agent.getInstance();
		JingClient client = factory.newClient(repo, url);
		client.setSMSHandler(new MySMSHandler());
		this._client = client;
	}

	class MySMSHandler implements JingSMSHandler {

		@Override
		public void sendSMS(String to, String msg) {
			throw new RuntimeException("no impl");
		}

		@Override
		public void onReceive(JingClient client, Properties overview) {
			throw new RuntimeException("no impl");
		}
	}

}
