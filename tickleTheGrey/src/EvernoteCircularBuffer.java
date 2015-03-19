import java.util.ArrayList;
import java.util.Scanner;

public class EvernoteCircularBuffer {

	public final static char APPEND = 'A';
	public final static char REMOVE = 'R';
	public final static char LIST = 'L';
	private static ArrayList<String> circularBuffer;
	private static int n;
	private static Scanner sc;

	private static void init() {
		sc = new Scanner(System.in);
		n = Integer.parseInt(sc.nextLine());
		circularBuffer = new ArrayList<String>(n);
		String input = sc.nextLine();
		while (!input.equalsIgnoreCase("Q")) {
			processCommand(input,sc);
			input = sc.nextLine();
			debugg(input);
		}
		sc.close();
	}

	private static void debugg(String n) {
		// System.out.println("Debug " + n);
	}

	private static void processCommand(String input,Scanner sc) {
		char command = input.charAt(0);
		if (command == LIST) {
			list();
		}
		else if (command == 'Q') {
			System.exit(0);
		}
		else {
			int numOfElements = Integer.parseInt(input.substring(2));
			if (command == APPEND) {
				String[] elements = new String[numOfElements];
				for(int i=0; i<numOfElements; i++) {
					elements[i] = sc.nextLine();
				}
				appendToBuffer(elements);
			} else if (command == REMOVE) {
				removeFromBuffer(numOfElements);
			}
		}
	}

	private static void list() {
		for (String s : circularBuffer) {
			System.out.println(s);
		}
		return;
	}

	private static void removeFromBuffer(int numOfElements) {
		if (numOfElements > circularBuffer.size()) {
			System.err.println("Cannot remove more than the elements present.");
			System.exit(0);
		}
		while (numOfElements > 0) {
			circularBuffer.remove(0);
			numOfElements--;
		}
	}

	private static void appendToBuffer(String[] elements) {
		if (n < elements.length) {
			System.err.println("Cannot insert more elements than the size of buffer");
			System.exit(0);
		}
		int newBufferSize = circularBuffer.size() + elements.length;
		if (newBufferSize > n) {
			removeFromBuffer(newBufferSize-n);
		}
		for(String s:elements){
			circularBuffer.add(s);
		}
	}

	public static void main(String args[]) {
		init();
	}
}