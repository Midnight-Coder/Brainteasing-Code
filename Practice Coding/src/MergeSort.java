
public class MergeSort {

	void mergeSort(int list[], int start, int end) {
		int[] solution = new int[list.length];
		if(start>=end) {
			return;
		}
		
		int mid = (start+end)/2 + start;
		mergeSort(list, start, mid);
		mergeSort(list,mid+1, end);
		merge(list, start, mid, mid+1, end, solution);
	}
	//TODO: consider generic objects
	private void merge(int[] list, int startList1, int endList1, int startList2, int endList2, int[] solution) {
		int j = 0;
		while(startList1 <= endList2 && startList2 <= endList2) {	
			if(list[startList1] > list[startList2]) {
				solution[j] = list[startList2];
				startList2++;
			}
			else {
				solution[j] = list[startList1];
				startList1++;
			}
			j++;
		}
		while(startList1<=endList1) {
			solution[j] = list[startList1];
			j++;
			startList1++;
		}
		while(startList2<=endList2) {
			solution[j] = list[startList2];
			j++;
			startList2++;
		}
	}
}