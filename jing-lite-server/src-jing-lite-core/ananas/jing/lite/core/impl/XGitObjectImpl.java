package ananas.jing.lite.core.impl;

import java.io.File;

import ananas.jing.lite.core.XGitObject;
import ananas.jing.lite.core.xgit.XGitRepo;

public class XGitObjectImpl implements XGitObject {

	private final XGitRepo _repo;
	private final String _sha1;
	private File _file;

	public XGitObjectImpl(XGitRepo repo, String sha1) {
		this._repo = repo;
		this._sha1 = sha1.toLowerCase().trim();
	}

	@Override
	public File getFile() {
		File file = this._file;
		if (file == null) {
			String s = this.getSha1();
			int index = 2;
			String p1 = s.substring(0, index);
			String p2 = s.substring(index);
			File dir = this._repo.getFile(XGitRepo.dir_objects);
			file = new File(dir, (p1 + "/" + p2));
			this._file = file;
		}
		return file;
	}

	@Override
	public String getSha1() {
		return this._sha1;
	}

	@Override
	public boolean exists() {
		return this.getFile().exists();
	}

}
