package ananas.jing.lite.core.impl;

import java.io.IOException;
import java.io.InputStream;

import ananas.jing.lite.core.xgit.XGitCheckout;

public class XGitCheckoutImpl implements XGitCheckout {

	private final String _type;
	private final long _length;
	private InputStream _in;

	public XGitCheckoutImpl(String type, long length, InputStream in) {
		this._length = length;
		this._type = type;
		this._in = in;
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
	public InputStream getInputStream() {
		return this._in;
	}

	@Override
	public void close() throws IOException {
		final InputStream in = this._in;
		if (in != null) {
			this._in = null;
			in.close();
		}
	}

}
