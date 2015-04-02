import java.util.ArrayList;
import java.util.Scanner;


public class primeFactor {
	/*
	 * Complete the function below.
	 */
	public static void main(String args[]){
		System.out.println("Enter number");
		Scanner sc = new Scanner(System.in);
		String s = sc.nextLine();
		int a[] = new int[100];
		String stupid[] =s.split(" ");
		for(int i=0;i<stupid.length;i++){
			a[i] = Integer.parseInt(stupid[i]);
			System.out.println(a[i] + " ");
		}
		System.out.println(getNumberOfPrimes(100));
	}
	static int getNumberOfPrimes(int N) {
	    ArrayList<Integer> primes = new ArrayList<Integer>();

	    boolean isPrime;
	    int num;
	    
	    if(N<=0){
	        //validation for whole numbers
	        return 0;
	    }
	    if(N<=2){
	        //Allows us to iterate through odd numbers
	        return 1;
	    }
	    for(num = 3; num<=N; num+=2){
	        isPrime = true;
	        for(Integer i:primes){
	            if(num%i == 0){
	                isPrime = false;
	                break;
	            }
	            if(i>Math.sqrt(num)){
	            	break;
	            }
	        }
	        if(isPrime){
	            primes.add(num);
	        }
	    }    
	    return primes.size()+1;
	}

}
