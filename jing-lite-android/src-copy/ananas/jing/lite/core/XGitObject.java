package ananas.jing.lite.core;

public interface XGitObject {

	boolean exists();

	String getSha1();

	String getType();

	long getLength();

}
