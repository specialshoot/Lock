package main;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用tryLock 
 * 
 * lock()方法是平常使用得最多的一个方法，就是用来获取锁。如果锁已被其他线程获取，则进行等待。
 * 
 * tryLock()方法是有返回值的，它表示用来尝试获取锁，如果获取成功，则返回true，如果获取失败（即锁已被其他线程获取），则返回false，
 * 也就说这个方法无论如何都会立即返回。在拿不到锁时不会一直在那等待。
 * 
 * tryLock(long time, TimeUnit unit)方法和tryLock()方法是类似的
 * 
 * 只不过区别在于这个方法在拿不到锁时会等待一定的时间，在时间期限之内如果还拿不到锁，就返回false。
 * 如果如果一开始拿到锁或者在等待期间内拿到了锁，则返回true。
 * 
 * @author han
 *
 */
public class ReentrantLockTest3 {

	private ArrayList<Integer> arrayList = new ArrayList<>();
	private Lock lock = new ReentrantLock();// 注意這個地方

	public static void main(String[] args) {
		final ReentrantLockTest3 test = new ReentrantLockTest3();

		for (int i = 0; i < 10; i++) {
			new Thread() {
				public void run() {
					test.insert(Thread.currentThread());
				}
			}.start();
		}
	}

	public void insert(Thread thread) {
		if (lock.tryLock()) {
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
		} else {
			System.out.println(thread.getName() + "获取锁失败");
		}
	}

}
