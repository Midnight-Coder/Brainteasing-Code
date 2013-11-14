import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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
public class ProducerConsumersController{
	
	public static final int convertMilliToNano = 1000000;
	public static final int executionTimeInMilli = 60000;
	public static final float interArrivalTimeInMilli = 0.01f;
	public static final long interArrivalTimeInNano = (long) (interArrivalTimeInMilli*convertMilliToNano);
	public static final int ArraySize =(int) ((float)executionTimeInMilli/interArrivalTimeInMilli);
	public static int batchSize = 1000;
	public static long executionStartTimeInMIlli;
	
	
	/*
	 * Gets time in the format: dd-MMM-yy HH:mm:ss
	 */
	public static String getTime() {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return format.format(cal.getTime());
	}

	public static void main(String args[]) throws IOException {
		
		int numOfConsumers = 1;
		int numOfProducers = 1; 
		int numOfBuffers = numOfProducers;
		Producer producerThreadArray[] = new Producer[numOfProducers];
		Consumer consumerThreadArray[] = new Consumer[numOfConsumers];
		Buffer sharedBuffers[] = new Buffer[numOfBuffers];
		System.out.println("#Producers:" + numOfProducers + " #Consumers:" + numOfConsumers + " #Buffers:" + numOfBuffers);
		for(int j=0; j<numOfBuffers; j++) {
			sharedBuffers[j] = new Buffer();
		}
		for(int j=0; j<numOfProducers; j++) {
			//Initialize each producer. The initialization also inserts $batchSize objects in the shared buffer
			producerThreadArray[j] = new Producer(sharedBuffers[j%numOfBuffers]);		
			producerThreadArray[j].setPriority(Thread.MAX_PRIORITY);
		}
		for(int j=0; j<numOfConsumers; j++) {
			consumerThreadArray[j] = new Consumer(sharedBuffers[j%numOfBuffers], "Consumer " + j);
		}
		for(Consumer i:consumerThreadArray) {
			i.start();
		}
		
		executionStartTimeInMIlli = System.currentTimeMillis();
		long lastInsertTimeInNano = System.nanoTime();
		long executionDurationInMilli = 0;
		long timeSinceLastInsertInNano;
		
		while (executionDurationInMilli <= executionTimeInMilli){
			timeSinceLastInsertInNano = System.nanoTime() - lastInsertTimeInNano;
			if(timeSinceLastInsertInNano >= interArrivalTimeInNano) {
				lastInsertTimeInNano = System.nanoTime();
				for(Producer i:producerThreadArray){
					i.run();
				}
			}
			executionDurationInMilli = System.currentTimeMillis() - executionStartTimeInMIlli;
		}
		for(Consumer i:consumerThreadArray) {
			i.getQueueStats();
		}
		
		for(Buffer i:sharedBuffers){
			i.isProducing = Boolean.FALSE;
		}
		stopThreadExecution(consumerThreadArray);
		try{
			System.out.println("Stopped consumers");
			Thread.sleep(10000);
		}catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		stopThreadExecution(producerThreadArray);
	}
	private static void stopThreadExecution(Thread array[]) {
		for(Thread i:array) {
			try {
				i.interrupt();
				i.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void printToFile(String outputFileName,Queue<TickObject> requestQueue) throws IOException{
		
		outputFileName += " " + interArrivalTimeInMilli + " msec@" + getTime();
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
	private BlockingQueue<TickObject> requestsQueue;
	public Boolean isProducing;
	public static TickObject DUMMY = new TickObject(-999, -1);
	Buffer() {
		requestsQueue = new ArrayBlockingQueue<TickObject>(ProducerConsumersController.batchSize*3);
		isProducing = Boolean.TRUE;
	}
	public void put(TickObject tick) throws InterruptedException {
		requestsQueue.add(tick);
	}
	public TickObject get() throws InterruptedException {
		TickObject tickObject = DUMMY;
		try{
			tickObject = requestsQueue.take();
		}catch (NullPointerException e) {
			//TODO store e in an array and print later. Insert dummy instead
			e.printStackTrace();
		}
		return tickObject;
	}
	
	public Queue<TickObject> getBatch() throws InterruptedException {
		//Gets a batch of requests from the shared Buffer and moves it to a processing queue - freeing space for new requests in the shared Buffer
		Queue<TickObject> q = new LinkedList<TickObject>();
		TickObject tickObject;
		int currentBatch = Math.min(ProducerConsumersController.batchSize, this.requestsQueue.size());
		//TODO Design issue: return if size < batchSize? 
		for(int i=0; i<currentBatch; i++)
		try{
			tickObject = this.requestsQueue.take();
			q.add(tickObject);
		}catch (NullPointerException e) {
			return q;
		}
		return q;
	}
	/*Returns shared buffer*/
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
		while(bufferQueue.isProducing) {
				try {
					//TODO addAll??
					ticksToBeProcessed.addAll(bufferQueue.getBatch());
					processReads();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}
	private void processReads() {
		TickObject o = ticksToBeProcessed.poll();
		while(o!=null) {
			while(o.requestTime > System.currentTimeMillis()) {
				//Wait indefinitely until it is time to process this and subsequent ticks
			}
			itemsReadValidation.add(o);
			o = ticksToBeProcessed.poll();
		}
	}
	
	Queue<TickObject> getValidateConsumerQueue() {
		return itemsReadValidation;
	}
	
	void getQueueStats() {
		System.out.println("Size of various queues processed by " + this.getName() + ":");
		System.out.println("Shared Buffer:" + bufferQueue.getQueue().size());
		System.out.println("Ticks Validated:" + itemsReadValidation.size());
		System.out.println("Ticks To Be Processed:" + ticksToBeProcessed.size());
	}
}

class Producer extends Thread{
	private int tick;
	private Buffer bufferQueue;
	private static int batchSize = ProducerConsumersController.batchSize;

	Producer(Buffer sharedBuffer) {
		this.setName("Producer " + this.getName());
		bufferQueue = sharedBuffer;
		try {
			this.insertBatch();
		} catch (InterruptedException e) {
			System.err.print("This should nver happen ");
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			TickObject o = new TickObject(tick++, System.currentTimeMillis());
			this.bufferQueue.put(o);//insertBatch();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	void insertBatch() throws InterruptedException {
		TickObject o;
		for(int i=0; i<batchSize; i++) {
			o = new TickObject(tick, ProducerConsumersController.executionStartTimeInMIlli + tick);
			tick++;
			this.bufferQueue.put(o);
		}
	}
}