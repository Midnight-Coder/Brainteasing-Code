import java.util.Arrays;
import java.util.Scanner;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/*
 * Using the same digits find a number such that the newNmuber is the smallest newNumber > originalNumber
 * eg. 4125 -> 4152 -> 4215 -> 4251...
 */
public class NextGreatestNumber {
//TODO optimize and test more
	/*
	 * Approach: 
	 * Check if desc. sorteed digits? -yes return the same number
	 * Start from leftmost(i) index and look for the first digit > currentDigit (important to note that least increment implies least place value position)
	 *		1. If no digit found, repeat with i-- 
	 *		2. if found swap i,j and asc sort on all digits on right of j (j+1 to string.length)
	 *
	 *Assumptions: Whole numbers. No duplicates
	 */
	public static void main(String aargs[]){
		String number;
		Scanner sc = new Scanner(System.in);
		/*System.out.println("ENTER number");
		number = sc.next().trim();*/
//		sc.close();
		number = "43529";
		while(number!=null) {
			//System.out.println(number);
			number = findSmallestIncrement(number);
		}
	}
	private static String findSmallestIncrement(String number) {
		int length = number.length();
		char[] numberArray = number.toCharArray();
		System.out.println("Processing:" + number);
		if(isGreatest(numberArray)) {
			System.out.println("Not possible to generate a bigger number with these digits:" + number);
			return null;
		}
		//2 loops i,j  1 swap,1 move if no swap
		for(int i=length-1; i>=0; i--) {
			char currentDigit = numberArray[i];
			boolean breakOuter = false;
			for(int j=i-1; j>=0; j--){
				if(numberArray[j]<currentDigit){
					swap(i,j,numberArray);
					ascendingSort(j+1,numberArray);
					System.out.println(numberArray);
					number = new String(numberArray);
					breakOuter = true;
					break;
				}
			}
			if(breakOuter) {
				breakOuter = false;
				break;
			}
		}
		return number;
	}
	/*
	 * sort chars from index j to numberArray.length in ascending order
	 */
	private static void ascendingSort(int startIndex, char[] numberArray) {
		// TODO optimize!
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
	/*
	 *swap chars at pos i,j in numberArray. Consider objects.
	 */
	private static void swap(int i, int j, char[] numberArray) {
		char temp = numberArray[i];
		numberArray[i] = numberArray[j];
		numberArray[j] = temp;
	}
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
