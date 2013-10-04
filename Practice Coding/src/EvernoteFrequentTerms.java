import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


public class EvernoteFrequentTerms {
	/*
	 * Given N terms, your task is to find the k most frequent terms from given N terms
	 */
	public static void main(String args[]) {
		init();
	}
	private static void init() {
		Scanner sc = new Scanner(System.in);
		int n = Integer.parseInt(sc.nextLine());
		Map<String,Integer> input = new HashMap<String,Integer>();
		FrequencyComparator cmp = new FrequencyComparator(input);
		Map<String, Integer> sortedMayByFrequency = new TreeMap<String,Integer>(cmp);
		for(int i=0; i<n; i++) {
			String s = sc.nextLine();
			if(input.containsKey(s)) {
				Integer increaseFreq = input.get(s) + 1;
				input.put(s, increaseFreq);
			}
			else {
				input.put(s, 1);
			}
		}
		sortedMayByFrequency.putAll(input);
		int k = Integer.parseInt(sc.nextLine());
		sc.close();
		for(Map.Entry<String, Integer> e:sortedMayByFrequency.entrySet()) {
			if(k<=0) {
				break;
			}
			k--;
			System.out.println(e.getKey() + " " + e.getValue());
		}
	}
}
class FrequencyComparator implements Comparator<String>{
	Map<String,Integer> map;
	public FrequencyComparator(Map<String,Integer> input) {
		map = input;
	}
	@Override
    public int compare(String a, String b) {
		if(map.get(a)<map.get(b)) {
			return 1;
		}
		else if(map.get(a) == map.get(b)) {
			if(a.compareTo(b)< 0) {
				return -1;
			}
			else {
				return 1;
			}
		}
		else {
			return -1;
		}
	}
}