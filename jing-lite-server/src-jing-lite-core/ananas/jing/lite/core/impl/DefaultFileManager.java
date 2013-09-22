package ananas.jing.lite.core.impl;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import ananas.jing.lite.core.fileman.FileManager;

public class DefaultFileManager implements FileManager {

	private final File _base;
	private Map<String, File> _mapper;

	public DefaultFileManager(File base) {
		this._base = base;
	}

	@Override
	public File getFile(String key) {
		Map<String, File> map = this.__getMapper();
		return map.get(key);
	}

	private Map<String, File> __getMapper() {
		Map<String, File> map = this._mapper;
		if (map == null) {
			map = new Hashtable<String, File>();
			Class<?> cls = this.getClass();
			Field[] fields = cls.getFields();
			for (Field field : fields) {

				Class<?> type = field.getType();
				String name = field.getName();
				if (type.equals(String.class))
					if (name.startsWith("dir_") || name.startsWith("file_")) {
						String key;
						try {
							key = field.get(null).toString();
						} catch (Exception e) {
							e.printStackTrace();
							key = "__unknow__";
						}
						File file = new File(this._base, key);
						map.put(key, file);
						System.out.println("map " + key + " ==>> " + file);
					}
			}
			this._mapper = map;
		}
		return map;
	}

	@Override
	public List<File> listFiles() {
		Map<String, File> map = this.__getMapper();
		return new ArrayList<File>(map.values());
	}
}
