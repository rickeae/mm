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
		//mm.printSolution();
		
		int[] g = mm.numberToArray(6973); //solution
		int[] a = mm.numberToArray(5376); //guess
		System.out.println("g"+Arrays.toString(g));
		System.out.println("a"+Arrays.toString(a));
		double s = 0;
		s = mm.calculateScore(a, g);
		System.out.println("Score: " + s);
	}//main
	
}//class Test_Mastermind