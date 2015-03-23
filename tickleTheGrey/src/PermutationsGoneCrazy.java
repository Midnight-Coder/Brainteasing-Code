import java.util.Arrays;

/*
 * Generate all possible permutations for a given string
 * Including upper and lower cases
 * Eg. ab ==> Ab, aB, AB, ba, Ba, bA, BA
 * 
 * Number of unique permutations possible (ignoring cases) = n!
 * Permutations by changing cases = 2^n
 */

/**
 * @author Midnight-Coder @monstero
 *
 */

/*	Analysis
 * 
 *ex.   ab ==>{ab,ba}  			(n!)
 * 		ab = ab, Ab, aB, AB  	(2^n)
 * 
 * ex. abc ==> {abc, acb, bac, bca, cab, cba}
 * 	   abc= abc, Abc, aBc, abC
 * 		    ABC, aBC, AbC, ABc 	
 * theta(n!*2^n)
 */
public class PermutationsGoneCrazy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s = "abcd";
		casePermute2(new String(""), s.toLowerCase());
	}
	
	private static void casePermute2(String begin, String end) {
		if(end.length() <= 1){
			System.out.println(begin+end);
			permuteCase(begin, end.toUpperCase());
		}
		else{
			for(int i=0; i<end.length(); i++){
				//Fix one char at a position, and recurse on the remaining string
				String temp = end.substring(0, i) + end.substring(i+1);
				casePermute2(begin+end.charAt(i), temp);
			}
		}
	}

	private static void permuteCase(String lc, String up) {
		//TODO find the heart to write (2^n)*n! 
	}
}
