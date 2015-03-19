import java.util.Scanner;

/*
 * Given 2 strings find if they match:
 * 	1. String can have characters a-z, *, .
 * 	2. * : ability to delete the char occurring before it (can be ignored)
 * 	3. . : ability to take form of any character (cannot be ignored)
 * http://www.careercup.com/question?id=6631993756352512
 */
/*
 * My Approach : 
 * 	Iterate through the strings n-1..0
 * 	Compare the possibility of  * *, * ., . ., * char, . char etc...
 * 	Assumptions : A string cannot have * followed by * eg. a** -invalid
 *  Time complexity : O(max(n,m)) where n and m are the lengths of 2 strings
 *  Space complexity : O(1)
 */
public class MatchStringsWithSpecialOperators {

	private static char Delete = '*';
	private static char Wildcard = '.';
	
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		String one = sc.nextLine();
		String two = sc.nextLine();
		
		int stars1 = countStars(one);
		System.out.println(isMatch(one, stars1, two));
		sc.close();
	}
	/*
	 * Counts the number of delete operators present in the string
	 * Used to estimate the length the string may have
	 */
	private static int countStars(String str) {
		int count = 0;
		for(int i=0; i<str.length(); i++) {
			if(str.charAt(i) == Delete) {
				count++;
			}
		}
		return count;
	}

	/*
	 * Assumes that only String one has the delete char
	 * Reduces runtime from O(n2) to O(n)
	 */
	private static boolean isMatch(String one, int numberOfDeleteOpsInFirst, String two) {
		int i,j,n,m;
		n = one.length();
		m = two.length();
		if(m>n) {
			return false;
		}
		//eg bbx*b*z, bbb => 4-(6-3)
		/*#chars that can be deleted - #superfluous chars*/
		int numberOfDeleteOpsToBeSkipped = (numberOfDeleteOpsInFirst*2) - (n-m);
		if(numberOfDeleteOpsToBeSkipped < 0) {
			return false;
		}
		boolean deleteNextCharFlag = false;
		i=j=0;
		while(i<n && j<m) {
			char onei = one.charAt(i);
			char twoj = two.charAt(j);
			if(onei != Delete) {
				if(deleteNextCharFlag) {
					/*Expected to delete the previous mismatching char*/
					return false;
				}
				if(twoj == Wildcard || onei == twoj || onei == Wildcard) {
					i++;
					j++;
				}
				else {
					i++;
					deleteNextCharFlag = true;;
				}
			}
			else {
				if(deleteNextCharFlag) {
					i++;
					deleteNextCharFlag = false;
					continue;
				}
				/*
				 * There has been no mismatch in the characters until this point
				 * Delete/Ignore the prev character only if the equality in the string size has to be maintained 
				 */
				if(one.charAt(i+1) == twoj) {
					/*Option to delete exists iff one[i+1] = two[j]*/
					if(numberOfDeleteOpsToBeSkipped == 0){
						// =>Delete
						i+=2;
						j++;
					}
					else {
						/*Don't perform deletion as the size of string will be unequal*/
						i++;
						j++;
						numberOfDeleteOpsToBeSkipped--;
					}
				}
				else {
					/*No deletion since one[i+1] != two[j]*/
					i++;
					j++;
				}
			}
		}
		/*
		 * There has been no mismatch in the characters until this point & String two is exhausted
		 * Ensure the subsequent chars in String one can be deleted 
		 */
		if(one.charAt(i)==Delete) { /*The Delete operator for previous char can be ignored*/
			i++;
		}
		int charsCanBeDeleted = 0;
		while(i<n) {
		
			if(one.charAt(i)==Delete){
				charsCanBeDeleted++;
			}
			else {
				charsCanBeDeleted--;
			}
			i++;
		}
		if(charsCanBeDeleted!=0) {
			return false;
		}
		return true;
	}
	//TODO code for Both strings - DP
}