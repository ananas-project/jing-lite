package test.jing.lite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

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
		String url = "http://localhost:18080/jing-lite-server/ObjectHub";
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
		final JingRepo repo = client.getRepo();

		File obj_dir = repo.getFile(JingRepo.dir_objects);
		if (!obj_dir.exists()) {
			obj_dir.mkdirs();
		}
		// String repo_dir_name = repo.getFile(JingRepo.dir_repo).getName();
		File works = repo.getFile(JingRepo.dir_workspace);
		File[] list = works.listFiles();
		for (File file : list) {
			if (file.exists())
				if (!file.isDirectory()) {

					System.out.println("addFileAsObject : " + file);
					LocalXGitObject go = repo.getApiL().addRawObject("blob",
							file);
					System.out.println("added object " + go);

					RemoteXGitObject rgo = client.push(go);

					// this.__print(rgo);

					rgo = client.head(rgo.getLongURL());

					this.__print(rgo);

					JingClient client2 = this._client2;
					LocalXGitObject lgo2 = client2.pull(rgo.getLongURL());

					try {
						XGitCheckout co = client2.getRepo().getApiL()
								.checkout(lgo2);

						File works2 = client2.getRepo().getFile(
								XGitRepo.dir_workspace);

						String sha1 = lgo2.getSha1();
						OutputStream out = new FileOutputStream(new File(
								works2, sha1));

						InputStream in = co.getInputStream();
						StreamPump pump = (new StreamPump(in, out));
						pump.run();

						out.close();
						co.close();

					} catch (IOException e) {
						e.printStackTrace();
					}

				}
		}

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