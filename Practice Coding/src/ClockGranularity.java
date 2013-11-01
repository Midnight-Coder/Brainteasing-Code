import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;
/*
 * Purpose is to test system clock granularity 
 */


public class ClockGranularity{
	private static final int MaxExecutionTimeMsec = 60000;
	//60*1000 milliseconds
	private static float delta = 100; 
	//Inter-arrival Time(IAT): in milliseconds
	static final int ArraySize =(int) ((float)MaxExecutionTimeMsec/delta);
	static int tick = 0;
	private static long[] ticksCount = new long[ArraySize];
	private static final int convertMilliToNano = 1000000;
	
	private static String getTime() {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return format.format(cal.getTime());
	}
	/*
	 * Invoke 1 producer vs 1,2,3 consumers
	 * Write consumer to file
	 */
	public static void main(String args[]) {
		ClockGranularity.delta = delta*convertMilliToNano;
		long execStartTime = System.currentTimeMillis();
		long experimentStartTime = System.nanoTime();
		long execDuration, experimentRuntime;
		Buffer requestQueue = new Buffer();
		Thread producerThread = new Thread(new Producer(requestQueue));
		Consumer consumer = new Consumer(requestQueue);
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();
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
			producerThread.run();	
			}
		} while (execDuration <= MaxExecutionTimeMsec);
		try {
			String producerFile = "Producer:" + delta + " msec @" + getTime();
			printToFile(producerFile,requestQueue.getQueue());
			String consumerFile = "Conumer:" + delta + " msec@" + getTime();
			printToFile(consumerFile, consumer.getValidateConsumerArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void printToFile(String outputFileName,Queue<Integer> requestQueue) throws IOException{
		String lineSeparator = System.lineSeparator();
		File directory = new File("C:\\Users\\Sagar\\git\\Brainteasing Code\\Practice Coding\\src\\ClockGranularity Test results\\Semaphore and Queue");
		File file = File.createTempFile(outputFileName, ".txt",directory);
		FileWriter writer = new FileWriter(file);
		writer.append("Index\tTimestamp\tQueue Contents" + lineSeparator);
		String summary = "#tick:" + tick + ", tick[]" + ticksCount.length + ", queue<>" + requestQueue.size();
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
class Buffer {
	private Queue<Integer> requestsQueue;
	Buffer() {
		requestsQueue = new LinkedList<Integer>();
	}
	public synchronized void put(Integer tick) {
		//TODO include a while ?
		requestsQueue.add(tick);
	}
	public synchronized int get() {
		int tick;
		while(requestsQueue.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		tick = requestsQueue.poll();
		return tick;
	}
	public Queue<Integer> getQueue() {
		return requestsQueue;
	}
}

class Consumer implements Runnable{
	private Buffer bufferQueue;
	private Queue<Integer> validateConsumer;
	Consumer(Buffer requestQueue) {
		bufferQueue = requestQueue;
		validateConsumer = new LinkedList<Integer>();
	}
	public void run() {
		validateConsumer.add(bufferQueue.get());
	}
	public Queue<Integer> getValidateConsumerArray() {
		return validateConsumer;
	}
}

class Producer implements Runnable{
	private Buffer bufferQueue;
	Producer(Buffer requestQueue) {
		bufferQueue = requestQueue;	
	}
	public void run() {
		bufferQueue.put(ClockGranularity.tick);
	}
}