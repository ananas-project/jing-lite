package impl.jing.lite.core;

import java.io.File;
import java.io.IOException;

import ananas.jing.lite.core.LocalXGitObject;
import ananas.jing.lite.core.xgit.XGitCheckout;
import ananas.jing.lite.core.xgit.XGitRepo;

public class XGitObjectImpl implements LocalXGitObject {

	private final XGitRepo _repo;
	private final String _sha1;
	private File _file;
	private Header _header;

	public XGitObjectImpl(XGitRepo repo, String sha1) {
		if (sha1 == null) {
			sha1 = "none";
		}
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

	@Override
	public String getType() {
		Header h = this.__getHead();
		if (h == null) {
			return null;
		} else {
			return h.getType();
		}
	}

	private Header __getHead() {
		Header h = this._header;
		if (h == null) {
			h = new Header();
			try {
				h.load(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this._header = h;
		}
		return h;
	}

	@Override
	public long getLength() {
		Header h = this.__getHead();
		if (h == null) {
			return 0;
		} else {
			return h.getLength();
		}
	}

	class Header {

		private String _type;
		private long _length;

		public String getType() {
			return this._type;
		}

		public void load(LocalXGitObject go) throws IOException {
			XGitCheckout co = go.getRepo().getApiL().checkout(go);
			this._length = co.getLength();
			this._type = co.getType();
			co.close();
		}

		public long getLength() {
			return this._length;
		}
	}

	@Override
	public XGitRepo getRepo() {
		return this._repo;
	}

	public String toString() {
		String sha1 = this.getSha1();
		String type = this.getType();
		long length = this.getLength();
		return ("[XGitObject sha1:" + sha1 + " type:" + type + " length:"
				+ length + "]");
	}

}
