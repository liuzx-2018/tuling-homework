package com.lewis;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class LongAdderTest {

	public final int THREAD_COUNT=10; 
	private final int TASK_COUNT=10;
	private final int TARGET_COUNT=100000000;
	
	private AtomicLong atomicLongVal = new AtomicLong();
	private LongAdder longAdderVal = new LongAdder();
	
	final CountDownLatch latchForAtomicLong = new CountDownLatch(TASK_COUNT);
	final CountDownLatch latchForLongAdder = new CountDownLatch(TASK_COUNT);
	
	private class AtomicLongThread implements Runnable{
		private long startTime;

		public AtomicLongThread(long startTime) {
			this.startTime = startTime;
		}

		public void run() {
			long v = atomicLongVal.get();
			while(v<TARGET_COUNT) {
				v = atomicLongVal.incrementAndGet();
			}
			long end =System.currentTimeMillis();
			System.out.println("AtomicLongThread Spend:"+
			(end-startTime)+"ms v"+ "=" + v);
			latchForAtomicLong.countDown();
		}
	}
	
	private class LongAdderThread implements Runnable{
		private long startTime;

		public LongAdderThread(long startTime) {
			this.startTime = startTime;
		}

		public void run() {
			long v = longAdderVal.sum();
			while(v<TARGET_COUNT) {
				longAdderVal.increment();
				v = longAdderVal.sum();
			}
			long end =System.currentTimeMillis();
			System.out.println("LongAdderThread Spend:"+
			(end-startTime)+"ms v"+ "=" + v);
			latchForLongAdder.countDown();
		}
	}
	
	public void testAtomicLongThread() throws InterruptedException {
		ExecutorService exe = Executors.newFixedThreadPool(THREAD_COUNT);
		long startTime = System.currentTimeMillis();
        AtomicLongThread atomicLongThread = new AtomicLongThread(startTime);
		for (int i = 0; i < TASK_COUNT; i++) {
			exe.submit(atomicLongThread);
		}
		latchForAtomicLong.await();
		exe.shutdown();
	}
	
    public void testLongAdderThread() throws InterruptedException {
        ExecutorService exe = Executors.newFixedThreadPool(THREAD_COUNT);
        long startTime = System.currentTimeMillis();
        LongAdderThread longAddderThread = new LongAdderThread(startTime);
        for(int i = 0; i < TASK_COUNT; i++) {
            exe.submit(longAddderThread);
        }
        latchForLongAdder.await();
        exe.shutdown();
    }
    
    public static void main(String[] args) throws InterruptedException {
		LongAdderTest test = new LongAdderTest();	
		test.testAtomicLongThread();
		test.testLongAdderThread();
	}
}
