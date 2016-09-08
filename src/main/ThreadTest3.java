package main;

/**
 * 同ThreadTest2不同，没有锁住某个属性，而是锁住一个方法
 * 
 * 细心的读者发现了：这段代码没有使用main方法中创建的String对象作为这10个线程的线程锁。
 * 而是通过在run方法中调用本线程中一个静态的同步方法abc而实现了线程的同步。
 * 我想看到这里，你们应该很困惑：这里synchronized静态方法是用什么来做对象锁的呢？
 * 
 * 我们知道，对于同步静态方法，对象锁就是该静态放发所在的类的Class实例， 由于在JVM中，所有被加载的类都有唯一的类对象，具体到本例，就是唯一的
 * ThreadTest3.class对象。 不管我们创建了该类的多少实例，但是它的类实例仍然是一个！
 * 
 * 这样我们就知道了：
 * 
 * 1、对于同步的方法或者代码块来说，必须获得对象锁才能够进入同步方法或者代码块进行操作；
 * 2、如果采用method级别的同步，则对象锁即为method所在的对象，如果是静态方法，对象锁即指method所在的 Class对象(唯一)；
 * 3、对于代码块，对象锁即指synchronized(abc)中的abc；
 * 4、因为第一种情况，对象锁即为每一个线程对象，因此有多个，所以同步失效，第二种共用同一个对象锁lock，因此同步生效，第三个因为是
 * static因此对象锁为ThreadTest3的class 对象，因此同步生效。
 * 
 * 如上述正确，则同步有两种方式，同步块和同步方法（为什么没有wait和notify？这个我会在补充章节中做出阐述）
 * 
 * 如果是同步代码块，则对象锁需要编程人员自己指定，一般有些代码为synchronized(this)只有在单态模式才生效； （本类的实例有且只有一个）
 * 
 * 如果是同步方法，则分静态和非静态两种 。
 * 
 * 静态方法则一定会同步，非静态方法需在单例模式才生效，推荐用静态方法(不用担心是否单例)。
 * 
 * 所以说，在Java多线程编程中，最常见的synchronized关键字实际上是依靠对象锁的机制来实现线程同步的。
 * 我们似乎可以听到synchronized在向我们说：“给我一把锁，我能创造一个规矩”。
 * 
 * @author han
 *
 */
public class ThreadTest3 extends Thread {

	private int threadNo;

	public ThreadTest3(int threadNo) {
		this.threadNo = threadNo;
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		for (int i = 1; i < 10; i++) {
			new ThreadTest3(i).start();
			Thread.sleep(1);
		}
	}

	public static synchronized void abc(int threadNo) {
		for (int i = 1; i < 50; i++) {
			System.out.println("No." + threadNo + " : " + i);
		}
	}

	@Override
	public void run() {
		abc(threadNo);
	}

}
