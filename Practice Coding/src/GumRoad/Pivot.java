package GumRoad;

import java.util.Arrays;
import java.util.List;

/*
 * Write a method that returns the "pivot" index of a list of integers.
 *  We define the pivot index as the index where the sum of the numbers on the left is equal to the sum of the numbers on the right.
 *  Given [1, 4, 6, 3, 2], the method should return 2, 
 *  since the sum of the numbers to the left of index 2 is equal to the sum of numbers to the right of index 2 (1 + 4 = 3 + 2). 
 *  If no such index exists, it should return -1.
 *  If there are multiple pivots, you can return the left-most pivot.
 */
public class Pivot {

	public static int evaluatePivot(List<Integer> numbers) {
		int pivotIndex = 1;
		
		//Initialize sum of left and right sub array such that pivot = 1
		int sigLeft = numbers.get(0);
		int sigRight = 0;
		for(int i = pivotIndex+1; i<numbers.size(); i++) {
			sigRight += numbers.get(i);
		}
		//Hack to enable a quick exit in case pivot swings between left and right moves
		boolean isPivotMovementRight = true;
		
		while(pivotIndex < numbers.size()-1) {
			if(sigLeft==sigRight) {
				return pivotIndex;
			}
			else {
				sigLeft += numbers.get(pivotIndex);
				pivotIndex++;
				sigRight -= numbers.get(pivotIndex);
			}
		}
		return -1;
	}
	public static void main(String args[]) {
		Integer a[] = {1,4,-4,-4,1,2,2,1};
		int i = evaluatePivot(Arrays.asList(a));
		System.out.println("->" + i);
	}
}
