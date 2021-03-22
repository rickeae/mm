import java.util.*;
import java.util.stream.*; 

class Mastermind {
	int n; //number of possible solutions - n is reduced as we reduce the set of possible solutions
	
	int[][] a; //two dimensional array of the digits of all possible solutions
	int[][] xref = new int[4][12];
	
	int g[] = new int[4]; //this is the last guess - object should know its last guess and not have to be told
	int answer = 0;
	int correctPosition = -1;
	int wrongPosition = -1;
	int currentCountCorrect = 0;
	int currentCountWrong = 0;

	//used with the streams
	Object[] tempResult;
	boolean bStream = false; //use streams rather than non-steam code
	
	Random rand = new Random();
	
	Mastermind() { //constructor with no arguments
		reset();
	}// construction Mastermind()
	
	public void reset() {
		n = 10 * 10 * 10 * 10; //consider passing this in when the object is created
		
		//xref[0] not used
		//xref[1] not used
		this.xref[2] = new int[] { 1, 0 };
		this.xref[3] = new int[] { 1, 0, 0, 3, 3, 1 };
		this.xref[4] = new int[] { 1, 0, 0, 0, 2, 2, 1, 1, 3, 3, 3, 2 };

		a = new int[n][4];
		for(int i = 0; i<n; i++)
			for (int j = 0; j<=3; j++)
				this.a[i][j] = (i/(int)Math.pow(10,j))%10;	
	}//reset
	
	int[] calculateScore(int[] g, int[] a) {
		
		//this is the non-stream version of the method
		
		// this code repeatedly compares each digit of the answer to each digit of the guess
		// the used[] tracks which digits of the answer have already contributed to a count in the score
		boolean used[] = { false, false, false, false };
		int s[] = {0, 0};		
		for (int i = 0; i<=3; i++){
			if (g[i]==a[i]){
				s[0]++;
				used[i]=true;
			}//if
		}//for
		for (int guess=0; guess<=3; guess++) {
			// guess iterates over the digits in the guess
			if (g[guess]!=a[guess]){
				for (int answer=0; answer<=3; answer++) {
					if ( (!used[answer]) && (g[guess] == a[answer]) ) {
						// guess digit matches answer digit but is in the wrong position
						s[1]++;
						used[answer] = true;
						break; // break from answer loop to next digit of guess
					}//if
				}//for answer
			}// if (g[guess]!=a[guess])
		}// for guess
		return s;
	}//calculateScore()
	
