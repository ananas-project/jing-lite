package impl.jing.lite.core;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import ananas.jing.lite.core.JingRepo;
import ananas.jing.lite.core.client.JingClient;
import ananas.jing.lite.core.client.JingMessageOverview;

public class JingMessageOverviewList extends ArrayList<JingMessageOverview> {

	private static final long serialVersionUID = 5789159367331311052L;

	private final JingClient _client;

	public JingMessageOverviewList(JingClient client) {
		_client = client;
		this.__load_all();
	}

	private void __load_all() {
		this.clear();
		JingRepo repo = _client.getRepo();
		File dir = repo.getFile(JingRepo.dir_sms_overview);
		File[] files = null;
		if (dir.exists()) {
			if (dir.isDirectory()) {
				files = dir.listFiles();
			}
		}
		if (files == null) {
			return;
		}
		for (File file : files) {
			JingMessageOverview ov = this.__load_file(file);
			if (ov != null)
				this.add(ov);
		}
	}

	private JingMessageOverview __load_file(File file) {
		try {
			FileInputStream in = new FileInputStream(file);
			Properties prop = new Properties();
			in.close();
			return new JingMessageOverviewImpl(_client, file, prop);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
