import java.util.*;

public class ComputerPlay {
	public static void main(String[] args) {
		
		boolean enablePrint = true;
		int numGames = 2;
		
		int guess=0;
		int guessCounter;
		double score;
		int[] s = new int[4];
		int[] a = new int[4];
		int totalTries = 0;
		String guessString = new String();
		
		Mastermind mm = new Mastermind();
		Random rand = new Random();
		
		for (int k = 0; k<numGames; k++) {
			mm.answer = rand.nextInt(9999);
			if (enablePrint & numGames > 1) System.out.print("Game " + k + ": ");
			if (enablePrint) System.out.println("Secret code is " + mm.makePaddedString(mm.answer) + "\n");
			score = 5.0; //sentinel must not be between 0 and 4 inclusive
			guessCounter = 0;
			//mm.result.forEach(x -> System.out.println(Arrays.toString(x)));
			mm.reset();
			while (score != 4.0){
				guessCounter++;
				guess = mm.guess();
				if (mm.n>1){
					if (enablePrint) System.out.println("Guess "+guessCounter+": " + mm.makePaddedString(guess) + "        There are " + mm.n + " possible solutions remaining");
				} else {
					if (enablePrint) System.out.println("Guess "+guessCounter+": " + mm.makePaddedString(guess) + "        This is my final answer.");	
					break;
				}//if
				a = mm.numberToArray(mm.answer);
				s = mm.calculateScore(mm.g, a);
				score = (s[0] * 10.0 + s[1]) / 10.0;
				if (enablePrint) System.out.print("Score "+guessCounter+": " + score);
				mm.eval(score);
				if (enablePrint) System.out.println("         You scored " + mm.correctPosition + " digit(s) in the correct position, and " + mm.wrongPosition + " in the wrong position\n");
			}//while
			if (enablePrint) System.out.println("Done. Initial solution was "+ mm.answer + "\n");
			//if (enablePrint) System.out.println(new String(new char[70]).replace("\0", "\r\n"));
			totalTries += guessCounter;
			//check that computer got the solution correct
			//this is checking for bugs in the code
			if(mm.answer != guess) {
				System.out.println("Error - computer's solution is wrong");
				break;
			}//
		}//k
		if (numGames > 1) System.out.println("All done. I solved " + numGames + " games in an average of " +  ((double) totalTries / (double) numGames) + " guesses");
	}//main
}//class Play_Mastermind