import java.util.Arrays;
import java.util.Scanner;
/**
 * Using the same digits find a number such that the newNmuber is the smallest newNumber > originalNumber
 * eg. 4125 -> 4152 -> 4215 -> 4251...
 */
public class NextGreatestNumber {
//TODO optimize and test more
	/*
	 * Approach: 
	 * Check if desc. sorted digits? -yes return the same number and err
	 * Start from leftmost(i) index and look for the first digit > currentDigit (important to note that least increment implies least place value position)
	 *		1. If no digit found, repeat with i-- 
	 *		2. if found swap i,j and asc sort on all digits on right of j (j+1 to string.length)
	 * *Detailed documentation in the function
	 *Assumptions: Whole numbers only (no fractions or negatives)
	 */
	public static void main(String aargs[]){
		String number;
		Scanner sc = new Scanner(System.in);
		System.out.println("ENTER number");
		number = sc.next().trim();
		//number = "43529";
		while(number!=null) {
			sc.next();
			number = findSmallestIncrement(number);
			System.out.println(number);
		}
		sc.close();
	}
	
	/**
	 * Returns the next smallest number > input number
	 * @param String number
	 * @return a String number as the solution. Null if invalid input or further processing not mathematically possible
	 */
	private static String findSmallestIncrement(String number) {
		if(!inputSanityCheck(number)) {
			return null;
		}
		
		int length = number.length();
		char[] numberArray = number.toCharArray();
		System.out.println("Processing:" + number);
		if(isGreatest(numberArray)) {	//Time complexity: O(n)
			System.out.println("Not possible to generate a bigger number with these digits:" + number);
			return null;
		}
		/**	Have two pointers: windowUpperBound, windowLowerBound
		 *  Initialize the lower bound to length-1 index always
		 *  Check for a possibility of swapping numbers within the window 
		 *  If not possible then increase the window size iteratively (recursion avoided) => Time complexity: O(n2) 
		 *  After swapping, sort the elements on the right of the windowUpperBound in ascending order => Time complexity: O(nlogn)
		 *  Approach is upper bounded to O(n3logn)
		 */
		for(int windowUpperBound=length-2; windowUpperBound>=0; windowUpperBound--) {
			char currentDigit = numberArray[windowUpperBound];
			for(int windowLowerBound=length-1; windowLowerBound > windowUpperBound; windowLowerBound--){
				if(numberArray[windowLowerBound]>currentDigit){
					swap(windowUpperBound,windowLowerBound,numberArray);
					ascendingSort(windowUpperBound+1,numberArray);
					number = new String(numberArray);
					return number;
				}
			}
		}
		return number;
	}
	/**
	 * Check for negative and floating point numbers
	 * @param String input number
	 * @return True if all sanity checks were passed, else false
	 */
	private static boolean inputSanityCheck(String number) {
		String featureNotSupportedErrorMessage = "This version of the program supports only Natural numbers, ie. integers that are >= 1";
		try {
			Integer i = Integer.parseInt(number);
			if(i<=0) {
				System.err.println(featureNotSupportedErrorMessage);
				return false;
			}
		} catch(NumberFormatException ex) { 
			System.err.println(featureNotSupportedErrorMessage);
			return false;
		}
		return true;
	}
	/**
	 * Sort sub-array: chars from index j to numberArray.length in ascending order
	 * @param Start index of sub array (inclusive) and the array
	 */
	private static void ascendingSort(int startIndex, char[] numberArray) {
		// TODO optimize!
		if(startIndex == numberArray.length-1) {
			return;
		}
		int j = startIndex;
		char[] subArray = new char[numberArray.length - j];
		for(int i=0; i<subArray.length; i++) {
			subArray[i] = numberArray[j];
			j++;
		}
		Arrays.sort(subArray);
		j = startIndex;
		for(int i=0; i<subArray.length; i++) {
			numberArray[j] = subArray[i];
			j++;
		}
	}
	/**
	 * swap chars at pos i,j in numberArray. Consider objects.
	 * @param swaps elements at position i and j in the given array
	 */
	private static void swap(int i, int j, char[] numberArray) {
		char temp = numberArray[i];
		numberArray[i] = numberArray[j];
		numberArray[j] = temp;
	}
	/**
	 * Check if the number is the greatest possible permutation of the digits
	 * @param char array of input numbers
	 * @return true if further processing not possible, else false
	 */
	private static boolean isGreatest(char[] numberArray) {
		char max = numberArray[0];
		for(char i:numberArray) {
			if(i>max) {
				return false;
			}
			max = i;
		}
		return true;
	}
}