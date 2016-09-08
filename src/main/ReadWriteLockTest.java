package main;

/**
 * ReadWriteLock读写锁
 * 
 * 本历程展示没有使用读写锁而使用synchronized的情况
 * 这段程序的输出结果会是，直到thread1执行完读操作之后，才会打印thread2执行读操作的信息
 * 读操作是可以同时读的,用synchronized减慢了效率
 * 
 * @author han
 *
 */
public class ReadWriteLockTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final ReadWriteLockTest test = new ReadWriteLockTest();

		new Thread() {
			public void run() {
				test.get(Thread.currentThread());
			};
		}.start();

		new Thread() {
			public void run() {
				test.get(Thread.currentThread());
			};
		}.start();
	}

	/**
	 * 这段程序的输出结果会是，直到thread1执行完读操作之后，才会打印thread2执行读操作的信息
	 * 
	 * @param thread
	 */
	public synchronized void get(Thread thread) {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start <= 1) {
			System.out.println(thread.getName() + "正在进行读操作");
		}
		System.out.println(thread.getName() + "读操作完毕");
	}

}
