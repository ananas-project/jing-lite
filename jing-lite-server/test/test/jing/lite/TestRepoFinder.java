package test.jing.lite;

import java.io.File;
import java.util.List;

import ananas.jing.lite.core.util.RepoFinder;

public class TestRepoFinder {

	public static void main(String[] arg) {

		long time0 = System.currentTimeMillis();
		RepoFinder finder = RepoFinder.Factory.newFinder();
		List<File> list = finder.find(new File("C:\\"), 4);
		for (File file : list) {
			System.out.println("result : " + file);
		}
		long time1 = System.currentTimeMillis();
		System.out.println("cost " + ((time1 - time0) / 1000) + " sec");
		System.out.println("eof");
	}
}
