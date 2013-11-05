import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
/*
 * Purpose is to test system clock granularity 
 */

public class ClockGranularity implements Runnable  {

	private static final int MaxExecutionTimeMsec = 60000;
	//60*1000 milliseconds
	private static float delta = 1; 
	//Inter-arrival Time(IAT): in milliseconds
	private static final int ArraySize =(int) ((float)MaxExecutionTimeMsec/delta);
	private static int tick = 0;
	private static String outputFileName ="temp" + delta + " msec @" + getTime();
	public static Thread t;
	private static long[] ticksCount = new long[ArraySize];
	
	private static final int convertMilliToNano = 1000000;
	private static Queue<Object> requestQueue = new LinkedList<Object>();
	private static Semaphore queueSemaphore = new Semaphore(1);
	private static byte[] o;
	
	public static void initTests() {
		o = new byte[100000];
		for(int i=0;i<o.length;i++) {
			o[i] = 127;
		}
	}
	private static String getTime() {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return format.format(cal.getTime());
	}
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
		initTests();
		//For nano secs:
		ClockGranularity.delta = delta*convertMilliToNano;
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
			printToFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void printToFile() throws IOException{
		String lineSeparator = System.lineSeparator();
		File directory = new File("C:\\Users\\Sagar\\git\\Brainteasing Code\\Practice Coding\\src\\ClockGranularity Test results\\Semaphore and Queue");
		File file = File.createTempFile(outputFileName, ".txt",directory);
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