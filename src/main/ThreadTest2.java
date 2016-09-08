package main;

/**
 * 针对ThreadTest的问题改进 synchronized锁住了lock
 * 
 * @author han
 *
 */
public class ThreadTest2 extends Thread {

	private int threadNo;
	private String lock;

	public ThreadTest2(int threadNo, String lock) {
		this.threadNo = threadNo;
		this.lock = lock;
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String lock = new String("lock");
		for (int i = 1; i < 10; i++) {
			new ThreadTest2(i, lock).start();
			Thread.sleep(1);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		/*
		 * 我们注意到，该程序通过在main方法启动10个线程之前，创建了一个String类型的对象。并通过ThreadTest2的构造函数，
		 * 将这个对象赋值 给每一个ThreadTest2线程对象中的私有变量lock。根据Java方法的传值特点，我们知道，
		 * 这些线程的lock变量实际上指向的是堆内存中的 同一个区域，即存放main函数中的lock变量的区域。
		 * 程序将原来run方法前的synchronized关键字去掉，换用了run方法中的一个synchronized块来实现。这个同步块的对象锁，
		 * 就是 main方法中创建的那个String对象。换句话说，他们指向的是同一个String类型的对象，对象锁是共享且唯一的！
		 */
		synchronized (lock) {
			for (int i = 1; i < 50; i++) {
				System.out.println("No." + threadNo + " : " + i);
			}
		}
	}

}
