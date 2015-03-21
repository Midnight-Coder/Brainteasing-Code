package AMZNChallenge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Given two lists of integers, write a function that returns a list that contains only the intersection
 * (elements that occur in both lists) of the two lists. 
 * The returned list should only contain unique integers, no duplicates.
 */

public class SetIntersection {

	public static List<Integer> intersection1 (List<Integer> a, List<Integer> b) {
		Set<Integer> s1 = new HashSet<Integer>(a);
		s1.retainAll(b);
		List<Integer> answer = new ArrayList<Integer>(s1);
		return answer;
	}
	public static List<Integer> intersection2 (List<Integer> a, List<Integer> b) {
		Set<Integer> answer = new HashSet<Integer>();
		Set<Integer> aSet = new HashSet<Integer>(a);
		Set<Integer> bSet = new HashSet<Integer>(b);	//To ensure no duplicates in b 
		for(Integer i:bSet) {
			if(!aSet.add(i)){
				answer.add(i);
			}
		}
		return new ArrayList<Integer>(answer);
	}
	public static void main(String args[]){
		Integer a[] = {1,2,3,4,5};
		Integer b[] = {2,4,5,6,7,8};
		intersection1(Arrays.asList(a), Arrays.asList(b));
	}
}
