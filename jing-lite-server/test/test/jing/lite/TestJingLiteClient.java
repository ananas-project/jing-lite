package test.jing.lite;

import java.io.File;
import java.io.PrintStream;

import ananas.jing.lite.core.JingEndpointFactory;
import ananas.jing.lite.core.JingSMSHandler;
import ananas.jing.lite.core.RemoteXGitObject;
import ananas.jing.lite.core.client.JingClient;

public class TestJingLiteClient implements JingSMSHandler {

	private final JingClient _client;
	private final JingClient _client2;

	public TestJingLiteClient(JingClient client1, JingClient client2) {
		this._client = client1;
		this._client2 = client2;
	}

	public static void main(String[] arg) {

		File repo1 = new File("/home/xukun/test/jing-lite/repo1/.xgit");
		File repo2 = new File("/home/xukun/test/jing-lite/repo2/.xgit");
		String url = "http://puyatech.com/jing/";

		JingEndpointFactory factory = JingEndpointFactory.Agent.getInstance();

		JingClient client1 = factory.newClient(repo1, url);
		JingClient client2 = factory.newClient(repo2, url);
		System.out.println("" + client1);
		System.out.println("" + client2);

		TestJingLiteClient test = new TestJingLiteClient(client1, client2);

		client1.setSMSHandler(test);

		test.run();
	}

	private void run() {

		final JingClient client = this._client;

		client.getMessageManager().sendMessage("13066668888", "hello,world",
				null);

	}

	private void __print(RemoteXGitObject rgo) {

		PrintStream out = System.out;
		out.println(rgo + "");
		out.println("    ep     = " + rgo.getEndpointURL());
		out.println("    sha1   = " + rgo.getSha1());
		out.println("    type   = " + rgo.getType());
		out.println("    length = " + rgo.getLength());
		out.println("    url-l  = " + rgo.getLongURL());
		out.println("    url-s  = " + rgo.getShortURL());

	}

	@Override
	public void sendSMS(String addr, String msg) {

		System.out.println(this + ".sendSMS : " + addr + " << " + msg);

		this._client2.getMessageManager().receiveMessage(addr, msg);

	}

}
