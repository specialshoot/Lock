package main;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLock读写锁
 * 
 * 说明thread1和thread2在同时进行读操作。
 * 
 * 这样就大大提升了读操作的效率。
 * 
 * 不过要注意的是，如果有一个线程已经占用了读锁，则此时其他线程如果要申请写锁，则申请写锁的线程会一直等待释放读锁。
 * 
 * 如果有一个线程已经占用了写锁，则此时其他线程如果申请写锁或者读锁，则申请的线程会一直等待释放写锁。
 * 
 * 关于ReentrantReadWriteLock类中的其他方法感兴趣的朋友可以自行查阅API文档。
 * 
 * @author han
 *
 */
public class ReadWriteLockTest2 {

	private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final ReadWriteLockTest2 test = new ReadWriteLockTest2();

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
	public void get(Thread thread) {
		rwl.readLock().lock();
		try {
			long start = System.currentTimeMillis();
			while (System.currentTimeMillis() - start <= 1) {
				System.out.println(thread.getName() + "正在进行读操作");
			}
			System.out.println(thread.getName() + "读操作完毕");
		} catch (Exception e) {
		} finally {
			rwl.readLock().unlock();
		}
	}

}
