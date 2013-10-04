package AMZNChallenge;

public class ListLoopQuestion {
//FInd loop in a list
	// Robert Floyd in the 1960s - Floyd's cycle-finding algorithm
	  public static class ListNode {
	     public int value;
	     public ListNode next;
	  }
	 
	  public static boolean hasLoops( ListNode myList ) {
		  if(myList == null) {
			  return false;
		  }
		  ListNode tortoise, hare;
		  tortoise = myList;
		  hare = myList;
		  while(true) {
			  tortoise = tortoise.next;
			  if(hare.next != null) {
				  hare = hare.next.next;
			  }
			  else {
				  return false;
			  }
			  if(tortoise == null || hare == null) {//If either reach the null => no loop
				  return false;
			  }
			  if(hare.next == tortoise || hare == tortoise) {	//check against fast.next and fast so as to speed up the process -> avoids more traversal around the loop
				  return true;
			  }
		  }
	  }
	}