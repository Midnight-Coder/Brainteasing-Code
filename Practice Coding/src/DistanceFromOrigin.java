import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/*
 * There are n points on a 2D plane, find k points that are closest to origin ( x=0, y=0)
 * http://www.careercup.com/question?id=5309537623998464
 */

class Coordinates {
	private int abscissa, ordinate;
	private float originDistance;
	Coordinates(int x, int y)	{
		abscissa = x;
		ordinate = y;
		//Calculate the distance from the origin = (x^2) - (y^2)
		originDistance = (abscissa * abscissa) + (ordinate * ordinate);
	}

	public float getOriginDistance() {
		return originDistance;
	}
	public int getAbscissa() {
		return abscissa;
	}
	public int getOrdinate() {
		return ordinate;
	}
	@Override
	public String toString() {
		String s = "(" + abscissa + "," + ordinate + ")" + "-->" + originDistance;
		return s;
	}
}
public class DistanceFromOrigin {

	public static void main(String args[]) {
		ArrayList<Coordinates> points = new ArrayList<Coordinates>();
		int k;
		k = testValues(points);
		quickSelect(points, k); //TODO Look into introselect
		System.out.println(points);
	}
	private static int testValues(ArrayList<Coordinates> points) {
		int k;
		{
			points.add(new Coordinates(1,1));
			points.add(new Coordinates(1,4));
			points.add(new Coordinates(-1,-1));
			points.add(new Coordinates(2,-1));
			k = 3;
		}
		return k;
	}
	/*
	 * Arranges the list such that:
	 * Elements to the left of k are closer to the origin than to the right of k
	 */
	private static void quickSelect(ArrayList<Coordinates> points, int k) {
		int size = points.size();
		if(size < k) {
			//Error ?
			throw new Error();
		}
		else if (size == k) {
			return;
		}
		int left = 0;
		int mid = k;//(left + size-1)/2;
		float pivot = points.get(mid).getOriginDistance();
		int right = (k==size-1)?k:k+1;
		while(left<mid) {
			if(points.get(left).getOriginDistance() > pivot) {
				Collections.swap(points,left,right);
				right --;
			}
			else {
				left++;
			}
		}
		return ;
	}
}