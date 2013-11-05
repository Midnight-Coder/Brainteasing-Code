import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class QueueGranularity {
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		TestingQueueGranularity testingQueueGranularity = new TestingQueueGranularity();
		Thread producer = new Thread(testingQueueGranularity);
		for(int i=0;i<80000000;i++) {
				producer.run();
		}
		long insertionTime = System.currentTimeMillis() - startTime;
		float avgInsertionTime = (float)insertionTime/TestingQueueGranularity.queue.size();
		System.out.println("Time to insert " + TestingQueueGranularity.queue.size() + ": " + insertionTime);
		System.out.println("Avg. Insertion Time: " + avgInsertionTime);
	}
}
class TestingQueueGranularity implements Runnable{
	public static Queue<Integer> queue;
	static int i;
	public TestingQueueGranularity() {
		queue = new ConcurrentLinkedQueue<Integer>();
		i = 0;
	}
	@Override
	public void run() {
		queue.add(i);
	}
}