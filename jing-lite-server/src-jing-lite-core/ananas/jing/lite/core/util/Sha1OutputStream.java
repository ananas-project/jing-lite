package ananas.jing.lite.core.util;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1OutputStream extends OutputStream {

	private MessageDigest _md;
	private String _sha1;

	public Sha1OutputStream() {
	}

	@Override
	public void write(int b) throws IOException {
		MessageDigest md = this.__getMD();
		md.update((byte) b);
	}

	private MessageDigest __getMD() {
		MessageDigest md = this._md;
		if (md == null) {
			try {
				md = MessageDigest.getInstance("SHA-1");
				this._md = md;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return md;
	}

	private final static char[] char_array = "0123456789abcdef".toCharArray();

	public String getSha1() {
		String ret = this._sha1;
		if (ret == null) {
			MessageDigest md = this.__getMD();
			byte[] rlt = md.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : rlt) {
				int h, l;
				h = b >> 4;
				l = b;
				sb.append(char_array[0x0f & h]);
				sb.append(char_array[0x0f & l]);
			}
			ret = sb.toString();
			this._sha1 = ret;
		}
		return ret;
	}

}
