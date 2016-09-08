package main;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * http://www.cnblogs.com/dolphin0520/p/3923167.html 使用lockInterruptibly
 * lockInterruptibly()
 * 
 * 由于lockInterruptibly()的声明中抛出了异常，所以lock.lockInterruptibly()
 * 必须放在try块中或者在调用lockInterruptibly()的方法外声明抛出InterruptedException。
 * 
 * @author han
 *
 */
public class ReentrantLockTest4 {

	private ArrayList<Integer> arrayList = new ArrayList<>();
	private Lock lock = new ReentrantLock();// 注意這個地方

	public static void main(String[] args) {
		final ReentrantLockTest4 test = new ReentrantLockTest4();
		MyThread thread1 = new MyThread(test);
		MyThread thread2 = new MyThread(test);
		thread1.start();
		thread2.start();
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		thread2.interrupt();
	}

	public void insert(Thread thread) throws InterruptedException {
		lock.lockInterruptibly();// 如果需要正确中断,必须将获取锁放在外面,然后将InterruptedException抛出
		try {
			System.out.println(thread.getName() + "得到了锁");
			long startTime = System.currentTimeMillis();
			for (;;) {
				if (System.currentTimeMillis() - startTime >= 10000) {// 10秒
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			System.out.println(Thread.currentThread().getName() + "执行finally");
			lock.unlock();
			System.out.println(thread.getName() + "释放了锁");
		}
	}

}

class MyThread extends Thread {
	private ReentrantLockTest4 test = null;

	public MyThread(ReentrantLockTest4 test) {
		this.test = test;
	}

	@Override
	public void run() {
		try {
			test.insert(Thread.currentThread());
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(Thread.currentThread().getName() + "被中断");
		}
	}
}
