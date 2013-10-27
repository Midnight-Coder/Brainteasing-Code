import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
/*
 * Purpose is to test system clock granularity 
 */
public class ClockGranularity implements Runnable  {

	private static final int nanoInMilli = 1000000;
	private static final int conversion = nanoInMilli; //Milli-milli = 1 else nanoMilli
	private static final int MaxExecutionTimeMsec = 60*1000;
	//60*1000 milliseconds
	private static final int delta =  (int) ((0.1) *conversion);
	//Time between requests (IAT): in milli seconds * conversion factor
	private static final long ArraySize = (MaxExecutionTimeMsec/delta*conversion);
	static int tick = 0;
	static Queue<Object> requestQueue = new LinkedList<Object>();
	public static Thread t;
	static long[] ticksCount = new long[(int)ArraySize];
	static Semaphore queueSemaphore = new Semaphore(1);
	@Override
	public void run() {
		try {
			queueSemaphore.acquire();
			tick++;
			requestQueue.add(tick);
			queueSemaphore.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}
	public static void main(String args[]) {
		t = new Thread(new ClockGranularity(),"lamda");
		long execStartTime = System.currentTimeMillis();
		long experimentStartTime = System.currentTimeMillis();
		long execDuration, experimentRuntime;

		do {
			execDuration = System.currentTimeMillis() - execStartTime;
			experimentRuntime = System.currentTimeMillis() - experimentStartTime;
			if(experimentRuntime >= delta) {
				experimentStartTime = System.currentTimeMillis();
				if(tick >= ticksCount.length) {
					System.err.println("ERR:" + tick);
				}
				else
					ticksCount[tick] = execDuration;
				t.run();
			}
		} while (execDuration <= MaxExecutionTimeMsec);
		
		for(int i = 0; i<ticksCount.length; i++) {
			System.out.println(i + " ticks in " + ticksCount[i] + " msec\t" + "\t" + requestQueue.poll());
		}
		System.out.println(tick + " tick[]" + ticksCount.length + " queue" + requestQueue.size());
	}
}