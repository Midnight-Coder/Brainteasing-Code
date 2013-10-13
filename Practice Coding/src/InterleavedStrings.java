
public class InterleavedStrings {
/*
 * Given three strings A, B and C. 
 * Write a function that checks whether C CONTAINS A and B. 
 * C is said to be interleaving A and B if: 
 * It contains all characters of A and B and 
 * order of all characters in individual strings is preserved.
 * Modified version of : http://www.geeksforgeeks.org/check-whether-a-given-string-is-an-interleaving-of-two-other-given-strings-set-2/ 
 */
	/*
	 * My approach #1 init():
	 * Iterate through C and have two flags indicating which substring (A & B) is being processed and valid
	 * The solution runs in O(n) where n is the length of String C and it requires space O(n) to store C[]
	 * Doesn't work for: A/B contain consecutive repeating characters. Cannot be done in O(n)
	 *  
	 *  Approach #2 smarterCode():
	 *  Uses concepts of dynamic programming with memoization
	 *  Runs in O(n2) and space O(n2)
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String a = "abcd";
		String b = "xyz";
		String c = "efgababcdxyxyzlala";
		boolean flag = init(a,b,c);
		boolean smartFlag = smarterCode(a,b,c);
		System.out.println(flag + " & " + smartFlag);
	}
	private static boolean smarterCode(String A, String B, String C) {
		if(!sanityCheck(A, B, C)) {
			return false;
		}
		char a[] = A.toCharArray();
		char b[] = B.toCharArray();
		char c[] = C.toCharArray();
		
		boolean truthMatrixA[][] = new boolean[c.length-a.length][a.length];
		boolean truthMatrixB[][] = new boolean[c.length-b.length][b.length];
		int ai,bi;
		ai=bi=0;
		for(int i=0; i<truthMatrixA.length; i++) {
			if(ai == a.length) {
				ai = -1;
				break;
			}
			for(ai=0; ai<a.length; ai++)
			if(a[ai] == c[i+ai]) {
				truthMatrixA[i][ai] = true;
				if(ai==0){
					continue;
				}
				//To ensure order of chars
				truthMatrixA[i][ai] &= truthMatrixA[i][ai-1];
			}
			else {
				break;
			}
		}
		
		for(int i=0; i<truthMatrixB.length; i++) {
			if(bi == b.length) {
				bi = -1;
				break;
			}
			for(bi=0; bi<b.length; bi++)
				if(b[bi] == c[i+bi]) {
					truthMatrixB[i][bi] = true;
					if(bi==0){
						continue;
					}
					//To ensure order of chars
					truthMatrixB[i][bi] &= truthMatrixB[i][bi-1];
				}
				else {
					break;
				}
			}
		if(ai == -1 && bi == -1) {
			return true;
		}
		else {
			return false;
		}
	}
	private static boolean init(String A, String B, String C) {
		//COnvert to [] for efficient access
		if(!sanityCheck(A, B, C)){
			return false;
		}
		char a[] = A.toCharArray();
		char b[] = B.toCharArray();
		char c[] = C.toCharArray();
		
		//index to access a[],b[],c[]
		int ai,bi,ci;
		ai = bi = ci = 0;
		
		//Flags to ensure order of a[], b[]
		int diffA, diffB;
		diffA = diffB = -1;
		
		//Flags to ensure a[0..n] b[0..n] done
		boolean flagA,flagB;
		flagA = flagB = false;
		
		for(;ci<c.length && !(flagA && flagB);ci++) {
			if(!flagA && a[ai] == c[ci]) {
				diffB = -1;
				if(diffA == -1) {
					//diffA will always be constant if the C[] contains the whole substring a[] in the order 0-an
					diffA = ci - ai;
				}
				if (diffA == ci-ai) {
					ai++;
				}
			}
			else if(!flagB && b[bi] == c[ci]) {
				//Discounts the possibility of overlapping substrings
				diffA = -1;
				if(diffB == -1) {
					diffB = ci - bi;
				}
				else if(diffB != ci-bi) {
				}
				bi++;
			}
			else {
				//Reset & reiterate => since there was an inequality therefore the order is broken.Reiterate over the same ci for beginning of substring a
				if(diffA!=-1 || diffB!=-1) {
					ai = 0;
					bi = 0;
					diffA = diffB = -1;
					ci--;
				}
				continue;
			}
			
			if(!flagA && ai == a.length){
				flagA = true;
			}
			if(!flagB && bi == b.length) {
				flagB = true;
			}
		}
		return flagA & flagB;
	}
	private static boolean sanityCheck(String A, String B, String C) {
		int an = A.length();
		int bn = B.length();
		int cn = C.length();
		if(an == 0 || bn == 0 || cn == 0) {
			return false;
		}
		if(an+bn > cn) {
			//Overlapping != interleaving
			return false;
		}
		//TODO should check for repeating chars like 'aaab' since my code doesn't support that (O(n2)) 
		return true;
	}
}
