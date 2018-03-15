public class NonGUI {
	public static void main(String[] args) {
		// A non-graphical runner for doing a lot
		// of simulations and displaying the results

		// You'd run this if you wanted to run, say 1000 trials for
		// a set of initial conditions and see the results.
		int height, width, trials;

		height = 100;
		width = 100;
		trials = 1000;
		Simulator sim = new Simulator(height, width);
		
		for(double treeDensity = 0.00; treeDensity <= 1.00; treeDensity += 0.01) {
			double avgPercent = sim.getAveragePercentBurned(height, width, treeDensity, trials);
//			System.out.println("Tree Density: " + (int)(treeDensity*100) + "% \tAverage Percent: " + avgPercent);
//			System.out.println(Math.round(treeDensity*100)/100.0 + ", " + avgPercent);
			System.out.println(avgPercent);
		}
		
	}
}
