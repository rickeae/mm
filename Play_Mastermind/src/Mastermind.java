import java.util.*;
import java.util.stream.*; 

class Mastermind {
	int n; //number of possible solutions - n is reduced as we reduce the set of possible solutions
	int[][] a; //two dimensional array of the digits of all possible solutions
	int[][] xref = new int[4][12]; //array of indices to short cut the comparison process
	int g[] = new int[4]; //this is the last guess - object should know its last guess and not have to be told
	int answer = 0; //answer provided by user to help with debugging
	Object[] tempResult; //used with the streams in Eval
	boolean bStream = false; //use streams rather than non-steam code
	Random rand = new Random();
	int correctPosition, wrongPosition;
	
	Mastermind() { //constructor with no arguments
		reset();
	}// construction Mastermind()
	
	public void reset() {
		n = 10 * 10 * 10 * 10; //consider passing this in when the object is created	
		//xref[0] not used - need to auto gen this array based on math - do like hard coding it
		this.xref[1] = new int[] { 1, 0 };
		this.xref[2] = new int[] { 1, 0, 0, 3, 3, 1 };
		this.xref[3] = new int[] { 1, 0, 0, 0, 2, 2, 1, 1, 3, 3, 3, 2 };
		a = new int[n][4];
		for(int i = 0; i<n; i++)
			for (int j = 0; j<=3; j++)
				this.a[i][j] = (i/(int)Math.pow(10,j))%10;	
	}//reset
	
	public double calculateScore(int[] g, int[] a) {
		final int s[] = new int[2];
		final int[] b = IntStream.range(0,4).parallel().filter(e -> (g[e] != a[e])).toArray(); //array holding indices to elements digit that do not match digits in the same position
		final int m = b.length; //b.length will be either 4, 3, or 2 - it can't be 1
		s[0] = 4 - m;
		if (m>1){
			final int[] x = xref[m-1];
			final int c = ( m * m ) - m;
			s[1] = (int) IntStream.range(0,c).parallel().filter(e -> a[b[e%m]] == g[x[e]] ).count(); //number of digits in the wrong position
		}//if
		return s[0] + (double) s[1] / 10 ;
	}//calculateScore

	void eval(double score) {	
			this.correctPosition = (int) score;
			this.wrongPosition = (int) ((score * 10) % 10);
			tempResult = Arrays.stream(a,0,n).parallel().filter( e -> calculateScore(g,(int[]) e) == score ).toArray();
			n = tempResult.length;			
			IntStream.range(0,n).parallel().forEach( e -> a[e] = (int[]) tempResult[e]);	
	}//eval

	int[] numberToArray(int num){
		//takes a decimal number between zero and 9999 inclusive and return the digits as an array
		return new int[] { (num / 1000) % 10, (num / 100) % 10, (num / 10) % 10, num % 10 };
	}//numberToArray
	
	public int guess(){
		int row = 0;
		//if more than two solution remain then pick a random row and make that the current guess
	    if (n>1) row = rand.nextInt(n-1);
	    for(int i = 0; i<=3; i++)
	    	g[i] = a[row][i]; //thousands in [0], hundreds in [1], tens in [1] and ones in [0]
	    return g[3] + g[2]*10 + g[1]*100 + g[0]*1000; //return guess as an integer for human consumption
	}//guess
	
	public String makePaddedString(int number){
		String paddedString = "0000" + String.valueOf(number);
		paddedString = paddedString.substring(paddedString.length() - 4, paddedString.length()); //take the right 4 digits, which may include leading zeros if guess is less than 1000
		return paddedString;
	} //makePaddedString
	
	void printSolution() {
		//print the entire solution array
		Arrays.stream(a,0,n).forEach(x -> System.out.println(Arrays.toString(x)));
	}//printSolution

	public boolean doesSolutionExist() {
		final int[] s = numberToArray(this.answer);
		System.out.println(answer);
		return ( Arrays.stream(a,0,n).filter( e -> s == (int[]) e ).count() != 0 );
	}//doesSolutionExist
	
}//class Mastermind
