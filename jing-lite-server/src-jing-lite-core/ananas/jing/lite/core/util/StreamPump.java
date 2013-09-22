package ananas.jing.lite.core.util;

import java.io.InputStream;
import java.io.OutputStream;

public class StreamPump implements Runnable {

	private final InputStream _in;
	private final OutputStream _out;

	public StreamPump(InputStream in, OutputStream out) {
		this._in = in;
		this._out = out;
	}

	@Override
	public void run() {
		try {
			InputStream in = this._in;
			OutputStream out = this._out;
			byte[] buf = new byte[1024];
			for (int cb = in.read(buf); cb > 0; cb = in.read(buf)) {
				out.write(buf, 0, cb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
