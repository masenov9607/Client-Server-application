package server;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskManager {


	final Lock lock = new ReentrantLock();
	final Condition pendingTaskStatus = lock.newCondition();
	int pendingTaskCount = 0;

	ThreadPoolExecutor executor = 
			(ThreadPoolExecutor) Executors.newFixedThreadPool(2);

	public void submitTask(Runnable task){
		incrementPendingTaskCount();

		executor.submit(()->{
			task.run();
			decrementPendingTasksCount();
		});
	}

	void incrementPendingTaskCount(){
		lock.lock();
		pendingTaskCount++;
		lock.unlock();
	}
	void decrementPendingTasksCount(){

		lock.lock();
		pendingTaskCount--;
		
		if(0 == pendingTaskCount) {
			pendingTaskStatus.signal();
		}
		
		lock.unlock();
	}

	void waitAllPendingTasksToBeCompleted(long timeout){
		lock.lock();
		if(0 != pendingTaskCount){
			try {
				pendingTaskStatus.await(timeout, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			lock.unlock();
		}

	}
}
