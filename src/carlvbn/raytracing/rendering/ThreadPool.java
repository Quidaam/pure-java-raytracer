package carlvbn.raytracing.rendering;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadPool {
	private BlockingQueue<Runnable> job;
	private List<Thread> workers;
	
	private class Worker implements Runnable{

		
		public void run() {
			while(!Thread.interrupted()) {
				try {
					Runnable r= job.take();
					r.run();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
	}
	public ThreadPool(int size, int nbT) {
		job= new ArrayBlockingQueue<>(size);
		workers= new ArrayList<>();
		for( int i=0; i< nbT; i++) {
			Thread t= new Thread(new Worker());
			workers.add(t);
			t.start();
		}
		
	}
	
	public void submit( Runnable r) throws InterruptedException {
		job.put(r);
	}
	
	public void shutdown() {
		for(Thread t: workers) {
			t.interrupt();
			try {
				t.join();
			}catch(InterruptedException e) {
				job.clear();
				workers.clear();
				e.printStackTrace();
				
			}
		}
		job.clear();
		workers.clear();
	}
	
}
