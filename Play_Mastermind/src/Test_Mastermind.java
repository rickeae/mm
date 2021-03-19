import java.util.Arrays;

public class Test_Mastermind {
	public static void main(String[] args) {
		
		/*
        StdDraw.setPenRadius(0.05);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.point(0.5, 0.5);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.line(0.2, 0.2, 0.8, 0.2);
        StdDraw.text(.7,.7,"test");
		
		StdOut.println("Test");
        StdOut.println(17);
        StdOut.println(true);
        StdOut.printf("%.6f\n", 1.0/7.0);
		*/
		
		Mastermind mm = new Mastermind();
		mm.printSolution();
		
		//int[] a = mm.numberToArray(6013); //guess
		//int[] g = mm.numberToArray(659); //solution
		//int[] s = new int[2];
		//s = mm.calculateScore(a, g);
		//System.out.println("Score: "+s[0]+"."+s[1]);
	}//main
	
}//class Test_Mastermind