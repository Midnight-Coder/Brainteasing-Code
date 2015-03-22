import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Midnight-Coder @monstero
 *Overrides the iterator 
 * @param <Integer>
 */
class NestedIterator<T> implements Iterator<Integer>{
	Integer[][] list;
	//maintains state of the iterator: {Index in parent array, index within subArray OR -1}
	int[] index;
	//maintains state to disallow consecutive remove operations
	boolean wasNextCalled;
	public NestedIterator(Integer[][] x) {
		this.list = x;
		index = new int[2];
		index[0] = 0;
		index[1] = (list[0].length > 0 )? 0 : -1;
		wasNextCalled = false;
	}

	@Override
	public boolean hasNext() {
		//Preserve the iteration state
		int stashState[] = index.clone();
		try{
			this.next();
			//Restore the iteration state after next() 
			index = stashState;
		}
		catch(IndexOutOfBoundsException e){
			return false;
		}
		return true;
	}

	@Override
	public Integer next() throws IndexOutOfBoundsException{
		//Sets the flag to allow a single remove operation
		wasNextCalled = true;
		//Check if sub array has more elements to offer		
		if(index[1] < list[index[0]].length-1){
			index[1]++;
			return list[index[0]][index[1]];
		}
		//Else check if the array has more sub arrays with elements to offer
		index[0]++;
		while( index[0]<list.length && list[index[0]].length == 0 ){
			index[0]++;
		}
		if(index[0] < list.length){
			//This is sub-array has a minimum of 1 element
			index[1] = 0;
			return list[index[0]][index[1]];
		}
		else{
			throw new IndexOutOfBoundsException("Reckless programmer be wary.");
		}
	}

	@Override
	public void remove() throws IllegalStateException{
		if(!wasNextCalled){
			throw new IllegalStateException("Cannot remove without first calling next()");	
		}
		//Check if it is a singular element sub array
		if(list[index[0]].length == 1){
			list[index[0]] = new Integer[0];
		}
		//Else copy the other elements of the sub list
		else{
			Integer[] shallowCopy = new Integer[list[index[0]].length-1];
			for(int i = 0, j = 0; i < list[index[0]].length; i++){
				if(i != index[1]){
					shallowCopy[j] = list[index[0]][i];
					j++;
				}
			}
			list[index[0]] = shallowCopy;
		}
		//Prim the state
		index[1]--;
		wasNextCalled = false;	
	}
}

public class TestNestedIterator{
	public static void main(String args[]){
	    testPrint();
		testRemove();

	}
	private static boolean testRemove() {
		Integer x[][] = {{},{1,2,3},{4,5},{},{},{6},{7,8},{},{9},{10},{}};
		Integer expectedOutput[][] =   {{},{1,3},{5},{},{},{},{7},{},{9},{},{}};
		NestedIterator<Integer> j, i = new NestedIterator<Integer>(x);
		int temp;
		System.out.println("Testing remove");
		while(i.hasNext()){
			temp = i.next();
			if(temp%2==0){
				System.out.println("\nRemoving:" + temp);
				i.remove();
			}
			else{
			System.out.print(temp + ", ");
			}
		}
		i = new NestedIterator<Integer>(x);
		j = new NestedIterator<Integer>(expectedOutput);
		
		while(i.hasNext() && j.hasNext()){
			if(i.next() != j.next()){
				System.err.println("Mismatch::Expected " + x + " to match " + expectedOutput);
				return false;
			}
		}
		System.out.println("Passed");
		return true;
	}
	
	private static boolean testPrint(){
		Integer x[][] = {{},{1,2,3},{4,5},{},{},{6},{7,8},{},{9},{10},{}};
		Integer expectedOutput[] = {1,2,3,4,5,6,7,8,9,10};
		ArrayList<Integer>output = new ArrayList<Integer>();
		NestedIterator<Integer> i = new NestedIterator<Integer>(x);
		System.out.println("Testing iteration");
		while(i.hasNext()){
			output.add(i.next());
		}
		System.out.println(output);
		for(int j = 0; j < output.size(); j++){
			if( output.get(j) != expectedOutput[j]){
				System.err.println("Mismatch::Expected " + output + " to match " + expectedOutput);
				return false;
			}
		}
		System.out.println("Passsed");
		return true;
	}
	
}
