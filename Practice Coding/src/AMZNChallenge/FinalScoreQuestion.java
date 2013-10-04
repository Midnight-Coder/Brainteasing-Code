package AMZNChallenge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*Given a list of test results (each with a test date, Student ID, and the student’s Score), 
 * return the Final Score for each student.
 *  A student’s Final Score is calculated as the average of his/her 5 highest test scores.
 *   You can assume each student has at least 5 test scores.
 */
  
 /*  Iterate through the list and populate the map(with sum of scores). O(n)
*Another iteration through the map to divide by 5. O(n)
*/
 
/* Enter your Big O Analysis here. 
2*O(n) = O(n)*/
 
/* Enter your code here. */


class TestResult {
      
	private int studentId;
	private String testDate;
	private int testScore;

	public int getTestScore() {
		return testScore;
	}
	public void setTestScore(int testScore) {
		this.testScore = testScore;
	}
	public String getTestDate() {
		return testDate;
	}
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
      
   }
 
public class FinalScoreQuestion {
	
	Map <Integer, Double> calculateFinalScores (List<TestResult> results) {
	
	   Map<Integer,Double> finalscores = new HashMap<Integer,Double>();
	   Map<Integer, List<Integer>> idScores = new HashMap<Integer, List<Integer>>();
	   
	   List<Integer> temp;
	   int studentid;
	   double score;
	   //Organize the student with their respective scores 
	   for(TestResult i:results) { // => O(n)
		   int index = 0;
		   studentid = i.getStudentId();
		   score = i.getTestScore();
		   if(idScores.containsKey(studentid)) { // amortized O(1)
			     temp = idScores.get(studentid); // amortized O(1)
			     //Organize the top 5 in descending order by insertion sort (hack) => O(5)
			     index = organizeTop5(temp,score);
		   }
		   else {
			   temp = new ArrayList<Integer>();
		   }
			   temp.add(index,(int)score);
			   idScores.put(studentid, temp);
	   }
	   //Evaluate the top 5 scores
	   for(Map.Entry<Integer, List<Integer>> entry:idScores.entrySet()) { // => O(m) : m = #of students
		   studentid = entry.getKey();
		   temp = entry.getValue();
		   score = 0.0;
		   for(int i=0;i<5;i++) { // => O(5)
			   score += temp.get(i);
		   }
		   score/=5;
		   finalscores.put(studentid, score);
	   }
	  return finalscores; 
   }

	private int organizeTop5(List<Integer> temp, double score) {
		int index = (temp.size() >= 5)?5:temp.size();
		while(index>=0 && temp.get(index) < score) {
			index--;
		}
		return index;
	}
 }