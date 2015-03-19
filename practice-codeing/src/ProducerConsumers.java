import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

/*
 * Purpose is to test CPU utilization by threads -> maximize CPU utilization
 * The machine is a quad core processor. The aim of the experiment is:
 * Execute 'x' threads to have  100% CPU utilization (100% on all 4 cores)
 */
class TickObject {
	int tick;
	long requestTime;
	TickObject(int tick, long startTime) {
		this.tick = tick;
		this.requestTime = startTime;
	}
	@Override
	public String toString(){
		return tick + " valid @" + requestTime;
	}
}
public class ProducerConsumers{
	private static final int MaxExecutionTimeMsec = 60000;
	//60*1000 milliseconds
	private static float delta = 10; 
	//Inter-arrival Time(IAT): in milliseconds
	static final int ArraySize =(int) ((float)MaxExecutionTimeMsec/delta);
	public static int batchSize = 1000;
	public static long execStartTime;
	private static final int convertMilliToNano = 1000000;
	
	private static String getTime() {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return format.format(cal.getTime());
	}
	public static void main(String args[]) throws IOException {
		ProducerConsumers.delta = delta*convertMilliToNano;
		execStartTime = System.currentTimeMillis();
		long experimentStartTime = System.nanoTime();
		long execDuration, experimentRuntime;
		
		int numOfConsumers = 1;
		int numOfProducers = 1; 
		Producer producerThreadArray[] = new Producer[numOfProducers];
		
		for(int j=0;j<numOfProducers;j++) {
			producerThreadArray[j] = new Producer(numOfConsumers);		
			producerThreadArray[j].setPriority(Thread.MAX_PRIORITY);
		}
		for(Producer j:producerThreadArray) {
			j.startConsumers();
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
		ProducerConsumers.delta = delta/convertMilliToNano;
		for(Producer i:producerThreadArray) {
			i.cleanUp();
			try {
				i.joinConsumers();
				i.interrupt();
				i.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void printToFile(String outputFileName,Queue<TickObject> requestQueue) throws IOException{
		
		outputFileName += " " + delta + " msec@" + getTime();
		outputFileName = outputFileName.replace(':', '-');
		String lineSeparator = System.lineSeparator();
		File directory = new File("C:\\Users\\Sagar\\git\\Brainteasing Code\\Practice Coding\\src\\ClockGranularity Test results\\Semaphore and Queue\\concurrentlinkedq\\");
		File file = File.createTempFile(outputFileName, ".txt",directory);
		FileWriter writer = new FileWriter(file);
		writer.append("Index \tQueue Contents" + lineSeparator);
		int size = requestQueue.size();
		String summary = outputFileName + ": queue size:" + size + " last element: ";
		writer.append(summary + lineSeparator);
		String temp="";
		for(int i = 0; i<size; i++) {
			temp = i + "th tick  \t" + requestQueue.poll();
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
	private Queue<TickObject> requestsQueue;
	public static TickObject DUMMY = new TickObject(-999, -1);
	Buffer() {
		requestsQueue = new LinkedList<TickObject>();
		requestsQueue.add(DUMMY);
		this.canRead = 1;
	}
	public void put(TickObject tick) throws InterruptedException {
		requestsQueue.add(tick);
		canRead++;
	}
	public TickObject get() throws InterruptedException {
		TickObject tickObject = DUMMY;
		while(this.canRead == 0) {
		}
		try{
			tickObject = requestsQueue.poll();
			this.canRead--;
		}catch (NullPointerException e) {
			//TODO store e in an array and print later. Insert dummy instead
			e.printStackTrace();
		}
		return tickObject;
	}
	public Queue<TickObject> getBatch() throws InterruptedException {
		Queue<TickObject> q = new LinkedList<TickObject>();
		TickObject tickObject = DUMMY;
		for(int i=0; i<ProducerConsumers.batchSize; i++)
		try{
			tickObject = requestsQueue.poll();
			q.add(tickObject);
		}catch (NullPointerException e) {
			return q;
		}
		return q;
	}
	public Queue<TickObject> getQueue() {
		return requestsQueue;
	}
}

class Consumer extends Thread{
	private Buffer bufferQueue;
	private Queue<TickObject> itemsReadValidation, ticksToBeProcessed;
	Consumer(Buffer requestQueue, String name) {
		this.setName(name);
		bufferQueue = requestQueue;
		itemsReadValidation = new LinkedList<TickObject>();
		ticksToBeProcessed = new LinkedList<TickObject>();
	}
	public void run() {
		while(true) {
				try {
					ticksToBeProcessed.addAll(bufferQueue.getBatch());
					processReads();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}
	private void processReads() {
		TickObject o;
		while(!ticksToBeProcessed.isEmpty()) {
			o = ticksToBeProcessed.peek();
			while(o.requestTime > System.currentTimeMillis()) {
				//Wait indefinitely until it is time to process this and subsequent ticks
			}
			o = ticksToBeProcessed.poll();
			itemsReadValidation.add(o);
		}
	}
	Queue<TickObject> getValidateConsumerArray() {
		return itemsReadValidation;
	}
	int getNumberOfFakeConsumed() {
		int fake = 0;
		for(TickObject i:this.itemsReadValidation){
			if(i == Buffer.DUMMY) {
				fake++;
			}
		}
		return fake;
	}
}

class Producer extends Thread{
	private int tick;
	private Buffer[] bufferQueue;
	private Consumer consumerThreadArray[];
	private static int batchSize = ProducerConsumers.batchSize;

	Producer(int numOfConsumers) {
		this.setName("Producer " + this.getName());
		/*Initialize 1 buffer/consumer*/
		bufferQueue = new Buffer[numOfConsumers];
		/*Initialize consumers for each producerThread*/
		 consumerThreadArray = new Consumer[numOfConsumers];
		for(int i=0; i<numOfConsumers; i++) {
			bufferQueue[i] = new Buffer();
			consumerThreadArray[i]= new Consumer(bufferQueue[i], "Consumer " + i);
		}
		try {
			this.insertBatch();
		} catch (InterruptedException e) {
			System.err.print("This should nver happen ");e.printStackTrace();
		}
	}
	public void startConsumers() {
		for(Consumer i:consumerThreadArray) {
			i.start();
		}
	}
	public void joinConsumers() throws InterruptedException {
		for(Consumer i:consumerThreadArray) {
			i.interrupt();
			//i.join();			
		}
	}
	public void cleanUp() throws IOException{
		int j = 0;
		for(Consumer i:consumerThreadArray) {
			//int fake = i.getNumberOfFakeConsumed()-1;
			//System.out.println("Fake consumed" + this.getName() + "->" + i.getName() + " " + fake);
			String consumerFile = i.getName();
			ProducerConsumers.printToFile(consumerFile, i.getValidateConsumerArray());
			ProducerConsumers.printToFile(this.getName(), bufferQueue[j].getQueue());
			j++;
		}
	}
	public void run() {
		try {
			insertBatch();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	void insertBatch() throws InterruptedException {
		TickObject o;
		for(int i=0;i<batchSize;i++) {
			o = new TickObject(tick, ProducerConsumers.execStartTime + tick);
			tick++;
			bufferQueue[0].put(o);
		}
	}
}