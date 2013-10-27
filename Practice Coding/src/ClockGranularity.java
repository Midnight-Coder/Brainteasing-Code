
/*
 * Purpose is to test system clock granularity 
 */
public class ClockGranularity implements Runnable  {

	private static final int nanoInMilli = 1000000;
	private static final int MaxExecutionTimeMsec = 60*1000;
	//60*1000 milliseconds
	private static final int delta =  (int) ((0.01f) * nanoInMilli);
	//Time between requests (IAT): in milli seconds * conversion factor
	private static final int ArraySize = (MaxExecutionTimeMsec*(nanoInMilli/delta));
	static int tick = 0;
	public static Thread t;
	static long[] ticksCOunt = new long[ArraySize];
	
	@Override
	public void run() {
		tick += 1;
		
	}
	public static void main(String args[]) {
		t = new Thread(new ClockGranularity(),"lamda");
		long execStartTime = System.currentTimeMillis();
		long experimentStartTime = System.nanoTime();
		long execDuration, experimentRuntime;

		do {
			execDuration = System.currentTimeMillis() - execStartTime;
			experimentRuntime = System.nanoTime() - experimentStartTime;
			if(experimentRuntime >= delta) {
				experimentStartTime = System.nanoTime();
				ticksCOunt[tick] = execDuration;
				t.run();
			}
		} while (execDuration <= MaxExecutionTimeMsec);
		
		for(int i = 0; i<tick; i++) {
			System.out.println(i + " ticks in " + ticksCOunt[i] + " msec\t");
		}
	}
	
	
	/*
	 * An approach to use one thread asa 100 ms counter 
	 * TODO Make it work
	 */
	public static void twoThreadedImpl() throws InterruptedException {
		Thread supervisor = new Thread("100msec supervisor") {
			public void run(){
				long experimentStartTime = System.currentTimeMillis();
				long experimentRuntime = System.currentTimeMillis() - experimentStartTime;
				experimentRuntime = System.currentTimeMillis() - experimentStartTime;
				if(experimentRuntime >= delta) {
					t.run();
					experimentStartTime = System.currentTimeMillis();
				}
			}
		};
		long threadStart,start;
		threadStart = start = System.currentTimeMillis();
		long execDuration,threadDuration;
		supervisor.start();
		do{
			execDuration = System.currentTimeMillis() - start;
			threadDuration = System.currentTimeMillis() - threadStart;
			if(threadDuration >= 100) {
				System.out.println(tick + "@" + execDuration);
				if(tick%100 == 0) {
					System.out.println();
				}
				supervisor.run();
			}
		} while(execDuration <= MaxExecutionTimeMsec);
	}
}