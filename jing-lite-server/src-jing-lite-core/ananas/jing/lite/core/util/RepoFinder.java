package ananas.jing.lite.core.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface RepoFinder {

	List<File> find(File root, int depthLimit);

	class Factory {

		public static RepoFinder newFinder() {
			return new TheImpl();
		}

		private static class TheImpl implements RepoFinder {

			@Override
			public List<File> find(File root, int depthLimit) {
				if (root == null) {
					root = File.listRoots()[0];
				}
				List<File> list = new ArrayList<File>();
				this.__find(list, root, depthLimit);
				return list;
			}

			private void __find(List<File> list, File path, int depthLimit) {
				if (depthLimit < 0)
					return;
				if (!path.isDirectory())
					return;
				File[] ls = path.listFiles();
				if (ls == null)
					return;
				System.out.println("find in " + path);
				for (File file : ls) {
					String name = file.getName();
					if (".xgit".equals(name)) {
						list.add(file);
						System.out.println("    list << " + file);
					}
					this.__find(list, file, depthLimit - 1);
				}
			}
		}

	}

}
