import java.util.Scanner;


public class MultiplyExceptSelf {

	public static void main(String[] args){
		init();
	}
	private static void init() {
		Scanner sc = new Scanner(System.in);
		int n = Integer.parseInt(sc.nextLine());
		long numbers[] = new long[n];
		long product = 1;
		int numberOfZeros = 0;
		for(int i=0; i<n; i++) {
			numbers[i] = Long.parseLong(sc.nextLine());
			if(numbers[i] == 0) {
				numberOfZeros++;
			}
			else {
			product *= numbers[i];
			}
		}
		if(numberOfZeros == 1){
			for(long i:numbers) {
				if(i==0) {
					System.out.println(product);
				}
				else {
					System.out.println("0");
				}
			}
		}
		else if(numberOfZeros > 1) {
			for(int i=0; i<numbers.length; i++) {
				System.out.println("0");
			}
		}	
		else {
			for(long i:numbers) {
				System.out.println(product/i);
			}
		}
		sc.close();
	}
}