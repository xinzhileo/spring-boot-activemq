package org.tongwoo.util;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentQueue {
	private LinkedList<Object> queue;
	private Lock lock;
	private Condition isEmptySignal;

	public ConcurrentQueue() {
		queue = new LinkedList<Object>();
		lock = new ReentrantLock();
		isEmptySignal = lock.newCondition();
	}

	public void offer(Object message) {
		try {
			lock.lock();
			if (queue.add(message)) {
				// System.out.println("offer msg.");
				isEmptySignal.signalAll();
			}
		} finally {
			lock.unlock();
		}
	}

	public Object take() {
		try {
			lock.lock();
			for (; queue.isEmpty();) {
				isEmptySignal.await();
				// System.out.println("take msg.");
			}

			return queue.poll();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		} finally {
			lock.unlock();
		}
	}

	public Object take(long millisecond) {
		try {
			lock.lock();
			if (queue.isEmpty())
				if (isEmptySignal.await(millisecond, TimeUnit.MILLISECONDS))
					return queue.poll();
				else
					return null;
			return queue.poll();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		} finally {
			lock.unlock();
		}
	}

	public int size() {
		lock.lock();
		try {
			return queue.size();
		} finally {
			lock.unlock();
		}
	}
}
