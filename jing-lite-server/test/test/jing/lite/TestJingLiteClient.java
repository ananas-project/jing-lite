package test.jing.lite;

import java.io.File;

import ananas.jing.lite.core.JingEndpointFactory;
import ananas.jing.lite.core.JingRepo;
import ananas.jing.lite.core.XGitObject;
import ananas.jing.lite.core.client.JingClient;

public class TestJingLiteClient {

	private final JingClient _client;

	public TestJingLiteClient(JingClient client) {
		this._client = client;
	}

	public static void main(String[] arg) {
		File repo = new File("/home/xukun/test/jing-lite/.repo");
		String url = "http://localhost:8080/jing-lite-server/ObjectHub";
		JingEndpointFactory factory = JingEndpointFactory.Agent.getInstance();
		JingClient client = factory.newClient(repo, url);
		System.out.println("" + client);

		TestJingLiteClient test = new TestJingLiteClient(client);
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
					XGitObject go = repo.getApiL().addRawObject("blob", file);
					System.out.println("added object " + go);

					client.push(go);

				}
		}

	}

}
