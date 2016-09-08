package future;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池
 * 
 * @author han
 *
 */
public class ThreadPoolTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);
		System.out.print("Enter base directroy (e.g. /home/han/workspace/TestFile)");
		String directory = in.nextLine();
		System.out.print("Enter keyword (e.g. volatile)");
		String keyword = in.nextLine();
		in.close();

		ExecutorService pool = Executors.newCachedThreadPool();// newCachedThreadPool()创建一个可根据需要创建新线程的线程池
		MatchCounter counter = new MatchCounter(new File(directory), keyword, pool);
		Future<Integer> result = pool.submit(counter);

		try {
			System.out.println(result.get() + " matching files.");
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		pool.shutdown();

		int largestPoolSize = ((ThreadPoolExecutor) pool).getLargestPoolSize();
		System.out.println("largest pool size = " + largestPoolSize);
	}

	static class MatchCounter implements Callable<Integer> {
		private File directory;
		private String keyword;
		private ExecutorService pool;
		private int count;

		public MatchCounter(File directory, String keyword, ExecutorService pool) {
			// TODO Auto-generated constructor stub
			this.directory = directory;
			this.keyword = keyword;
			this.pool = pool;
		}

		@Override
		public Integer call() {
			// TODO Auto-generated method stub
			count = 0;
			try {
				File[] files = directory.listFiles();
				List<Future<Integer>> results = new ArrayList<>();
				for (File file : files) {
					if (file.isDirectory()) {
						MatchCounter counter = new MatchCounter(file, keyword, pool);
						Future<Integer> task = pool.submit(counter);
						results.add(task);
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
