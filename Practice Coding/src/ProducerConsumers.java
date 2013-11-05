import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
/*
 * Purpose is to test CPU utilization by threads -> maximize CPU utilization
 * The machine is a quad core processor. The aim of the experiment is:
 * Execute 'x' threads to have  100% CPU utilization (100% on all 4 cores)
 */





import com.sun.scenario.effect.impl.prism.PrCropPeer;

public class ProducerConsumers{
	private static final int MaxExecutionTimeMsec = 60000;
	//60*1000 milliseconds
	private static float delta = 10; 
	//Inter-arrival Time(IAT): in milliseconds
	static final int ArraySize =(int) ((float)MaxExecutionTimeMsec/delta);
	private static final int convertMilliToNano = 1000000;
	
	private static String getTime() {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return format.format(cal.getTime());
	}
	public static void main(String args[]) throws IOException {
		ProducerConsumers.delta = delta*convertMilliToNano;
		long execStartTime = System.currentTimeMillis();
		long experimentStartTime = System.nanoTime();
		long execDuration, experimentRuntime;
		
		int numOfConsumers = 3;
		int numOfProducers = 1; 
		Producer producerThreadArray[] = new Producer[numOfProducers];
		
		for(int j=0;j<numOfProducers;j++) {
			Buffer[] requestQueues = new Buffer[numOfConsumers];
			producerThreadArray[j] = new Producer(requestQueues,numOfConsumers);		
			producerThreadArray[j].setPriority(Thread.MAX_PRIORITY);
		}
		do {
			execDuration = System.currentTimeMillis() - execStartTime;
			experimentRuntime = System.nanoTime() - experimentStartTime;
			if(experimentRuntime >= delta) {
				experimentStartTime = System.nanoTime();
				for(Producer i:producerThreadArray){
					i.run();
				}
			}
		} while (execDuration <= MaxExecutionTimeMsec);
		for(Producer i:producerThreadArray) {
			i.getFakeConsumed();
			try {
				i.joinConsumers();
				i.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		ProducerConsumers.delta/=convertMilliToNano;
	}
	public static void printToFile(String outputFileName,Queue<Integer> requestQueue) throws IOException{
		
		outputFileName += " " + delta + " msec@" + getTime();
		outputFileName = outputFileName.replace(':', '-');
		String lineSeparator = System.lineSeparator();
		File directory = new File("C:\\Users\\Sagar\\git\\Brainteasing Code\\Practice Coding\\src\\ClockGranularity Test results\\Semaphore and Queue\\concurrentlinkedq\\");
		File file = File.createTempFile(outputFileName, ".txt",directory);
		FileWriter writer = new FileWriter(file);
		writer.append("Index \tQueue Contents" + lineSeparator);
		int size = requestQueue.size();
		String summary = outputFileName + ": queue size:" + size + " last element: ";
		String temp="";
		for(int i = 0; i<size; i++) {
			temp = i + " ticks  \t" + requestQueue.poll();
			//System.out.println(temp);
			writer.append(temp + lineSeparator);
		}
		summary += temp;
		writer.append(lineSeparator + "Summary: " + lineSeparator);
		writer.append(summary + lineSeparator);
		System.out.println(summary);
		writer.close();
	}
}
class Buffer {
	public volatile int canRead;
	//static Object syncronizedObject;
	private Queue<Integer> requestsQueue;
	public static int DUMMY = -999;
	Buffer() {
		requestsQueue = new LinkedList<Integer>();
		requestsQueue.add(DUMMY);
		//syncronizedObject = new Object();
		this.canRead = 1;
	}
	public void put(Integer tick) throws InterruptedException {
		requestsQueue.add(tick);
		canRead++;
		/*synchronized(syncronizedObject) {
			syncronizedObject.notify();
		}*/
	}
	public int get() throws InterruptedException {
		int tick = DUMMY;
		while(this.canRead == 0) {
			/*synchronized (syncronizedObject) {
				syncronizedObject.wait();
			}*/
		}
		try{
			tick = requestsQueue.poll();
			this.canRead--;
			
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
		return tick;
	}
	private Queue<Integer> getQueue() {
		return requestsQueue;
	}
}

class Consumer extends Thread{
	private Buffer bufferQueue;
	private Queue<Integer> validateConsumer;
	Consumer(Buffer requestQueue, String name) {
		this.setName(name);
		bufferQueue = requestQueue;
		validateConsumer = new LinkedList<Integer>();
	}
	public void run() {
		while(true) {
			int i;
			//if(ProducerConsumers.canRead > 0) {
				try {
					i = bufferQueue.get();
					validateConsumer.add(i);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			//}
		}
	}
	Queue<Integer> getValidateConsumerArray() {
		return validateConsumer;
	}
	int getNumberOfFakeConsumed() {
		int fake = 0;
		for(int i:this.validateConsumer){
			if(i == Buffer.DUMMY) {
				fake++;
			}
		}
		return fake;
	}
}

class Producer extends Thread{
	public int tick = 0;
	private Buffer[] bufferQueue;
	private Consumer consumerThreadArray[];
	Producer(Buffer[] requestQueue, int numOfConsumers) {
		this.setName("Producer " + this.getName());
		bufferQueue = requestQueue;	
		/*Initialize consumers for each producerTdread*/
		 consumerThreadArray = new Consumer[numOfConsumers];
		for(int i=0; i<numOfConsumers; i++) {
			bufferQueue[i] = new Buffer();
			consumerThreadArray[i]= new Consumer(bufferQueue[i], "Consumer " + i);
		}
		for(Consumer i:consumerThreadArray) {
			i.start();
		}
	}
	public void joinConsumers() throws InterruptedException {
		for(Consumer i:consumerThreadArray) {
			i.join();			
		}
	}
	public void getFakeConsumed() throws IOException{
		for(Consumer i:consumerThreadArray) {
			int fake = i.getNumberOfFakeConsumed()-1;
			System.out.println("Fake consumed" + this.getName() + "->" + i.getName() + " " + fake);
			String consumerFile = i.getName();
			ProducerConsumers.printToFile(consumerFile, i.getValidateConsumerArray());
		}
	}
	public void run() {
		try {
				bufferQueue[tick%bufferQueue.length].put(tick++);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}