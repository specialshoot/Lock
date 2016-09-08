package main;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLockTest中锁乱了
 * 原因在于，在insert方法中的lock变量是局部变量，每个线程执行该方法时都会保存一个副本，那么理所当然每个线程执行到lock.lock()
 * 处获取的是不同的锁，所以就不会发生冲突。 把lock换为全局变量就是同一个副本获得的是同一把锁了,同一把锁关系就不会乱了.
 * 
 * @author han
 *
 */
public class ReentrantLockTest2 {

	private ArrayList<Integer> arrayList = new ArrayList<>();
	private Lock lock = new ReentrantLock();// 注意這個地方

	public static void main(String[] args) {
		final ReentrantLockTest2 test = new ReentrantLockTest2();

		for (int i = 0; i < 10; i++) {
			new Thread() {
				public void run() {
					test.insert(Thread.currentThread());
				}
			}.start();
		}
	}

	public void insert(Thread thread) {
		lock.lock();
		try {
			System.out.println(thread.getName() + "得到了锁");
			for (int i = 0; i < 5; i++) {
				arrayList.add(i);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			System.out.println(thread.getName() + "释放了锁");
			lock.unlock();
		}
	}

}
