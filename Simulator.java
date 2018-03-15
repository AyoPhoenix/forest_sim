
public class Simulator {
	private Forest currentForest, updatedForest;
	private int height, width;
	
	public Simulator(int r, int c, double treeDensity) {
		height = r;
		width = c;
		currentForest = new Forest(height, width);
		currentForest.initializeForest(treeDensity);
		updatedForest = currentForest;
	}
	public Simulator(int r, int c) {
		height = r;
		width = c;
		currentForest = new Forest(height, width);
		updatedForest = new Forest(height, width);
	}
	
	public void run() {
		while(currentForest.getNumTreesStatus(1) != 0) 
			doOneStep();
	}
	public void doOneStep() {
		for(int r = 0; r < height; r++) {
			for(int c = 0; c < width; c++) {
				Tree t = currentForest.getObjectAt(r, c);

				if(t != null && t.getStatus() == 1)
					updatedForest.propogateFire(currentForest, r, c);
			}
		}
		
		currentForest = updatedForest;
	}
	
	public double getAveragePercentBurned(int width, int height, double density, int trials) {
		 double percentage = 0;
		 
		 for(int t = 0; t < trials; t++) {
			 Simulator sim = new Simulator(height, width, density);
			 sim.run();
			 percentage += sim.getForest().percentBurned();
		 }
		 
		 return percentage / trials;
	}
	
	public void reset(double treeDensity) {
		currentForest =  new Forest(height, width);
		currentForest.initializeForest(treeDensity);
		updatedForest = currentForest;
	}
	
	public Forest getForest() {
		return currentForest;
	}
}