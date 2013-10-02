package impl.jing.lite.core;

import java.util.Map;

import ananas.jing.lite.core.Const;
import ananas.jing.lite.core.RemoteXGitObject;

public class DefaultRemoteXGitObject implements RemoteXGitObject {

	private String _long_url;
	private String _short_url;
	private String _ep_url;
	private String _type;
	private String _sha1;
	private boolean _exists;
	private long _length;

	public DefaultRemoteXGitObject(Map<String, String> map) {

		this._ep_url = map.get(Const.XGITP.endpoint);
		this._long_url = map.get(Const.XGITP.object_url_full);
		this._short_url = map.get(Const.XGITP.object_url_short);
		this._type = map.get(Const.XGITP.object_type);
		this._sha1 = map.get(Const.XGITP.object_sha1);

		this._exists = ((this._sha1 + "").length() == 40);
		String length = map.get(Const.XGITP.object_length);
		if (length == null) {
			this._length = 0;
		} else {
			this._length = Long.parseLong(length);
		}
	}

	@Override
	public boolean exists() {
		return this._exists;
	}

	@Override
	public String getSha1() {
		return this._sha1;
	}

	@Override
	public String getType() {
		return this._type;
	}

	@Override
	public long getLength() {
		return this._length;
	}

	@Override
	public String getEndpointURL() {
		return this._ep_url;
	}

	@Override
	public String getLongURL() {
		return this._long_url;
	}

	@Override
	public String getShortURL() {
		return this._short_url;
	}

}
