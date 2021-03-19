import java.util.Scanner;

public class Play_Mastermind {
	public static void main(String[] args) {
	    Scanner console = new Scanner( System.in );  
		Mastermind mm = new Mastermind();
		System.out.print("\nEnter solution (this helps with debugging): ");
		mm.answer = console.nextInt();
		System.out.print("\n");
		int guess;
		double score = 5.0; //sentinel must not be between 0 and 4 inclusive
		int guessCounter = 0;
		String guessString = new String();
		int[] s = new int[4];
		int[] a = new int[4];
		while (score != 4.0){
			guessCounter++;
			guess = mm.guess();
			guessString = "0000" + String.valueOf(guess);
			guessString = guessString.substring(guessString.length() - 4, guessString.length()); //take the right 4 digits, which may include zeros if guess is small
			//System.out.println("         "+mm.answer);
			if (mm.n>1){
				System.out.println("Guess "+guessCounter+": " + guessString + "     There are " + mm.n + " possible solutions remaining");
			} else {
				System.out.println("Guess "+guessCounter+": " + guessString + "     This is my final answer.");	
				break;
			}//if
			if (mm.doesSolutionExist() == false) {
				System.out.println("Hmmm, seems you may have made a mistake.");
				break;
			}//if
			
			/*
			 * get user entered score
			 * check for invalid scores being entered
			 */
			score = 5.0; //sentinel must not be between 0 and 4 inclusive
			while ((score > 4) || (score < 0)) {
				a = mm.numberToArray(mm.answer);
				s = mm.calculateScore(mm.g, a);
				System.out.print("Score "+guessCounter+" ("+s[0]+"."+s[1]+"): ");
				score = console.nextDouble();
				if (score == -1) {
					mm.printSolution();
					//System.out.println("         "+mm.answer);
					System.out.println("Guess "+guessCounter+": " + guess + "     There are " + mm.n + " possible solutions remaining");
				//} else if ( (score%10 != s[0]) || ((score*10)%10 != s[1])) {
					//user entered score does not match computer's scoring
					//score = -1;
					//System.out.println("\nYou may want to check your scoring. I think you made a mistake.");
				} else if (score > 4) {
					System.out.println("\nScore cannot be greater than 4.0. Try again.");
				} else if (score < 0) {
					System.out.println("\nScore cannot be less than zero. Try again.");					
				} else if ((score * 10) % 10 > 4) {
					System.out.println("\nFractional part cannot be greater than 4. Try again.");								
				}//if
			}//while
			mm.eval(score);
			if (mm.correctPosition ==1){
				System.out.println("                  You scored " + mm.correctPosition + " digit in the correct position, and " + mm.wrongPosition + " in the wrong position\n");				
			} else {
				System.out.println("                  You scored " + mm.correctPosition + " digits in the correct position, and " + mm.wrongPosition + " in the wrong position\n");
			}//if
		}//while
		console.close();
		System.out.println("Done.");
	}//main
}//class Play_Mastermind