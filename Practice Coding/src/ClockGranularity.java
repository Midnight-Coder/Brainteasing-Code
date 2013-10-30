import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
/*
 * Purpose is to test system clock granularity 
 */
class testSeveralObjects {
	byte byt;
	int integer;
	Integer INT;
	byte[] bytes;
	
	public byte getByt() {
		return byt;
	}
	public void setByt(byte byt) {
		this.byt = byt;
	}
	public int getInteger() {
		return integer;
	}
	public void setInteger(int integer) {
		this.integer = integer;
	}
	public Integer getINT() {
		return INT;
	}
	public void setINT(Integer iNT) {
		INT = iNT;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
public class ClockGranularity implements Runnable  {

	private static final int nanoInMilli = 1000000;
	
	private static final int MaxExecutionTimeMsec = 60*1000;
	//60*1000 milliseconds
	private static float delta =  0.01f ; /*Float => u-second calculations*/
	//Inter-arrival Time(IAT): in milliseconds
	private static final int ArraySize =(int) ((float)MaxExecutionTimeMsec/delta);
	static int tick = 0;
	static Queue<Object> requestQueue = new LinkedList<Object>();
	public static Thread t;
	static long[] ticksCount = new long[ArraySize];
	static Semaphore queueSemaphore = new Semaphore(1);
	static byte[] o;
	
	public static void initTests() {
		o = new byte[100000];
		for(int i=0;i<o.length;i++) {
			o[i] = 127;
		}
	}
	@Override
	public void run() {
		try {
			queueSemaphore.acquire();
			tick++;
			requestQueue.add(o);
			queueSemaphore.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}
	public static void main(String args[]) {
		t = new Thread(new ClockGranularity(),"lamda");
		initTests();
		//For nano secs:
		ClockGranularity.delta = delta*nanoInMilli;
		long execStartTime = System.currentTimeMillis();
		long experimentStartTime = System.nanoTime();
		long execDuration, experimentRuntime;

		do {
			execDuration = System.currentTimeMillis() - execStartTime;
			experimentRuntime = System.nanoTime() - experimentStartTime;
			if(experimentRuntime >= delta) {
				experimentStartTime = System.nanoTime();
				if(tick >= ticksCount.length) {
					System.err.println("ERR:" + tick);
				}
				else
					ticksCount[tick] = execDuration;
				t.run();
			}
		} while (execDuration <= MaxExecutionTimeMsec);
		try {
			output();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void output() throws IOException{
		String lineSeparator = System.lineSeparator();
		File directory = new File("C:\\Users\\Sagar\\git\\Brainteasing Code\\Practice Coding\\src\\ClockGranularity Test results\\Semaphore and Queue");
		File file = File.createTempFile("Array of 100k:" + delta + " nanosec", ".txt",directory);
		FileWriter writer = new FileWriter(file);
		writer.append("Index\tTimestamp\tQueue Contents" + lineSeparator);
		String summary = "tick:" + tick + ", tick[]" + ticksCount.length + ", queue<>" + requestQueue.size();
		for(int i = 0; i<tick; i++) {
			String temp = i + " ticks in " + ticksCount[i] + " msec\t" + "\t" + requestQueue.poll();
			System.out.println(temp);
			writer.append(temp + lineSeparator);
		}
		writer.append(lineSeparator + "Summary: " + lineSeparator);
		writer.append(summary + lineSeparator);
		System.out.println(summary);
		writer.close();
	}
}