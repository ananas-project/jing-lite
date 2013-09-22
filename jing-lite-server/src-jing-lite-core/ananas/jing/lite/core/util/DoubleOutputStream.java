package ananas.jing.lite.core.util;

import java.io.IOException;
import java.io.OutputStream;

public class DoubleOutputStream extends OutputStream {

	private final OutputStream _out1;
	private final OutputStream _out2;

	public DoubleOutputStream(OutputStream out1, OutputStream out2) {
		this._out1 = out1;
		this._out2 = out2;
	}

	@Override
	public void write(int b) throws IOException {
		this._out1.write(b);
		this._out2.write(b);
	}

	@Override
	public void flush() throws IOException {
		this._out1.flush();
		this._out2.flush();
	}
}
