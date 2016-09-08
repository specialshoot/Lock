package future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Callable+FutureTask获取执行结果
 * 
 * @author han
 *
 */
public class FutureTest2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 第一种方式
		ExecutorService executor = Executors.newCachedThreadPool();
		Task task = new Task();
		FutureTask<Integer> futureTask = new FutureTask<>(task);
		executor.submit(futureTask);
		executor.shutdown();

		// 第二种方式,注意这种方式和第一种方式效果是类似的，
		// 只不过一个使用的是ExecutorService，一个使用的是Thread
		// Task task2=new Task();
		// FutureTask<Integer> futureTask2=new FutureTask<>(task2);
		// Thread thread=new Thread(futureTask);
		// thread.start();

		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		System.out.println("主线程在执行任务");

		try {
			System.out.println("task运行结果" + futureTask.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
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
