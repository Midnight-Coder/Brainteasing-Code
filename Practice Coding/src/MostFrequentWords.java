/*
 * Write a function that takes two parameters: 
 * 1) a String representing a text document and 2) an integer providing the number of items to return. 
 * Implement the function such that it returns a list of Strings ordered by word frequency, the most frequently occurring word first. 
 * Use your best judgment to decide how words are separated. 
 * Your solution should run in O(n) time where n is the number of characters in the document. 
 * Implement this function as you would for a production/commercial system
 */
import com.google.common.collect.HashBiMap;

import java.util.Scanner;

/*
 * Approaches:
 * 1. O(n) : Use a map to store word - frequency. 
 * 		a. O(n) time + space to build hashmap and b. O(n) time to find. Insertion/Deletion : O(1)
 * 2. O(nlogn) : Sort and count (O(n)). Space O(1). Insertion/Deletion : O(n)
 * 3. List <Object> : Store String followed by freq. 
 * 		Building(from a Map) = O(n) time but space = 2n O(n). Find O(1). Insertion/Deletion : O(n). 
 * 4. BiMap : Guava library Insertion/Deletion : O(1)
 */
public class MostFrequentWords {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Input the string");
		String hugeString = sc.nextLine();
		System.out.println("Input the integer providing number of items to return");
		int n = sc.nextInt();
		sc.close();
		setupFrequency(hugeString,n);
	}

	private static void setupFrequency(String hugeString, int n) {
		HashBiMap<String,Integer> WordFreq = HashBiMap.create();
		char delimiter = ' ';
		for(int i=0; i<hugeString.length(); i++) {
			StringBuilder word = new StringBuilder("");
			int freq = 1;
			char c = hugeString.charAt(i);
				while(c!=delimiter) {
					word.append(c);
					i++;
					c=hugeString.charAt(i);
				}
				if(WordFreq.containsKey(word)) {
					freq = WordFreq.get(word);
			}
			WordFreq.forcePut(word.toString(), freq);
		}
		//findNFrequent(WordFreq,n);
	}
}
