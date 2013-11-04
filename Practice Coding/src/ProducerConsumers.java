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
 * Purpose is to test CPU utilization by threads -> maximize CPU utilization
 * The machine is a quad core processor. The aim of the experiment is:
 * Execute 'x' threads to have  100% CPU utilization (100% on all 4 cores)
 */

public class ProducerConsumers{
	private static final int MaxExecutionTimeMsec = 60000;
	//60*1000 milliseconds
	private static float delta = 1; 
	//Inter-arrival Time(IAT): in milliseconds
	static final int ArraySize =(int) ((float)MaxExecutionTimeMsec/delta);
	private static final int convertMilliToNano = 1000000;
	public volatile static boolean canRead = false;
	
	private static String getTime() {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return format.format(cal.getTime());
	}
	/*
	 * Invoke 1 producer vs 1,2,3 consumers
	 * Write consumer to file
	 */
	public static void main(String args[]) throws IOException {
		ProducerConsumers.delta = delta*convertMilliToNano;
		long execStartTime = System.currentTimeMillis();
		long experimentStartTime = System.nanoTime();
		long execDuration, experimentRuntime;
		
		int numOfConsumers = 3;
		//TODO int numOfProducers = 2; 
		Buffer[] requestQueues = new Buffer[numOfConsumers];
		
		Producer producer = new Producer(requestQueues);
		producer.setPriority(Thread.MAX_PRIORITY);
		Consumer consumerThreadArray[] = new Consumer[numOfConsumers];
		for(int i=0; i<numOfConsumers; i++) {
			requestQueues[i] = new Buffer();
			consumerThreadArray[i]= new Consumer(requestQueues[i]);
		}
		for(int i=0; i<numOfConsumers; i++) {
			consumerThreadArray[i].start();
		}
		do {
			execDuration = System.currentTimeMillis() - execStartTime;
			experimentRuntime = System.nanoTime() - experimentStartTime;
			if(experimentRuntime >= delta) {
				experimentStartTime = System.nanoTime();
			producer.run();
			}
		} while (execDuration <= MaxExecutionTimeMsec);
		for(int i=0; i<numOfConsumers; i++) {
			consumerThreadArray[i].interrupt();
			//String consumerFile = "Consumer-" + i + delta + " msec@" + getTime();
			//printToFile(consumerFile, consumerThreadArray[i].getValidateConsumerArray());
		}
		for(int i=0; i<numOfConsumers; i++) {
			int fake = consumerThreadArray[i].getNumberOfFakeConsumed();
			System.out.println(i + " " + fake);
		}
		delta/=convertMilliToNano;
		
		/*try {
			String producerFile = "Producer-" + delta + " msec @" + getTime();
			//printToFile(producerFile,requestQueues.getQueue());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	public static void printToFile(String outputFileName,Queue<Integer> requestQueue) throws IOException{
		outputFileName = outputFileName.replace(':', '-');
		String lineSeparator = System.lineSeparator();
		File directory = new File("C:\\Users\\Sagar\\git\\Brainteasing Code\\Practice Coding\\src\\ClockGranularity Test results\\Semaphore and Queue\\");
		File file = File.createTempFile(outputFileName, ".txt",directory);
		FileWriter writer = new FileWriter(file);
		writer.append("Index \tQueue Contents" + lineSeparator);
		int size = requestQueue.size();
		String summary = "queue<>" + size;
		for(int i = 0; i<size; i++) {
			String temp = i + " ticks  \t" + requestQueue.poll();
			System.out.println(temp);
			writer.append(temp + lineSeparator);
		}
		writer.append(lineSeparator + "Summary: " + lineSeparator);
		writer.append(summary + lineSeparator);
		System.out.println(outputFileName + " " + summary);
		writer.close();
	}
}
class Buffer {
//	static Object syncronizedObject;
	private Queue<Integer> requestsQueue;
	Semaphore accessQueue;
	Buffer() {
		requestsQueue = new LinkedList<Integer>();
		requestsQueue.add(-999);
		accessQueue = new Semaphore(1);
	//	syncronizedObject = new Object();
	}
	public void put(Integer tick) throws InterruptedException {
//		accessQueue.acquire();
		requestsQueue.add(tick);
	//	synchronized(syncronizedObject) {
		//	syncronizedObject.notify();
	//	}
		//accessQueue.release();
	}
	public int get() throws InterruptedException {
		int tick;
		while(!ProducerConsumers.canRead) {
		
			/*	try {
				synchronized (syncronizedObject) {
					syncronizedObject.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
		try{
		//accessQueue.acquire();
		tick = requestsQueue.poll();
		//accessQueue.release();
		return tick;
		}
		catch (NullPointerException e) {
			return -998;
		}
		
	}
	public Queue<Integer> getQueue() {
		return requestsQueue;
	}
}

class Consumer extends Thread{
	private Buffer bufferQueue;
	private Queue<Integer> validateConsumer;
	Consumer(Buffer requestQueue) {
		bufferQueue = requestQueue;
		validateConsumer = new LinkedList<Integer>();
	}
	public void run() {
		while(true) {
			int i;
			if(ProducerConsumers.canRead) {
				try {
					i = bufferQueue.get();
					validateConsumer.add(i);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public Queue<Integer> getValidateConsumerArray() {
		return validateConsumer;
	}
	public int getNumberOfFakeConsumed() {
		int fake = 0;
		for(int i:this.validateConsumer){
			if(i == -998) {
				fake++;
			}
		}
		return fake;
	}
}

class Producer extends Thread{
	public int tick = 0;
	private Buffer[] bufferQueue;
	Producer(Buffer[] requestQueue) {
		bufferQueue = requestQueue;	
	}
	public void run() {
		try {
				bufferQueue[tick%bufferQueue.length].put(tick++);
				ProducerConsumers.canRead = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}