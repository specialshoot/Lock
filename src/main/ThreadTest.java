package main;

public class ThreadTest extends Thread {
	
	private int threadNo;
	
	public ThreadTest(int threadNo){
		this.threadNo=threadNo;
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		for(int i=1;i<10;i++){
			new ThreadTest(i).start();
			Thread.sleep(1);
		}
	}

	/**
	 * run方法还是加了一个synchronized关键字的，按道理说，这些线程应该可以一个接一个的执行这个run方法才对阿。
	 * 然而即使加了synchronized还是混乱输出
	 * 原因：
	 * 我们提到的，对于一个成员方法加synchronized关键字，这实际上是以这个成员方法所在的对象本身作为对象锁。
	 * 在本例中，就是 以ThreadTest类的一个具体对象，也就是该线程自身作为对象锁的。
	 * 一共十个线程，每个线程持有自己 线程对象的那个对象锁。
	 * 这必然不能产生同步的效果。换句话说，如果要对这些线程进行同步，那么这些线程所持有的对象锁应当是共享且唯一的！
	 */
	@Override
	public synchronized void run() {
		// TODO Auto-generated method stub
		for(int i=1;i<50;i++){
			System.out.println("No."+threadNo+" : "+i);
		}
	}

}
