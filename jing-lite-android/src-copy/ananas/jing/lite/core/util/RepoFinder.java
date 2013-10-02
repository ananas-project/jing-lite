package ananas.jing.lite.core.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface RepoFinder {

	List<File> find(File root, int depthLimit);

	File findUp(File path);

	class Factory {

		public static RepoFinder newFinder() {
			return new TheImpl();
		}

		private static class TheImpl implements RepoFinder {

			static final String dir_name = ".xgit";

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

				File xgit = new File(path, dir_name);
				if (xgit.exists()) {
					list.add(xgit);
					System.out.println("    list << " + xgit);
					return;
				}

				for (File file : ls) {
					this.__find(list, file, depthLimit - 1);
				}
			}

			@Override
			public File findUp(File path) {
				for (int i = 0; path != null; path = path.getParentFile(), i++) {
					if (i > 100)
						break;
					File dir = new File(path, dir_name);
					if (dir.exists())
						if (dir.isDirectory())
							return dir;
				}
				return null;
			}
		}

	}

}
