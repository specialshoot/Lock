package future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Callable+Future获取执行结果
 * 
 * http://www.cnblogs.com/dolphin0520/p/3949310.html
 * 
 * @author han
 *
 */
public class FutureTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService executor = Executors.newCachedThreadPool();// ExecutorService表述了异步执行的机制，并且可以让任务在后台执行
		Task task = new Task();
		Future<Integer> result = executor.submit(task);
		executor.shutdown();

		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		System.out.println("主线程在执行任务");
		try {
			System.out.println("task运行结果" + result.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println("所有任务执行完毕");
	}

	/**
	 * Callable与Runnable的不同就是Callable可以返回一个值
	 * 
	 * @author han
	 *
	 */
	static class Task implements Callable<Integer> {

		@Override
		public Integer call() throws Exception {
			// TODO Auto-generated method stub
			System.out.println("子线程正在进行计算");
			Thread.sleep(3000);
			int sum = 0;
			for (int i = 0; i < 100; i++) {
				sum += i;
			}
			return sum;
		}
	}

}
