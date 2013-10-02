package ananas.jing.lite.core.fileman;

import java.io.File;
import java.util.List;

public interface FileManager {

	String dir_repo = "";

	File getFile(String key);

	List<File> listFiles();
}
