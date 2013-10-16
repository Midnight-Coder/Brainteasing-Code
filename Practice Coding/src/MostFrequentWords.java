/*
 * Write a function that takes two parameters: 
 * 1) a String representing a text document and 2) an integer providing the number of items to return. 
 * Implement the function such that it returns a list of Strings ordered by word frequency, the most frequently occurring word first. 
 * Use your best judgment to decide how words are separated. 
 * Your solution should run in O(n) time where n is the number of characters in the document. 
 * Implement this function as you would for a production/commercial system
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
 * Since the string represents a single doc - it is feasible to go through all the words. Ruling out sampling.
 * Approaches:
 * 1. O(n)(time and space) : Use a map to store word - frequency. 
		Search:O(n). Insertion/Deletion : O(1)
 * 2. O(mlogn) : (Built from map)Sort (m unique words), Search O(1). 
 * 		Space O(1). Insertion/Deletion : O(n)
 * 4. BiMap : Guava library. Everything : O(1) => is not a standard lib!
 */
/*
 * My approach:
 * Build hashmap with each word's frequency O(n)
 * Read the hashmap and store in a List in order of decreasing frequency
 * 
 * The problems of insertion and deletion can be ignored - assumption: the document is static.
 * The use case then would be accessing the most/least frequent terms => not even their frequencies. 
 * 
 * Assumptions: 
 * The text follows the rules of grammar ie. there is a space between each word. 
 * Punctuation is ignored for the sake of simplicity.  
 */

class WordDetails implements Comparator<WordDetails>{
	private int frequency;
	private String word;
	WordDetails(String w) {
		word = w;
		frequency=1;
	}
	public void incrementFreq() {
		frequency++;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public String getWord() {
		return word;
	}
	@Override
	public int compare(WordDetails arg0, WordDetails arg1) {
		if(arg0.getFrequency() == arg1.getFrequency()) {
			return 0;
		}
		return (arg0.getFrequency()>arg1.getFrequency()?-1:1);
	}
}
public class MostFrequentWords {

	public static void main(String args[]) {	
		Scanner sc = new Scanner(System.in);
		System.out.println("Input the string");
		String hugeString = "ab abcd ab def ab def";//sc.nextLine();
		System.out.println("Input the integer providing number of items to return");
		int n = 2;//sc.nextInt();
		sc.close();
		setupFrequency(hugeString,n);
	}

	private static void setupFrequency(String hugeString, int n) {	
		Map<String, WordDetails> wordFreq = new HashMap<String,WordDetails>();
		char delimiter = ' ';
		for(int i=0; i<hugeString.length(); i++) {
			StringBuilder word = new StringBuilder("");
			WordDetails freq;
			char c = hugeString.charAt(i);
			while(c!=delimiter) {
				word.append(c);
				i++;
				if(i==hugeString.length()) {
					break;
				}
				c = hugeString.charAt(i);
			}
			String wordString = word.toString();
			if(wordFreq.containsKey(wordString)) {
				freq = wordFreq.get(wordString);
				freq.incrementFreq();
			}
			else {
				freq = new WordDetails(wordString);
			}
			wordFreq.put(wordString, freq);
		}
		findNFrequent(wordFreq,n);
	}

	private static void findNFrequent(Map<String, WordDetails> wordFreq, int n) {
		List<WordDetails> frequentWords = new ArrayList<WordDetails>();
		//Adding all entries to the list
		for(Map.Entry<String, WordDetails> e : wordFreq.entrySet()) {
				frequentWords.add(e.getValue());
		}
		//Sorting all for fututre use
		Collections.sort(frequentWords,new WordDetails(null));
		//Print top n
		System.out.println("Word" + "\tFrequency");
		System.out.println("---------------------");
		for(WordDetails e:frequentWords) {
			System.out.println(e.getWord() + "\t" + e.getFrequency());
			n--;
			if(n==0) {
				break;
			}
		}
	}
}