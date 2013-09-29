package test.jing.lite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ananas.jing.lite.core.JingEndpointFactory;
import ananas.jing.lite.core.JingRepo;
import ananas.jing.lite.core.LocalXGitObject;
import ananas.jing.lite.core.RemoteXGitObject;
import ananas.jing.lite.core.client.JingClient;
import ananas.jing.lite.core.util.StreamPump;
import ananas.jing.lite.core.xgit.XGitCheckout;
import ananas.jing.lite.core.xgit.XGitRepo;

public class TestJingLiteClient {

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
		test.run();
	}

	private void run() {

		final JingClient client = this._client;

		client.sendMessage("13066668888", "hello,world", null);

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

}