	public int calculateScoreStream2(int[] g, int[] a) {
		
		boolean debug = false;
		
		//this is the stream version of the method
		//the method is called from eval()
		//it returns a two digit integer representing the score
		//computed when comparing guess g to answer a
				
		// this code repeatedly compares each digit of the answer to each digit of the guess
		// the used[] tracks which digits of the answer have already contributed to a count in the score
		
		int s[] = {0, 0};

		/*
		 * 
		boolean used[] = { false, false, false, false };
		
		for (int i = 0; i<=3; i++){
			if (g[i]==a[i]){
				s[0]++;
				used[i]=true;
			}//if
		}//for
		
		*/
	
		if (debug) System.out.println(Arrays.toString(s));
		if (debug) System.out.println(Arrays.toString(a));
		if (debug) System.out.println(Arrays.toString(g));
		//if (debug) System.out.println(Arrays.toString(used)+"\n");
		
		int[] b; //array holding index to elements that do not match
		b = IntStream.range(0,4).filter(e -> (g[e] != a[e])).toArray();
		final int m = b.length;
		s[0] = 4 - m;
		if (debug) System.out.println("s"+Arrays.toString(s));
		if (debug) System.out.println("b"+Arrays.toString(b)+"\n");
		
		/*
		s[0]=0;
		IntStream.range(0,4).filter(e -> (g[e] == a[e])).forEach( e -> {used[e]=true; s[0]++;});		
		if (debug) System.out.println(Arrays.toString(s));
		if (debug) System.out.println(Arrays.toString(a));
		if (debug) System.out.println(Arrays.toString(g));
		if (debug) System.out.println(Arrays.toString(used));
		*/
		
		int[] x = xref[m];
		final int c= (m * m) - m;
		
		/*
		 * IntStream    : { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }
		 * IntStream %4 : { 0, 1, 2, 3, 0, 1, 2, 3, 0, 1,  2,  3 }
		 * array        : { 1  0  0  0  2  2  1  1  3  3   3   2 }
		 * 
		 * ABCD
		 * abcd
		 * 0        1        2        3
		 * A(b,c,d) B(a,c,d) C(a,b,d) D(a,b,c) = 4!/2 = 24/2 = 12
		 *   1 2 3    0 2 3    0 1 3    0 1 2
		 */
		 /*
		 *
		 * IntStream    :   { 0, 1, 2, 3, 4, 5 }
		 * IntStream %3 : a { 0, 1, 2, 0, 1, 2 }
		 * compare      : x { 1  0  0  2  2  1 }
		 * 
		 * ABC
		 * abc
		 * 0      1      2
		 * A(b,c) B(a,c) C(a,b) = 2^3-2 = 6 = 3! = 6
		 *   1 2    0 2    0 1
		 */
		
		//              0   1   2   3   4   5
		//    e%3       0   1   2   0   1   2
		//              6   9   3   6   9   3
		//              3   5   5   6   6   3  
		//

		 /*   
		 *   
		 * IntStream    : { 0, 1 }
		 * IntStream %2 : { 0, 1 }
		 * array        : { 1  0 }
		 *   
		 * AB
		 * ab
		 * 0    1
		 * A(b) B(a) = 2^2-2 = 2! = 2
	 	 *   1    0
	 	 *   
		 */ 
	
		//IntStream.range(0,c).forEach(e -> System.out.println("a[b[e%m]]"+a[b[e%m]]+", g[x[e]]"+g[x[e]]+", bool="+(a[b[e%m]] == g[x[e]]) ));
		s[1] = (int) IntStream.range(0,c).filter(e -> a[b[e%m]] == g[x[e]] ).count();
		if (debug) System.out.println("s: " + Arrays.toString(s));
		
		/*
		int guess;
		for (int i=0; i<=b.length-1; i++) {
			// guess iterates over the digits in the guess
			guess = b[i];
			IntStream.range(0,b.length-1).filter(e -> (g[e] == a[e])).forEach( e -> {used[e]=true; s[0]++;});
			
			for (int j=0; j<=b.length-1; j++) {
				answer = b[j];
				if ( (!used[answer]) && (g[guess] == a[answer]) ) {
					// guess digit matches answer digit but is in the wrong position
					s[1]++;
					used[answer] = true;
					break;
				}//if
			}//for answer
		}// for guess	
		
		for (guess=0; guess<=3; guess++) {
			// guess iterates over the digits in the guess
			if (g[guess]!=a[guess]){;
				for (int answer=0; answer<=3; answer++) {
					if ( (!used[answer]) && (g[guess] == a[answer]) ) {
						// guess digit matches answer digit but is in the wrong position
						s[1]++;
						used[answer] = true;
						break;
					}//if
				}//for answer
			}// if (g[guess]!=a[guess])
		}// for guess
		*/
		
		return s[0]*10+s[1];
	}//calculateScoreStream2()

	public int calculateScoreStream(int[] g, int[] a) {
		int s[] = new int[2];
		final int[] b = IntStream.range(0,4).filter(e -> (g[e] != a[e])).toArray(); //array holding indices to elements that do not match
		final int m = b.length;
		s[0] = 4 - m;
		final int[] x = xref[m];
		final int c = ( m * m ) - m;
		s[1] = (int) IntStream.range(0,c).filter(e -> a[b[e%m]] == g[x[e]] ).count();
		return s[0]*10+s[1];
	}//calculateScoreStream2()

