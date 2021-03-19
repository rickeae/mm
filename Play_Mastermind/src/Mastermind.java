import java.util.*;
import java.util.stream.*; 

class Mastermind {
	int n; //number of possible solutions - n is reduced as we reduce the set of possible solutions
	
	int[][] a; //two dimensional array of the digits of all possible solutions
	
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
	
	int calculateScoreStream(int[] g, int[] a) {
		
		//this is the stream version of the method
		//the method is called from eval()
		//it returns a two digit integer representing the score
		//computed when comparing guess g to answer a
				
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
						break;
					}//if
				}//for answer
			}// if (g[guess]!=a[guess])
		}// for guess
		return s[0]*10+s[1];
	}//calculateScoreStream()

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