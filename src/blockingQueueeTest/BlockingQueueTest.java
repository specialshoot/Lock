package blockingQueueeTest;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 多线程阻塞队列 实现程序在一个目录及它的所有子目录下搜索所有文件,打印出包含指定关键字的行
 * 
 * @author han
 *
 */
public class BlockingQueueTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);
		System.out.print("Enter base directroy (e.g. /home/han/workspace/TestFile)");
		String directory = in.nextLine();
		System.out.print("Enter keyword (e.g. volatile)");
		String keyword = in.nextLine();
		in.close();

		final int FILE_QUEUE_SIZE = 10;// 文件队列数
		final int SEARCH_THREADS = 100;// 搜索线程数

		BlockingQueue<File> queue = new ArrayBlockingQueue<>(FILE_QUEUE_SIZE);

		FileEnumerationTask enumerator = new FileEnumerationTask(queue, new File(directory));
		new Thread(enumerator).start();
		for (int i = 1; i <= SEARCH_THREADS; i++) {
			new Thread(new SearchTask(queue, keyword)).start();
		}
	}
}

/**
 * 将文件夹下所有文件放到队列中
 * 
 * @author han
 *
 */
class FileEnumerationTask implements Runnable {

	public static File DUMMY = new File("");
	private BlockingQueue<File> queue;
	private File startingDirectory;

	public FileEnumerationTask(BlockingQueue<File> queue, File startingDirectory) {
		this.queue = queue;
		this.startingDirectory = startingDirectory;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			enumerate(startingDirectory);
			queue.put(DUMMY);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 
	 * @param directory
	 * @throws InterruptedException
	 */
	public void enumerate(File directory) throws InterruptedException {
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				enumerate(file);
			} else {
				queue.put(file);
			}
		}
	}
}

/**
 * 从文件夹队列中取出文件查找文件中带有关键字的行
 * 
 * @author han
 *
 */
class SearchTask implements Runnable {
	private BlockingQueue<File> queue;
	private String keyword;

	public SearchTask(BlockingQueue<File> queue, String keyword) {
		this.queue = queue;
		this.keyword = keyword;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			boolean done = false;
			while (!done) {
				File file = queue.take();
				if (file == FileEnumerationTask.DUMMY) {
					queue.put(file);
					done = true;
				} else {
					search(file);
				}
			}
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void search(File file) throws IOException {
		try (Scanner in = new Scanner(file)) {
			int lineNumber = 0;
			while (in.hasNextLine()) {
				lineNumber++;
				String line = in.nextLine();
				if (line.contains(keyword)) {
					System.out.printf("%s:%d:%s%n", file.getPath(), lineNumber, line);
				}
			}
		}
	}
}