	void eval(double score) {	
		if (bStream){
			//stream version
			int intScore = (int) (score * 10.0) ;
			tempResult = Arrays.stream(a,0,n).parallel().filter( e -> calculateScoreStream(g,(int[]) e) == intScore ).toArray();
			n = tempResult.length;			
			IntStream.range(0,n).parallel().forEach( e -> a[e] = (int[]) tempResult[e]);	
		} else {
			//non-stream version
			this.correctPosition = (int) score;
			this.wrongPosition = (int) (score * 10) % 10;
			int[] s;
			for (int row = 0; row<=(n-1); row++) {	
				s = calculateScore(g,a[row]);
				currentCountCorrect = s[0];
				currentCountWrong = s[1];
				if ((currentCountCorrect != correctPosition) || (currentCountWrong != wrongPosition)) {
					a[row][0] = -1; //not a solution - mark row as invalid with sentinel -1
				}//if
			}//for row
			compact(); //move all the solutions that are still valid to the front of the list and reduce n to the number of validSolutions
		}// if (bStream)
	}//eval

	private void compact(){
		
		/*
		*this method not needed in the stream version
		*/
		
		//compact the array and reduce n to the new size of the array
		//if (!this.doesSolutionExist()){
			//System.out.println("\n(compact) err on in: "+this.answer+" does not exist");
		//}//if
		int lastValidRow = -1;
		int row = 0;
		while (row<n) {
			if ((a[row][0] != -1) && (row>lastValidRow))  { //row is good, move it forward and increment lastValidRow
				lastValidRow += 1;
				for (int j = 0; j<=3; j++) a[lastValidRow][j] = a[row][j];
				if (row != lastValidRow){
					a[row][0] = -1; //mark row as bad
				}//end if
			}//end if
			row += 1; //proceed to next row
		}//while i<=(n-1)
		n = lastValidRow + 1; //add one to account for the zero element of the array
		//if (!this.doesSolutionExist()){
			//System.out.println("(compact) err on out: "+this.answer+" does not exist\n");
		//}//if
	}//compact
	
	int[] numberToArray(int num){
		//takes a decimal number between zero and 9999 inclusive and return the digits as an array
		return new int[] { (num / 1000) % 10, (num / 100) % 10, (num / 10) % 10, num % 10 };
	}//numberToArray
	
	public int guess(){
		int row = 0;
		//if more than two solution remain then pick a random row and make that the current guess
	    if (n>1) row = rand.nextInt(n-1);
	    //set g[] to the chosen guess
	    g[0] = a[row][0]; //thousands
	    g[1] = a[row][1]; //hundreds
	    g[2] = a[row][2]; //tens
	    g[3] = a[row][3]; //ones
	    //return our guess as an integer for human consumption
	    return g[3] + g[2]*10 + g[1]*100 + g[0]*1000;
	}//guess
	
	public String makePaddedString(int number){
		String paddedString = "0000" + String.valueOf(number);
		paddedString = paddedString.substring(paddedString.length() - 4, paddedString.length()); //take the right 4 digits, which may include leading zeros if guess is less than 1000
		return paddedString;
	} //makePaddedString
	
	void printSolution() {
		//print the entire solution array
		if (bStream){
			Arrays.stream(a,0,n).forEach(x -> System.out.println(Arrays.toString(x)));
		} else {
			for (int i = 0; i <=n; i++)
					System.out.println("[ " + a[i][0] + ", " + a[i][1]+ ", " + a[i][2]+ ", " + a[i][3] + " ]");
		}//if(bStream)
	}//printSolution

	public boolean doesSolutionExist() {
		int[] s = numberToArray(this.answer);
		if (bStream){
			return ( Arrays.stream(a,0,n).filter( e -> s == (int[]) e ).count() != 0 );
		} else {
			int flag;
			for (int row = 0; row<=(n-1); row++) {
				flag = 1;
				for (int i = 0; i<=3; i++){
					if (a[row][i] != s[i]) {
						flag = 0;
					}//if
				}//for i
				if (flag==1) return true;
			} //for row
			return false;
		}// if(bStream)
	}//doesSolutionExist
	
}//class Mastermind