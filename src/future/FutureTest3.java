package future;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 使用Callable+FutureTask获取执行结果
 * 
 * @author han
 *
 */
public class FutureTest3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);
		System.out.print("Enter base directroy (e.g. /home/han/workspace/TestFile)");
		String directory = in.nextLine();
		System.out.print("Enter keyword (e.g. volatile)");
		String keyword = in.nextLine();
		in.close();

		MatchCounter counter = new MatchCounter(new File(directory), keyword);
		FutureTask<Integer> task = new FutureTask<>(counter);
		Thread t = new Thread(task);
		t.start();
		try {
			System.out.println(task.get() + " matching files.");
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	static class MatchCounter implements Callable<Integer> {
		private File directory;
		private String keyword;
		private int count;

		public MatchCounter(File directory, String keyword) {
			// TODO Auto-generated constructor stub
			this.directory = directory;
			this.keyword = keyword;
		}

		@Override
		public Integer call() throws Exception {
			// TODO Auto-generated method stub
			count = 0;
			try {
				File[] files = directory.listFiles();
				List<Future<Integer>> results = new ArrayList<>();
				for (File file : files) {
					if (file.isDirectory()) {
						MatchCounter counter = new MatchCounter(file, keyword);
						FutureTask<Integer> task = new FutureTask<>(counter);
						results.add(task);
						Thread t = new Thread(task);
						t.start();
					} else {
						if (search(file)) {
							count++;
						}
					}
				}
				for (Future<Integer> result : results) {
					try {
						count += result.get();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return count;
		}

		public boolean search(File file) {
			try {
				try (Scanner in = new Scanner(file)) {
					boolean found = false;
					while (!found && in.hasNextLine()) {
						String line = in.nextLine();
						if (line.contains(keyword)) {
							found = true;
						}
					}
					return found;
				}
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}

	}
}
