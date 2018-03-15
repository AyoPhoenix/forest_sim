package updated;

public class Forest {
	// Instance Variables
	private int height, width;
	
	private Tree[][] forest;
	private int[] numTrees;
	
	private double[][] temperature;
	private int heatSpreadDist;
	private double heatLostPerUpdate;
	private double heatDissipation;
	
	public static final double TEMP_DEFAULT = 21;
	public static final double TEMP_MAX = 1500;
	public static final double TEMP_IGNITION = 500;
		
	// Constructors
	public Forest(int r, int c) {
		height = r;
		width = c;
		forest = new Tree[height][width];
		temperature = new double[r][c];
		heatSpreadDist = 2;
		heatLostPerUpdate = 500;
		heatDissipation = 10;
	}
	public Forest(int r, int c, int heatSpreadDist, double heatLostPerUpdate, double heatDissipation) {
		this(r, c);
		this.heatSpreadDist = heatSpreadDist;
		this.heatLostPerUpdate = heatLostPerUpdate;
		this.heatDissipation = heatDissipation;
	}
		
	public void initialize(double treeDensity) {
		initializeTemps();
		initializeTrees(treeDensity);
	}
	public void initializeTemps() {
		for(int r = 0; r < height; r++) {
			for(int c = 0; c < width; c++) {
				temperature[r][c] = TEMP_DEFAULT;
			}
		}
	}
	public void initializeTrees(double treeDensity) {
		int t = (int)(height*width*treeDensity);
		numTrees = new int[3];
		numTrees[Tree.ASH] = 0;
		numTrees[Tree.LIVING] = 0;
		numTrees[Tree.ON_FIRE] = 0;
		
		for(int i = 0; i < t; i++) {
			int randY = (int)(Math.random()*height);
			int randX = (int)(Math.random()*width);
			
			if(getObjectAt(randY, randX) == null) {
				Tree tree;
				
				if(i == t - 1) {
					tree = new Tree(1);
					numTrees[Tree.ON_FIRE]++;
					temperature[randY][randX] = TEMP_MAX;
				} else {
					tree = new Tree();
					numTrees[Tree.LIVING]++;
				}
				
				forest[randY][randX] = tree;
			} else i--;
		}
	}
	public void reset(double treeDensity) {
		forest = new Tree[height][width];
		temperature = new double[height][width];
		initialize(treeDensity);
	}
	
	public double getAveragePercentBurned(int height, int width, double density, int trials) {
		double percentage = 0;
		
		for(int t = 0; t < trials; t++) {
			Forest forest = new Forest(height, width);
			forest.initialize(density);
			forest.run();
			percentage += forest.getPercentBurned();
		}
		
		return percentage / trials;
	}
	public void run() {
		while(this.getNumTreesStatus(1) != 0)
			update();
	}
	
	public void update() {
		updateStatus();
		updateTemperature();
	}
	public void updateStatus() {
		for(int r = 0; r < height; r++) {
			for(int c = 0; c < width; c++) {
				Tree t = getObjectAt(r, c);
				if(t == null) continue;
				
				int status = t.getStatus();
				double temperature = getTempAt(r, c);
				double energy = t.getStoredEnergy();
				
				if(status == Tree.LIVING && temperature >= TEMP_IGNITION) {
					forest[r][c].setStatus(Tree.ON_FIRE);
					numTrees[Tree.LIVING]--;
					numTrees[Tree.ON_FIRE]++;
				}
				if(status == Tree.ON_FIRE && temperature <= TEMP_IGNITION && energy <= 0) {
					forest[r][c].setStatus(Tree.ASH);
					numTrees[Tree.ON_FIRE]--;
					numTrees[Tree.ASH]++;
				}
			}
		}
	}
	public void updateTemperature() {
		double[][] updatedTemp = temperature;
		
		for(int r = 0; r < height; r++) {
			for(int c = 0; c < width; c++) {
				Tree t = forest[r][c];
				
				if(t != null && t.getStatus() == Tree.ON_FIRE && t.getStoredEnergy() > 0) 
					heatPropagate(r, c, updatedTemp);
				else heatLoss(r, c);
			}
		}		
	}
	public void heatLoss(int r, int c) {
		if(forest[r][c] != null && forest[r][c].getStatus() == Tree.ON_FIRE && forest[r][c].getStoredEnergy() > 0)
			temperature[r][c] -= (8*heatSpreadDist) * ( heatDissipation*((heatSpreadDist*(TEMP_IGNITION - TEMP_DEFAULT)) + (TEMP_MAX - TEMP_IGNITION) )) / (TEMP_MAX - TEMP_IGNITION);
		else if(temperature[r][c] - heatDissipation < TEMP_DEFAULT) temperature[r][c] = TEMP_DEFAULT;
		else temperature[r][c] -= heatDissipation;
	}
	public void heatPropagate(int r, int c, double[][] updatedTemp) {
		double heatGainedPerUpdate = (heatLostPerUpdate/ (8 * heatSpreadDist));

		for(int row = r-heatSpreadDist; row <= r+heatSpreadDist; row++) {
			for(int col = c-heatSpreadDist; col <= c+heatSpreadDist; col++) {
				if(!inBounds(row, col) || (row == r && col == c)) continue;

				int distFromFire = Math.max(Math.abs(r-row), Math.abs(c-col));
				double gainedHeat = (heatGainedPerUpdate / distFromFire);
				
				if(updatedTemp[row][col] + gainedHeat > TEMP_MAX)
					updatedTemp[row][col] = TEMP_MAX;
				else updatedTemp[row][col] += gainedHeat;
			}
		}
		forest[r][c].setStoredEnergy(forest[r][c].getStoredEnergy() - TEMP_MAX / (TEMP_MAX - TEMP_IGNITION));
		temperature = updatedTemp;
	}
		
	// Helper Methods
	public boolean inBounds(int r, int c) {
		if(r >= 0 && r < height && c >= 0 && c < width) return true;
		return false;
	}
	public double getTempAt(int r, int c) {
		if(!inBounds(r, c)) return -1;
		return temperature[r][c];
	}
	public Tree getObjectAt(int r, int c) {
		if(!inBounds(r, c)) return null;
		return forest[r][c];
	}

	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public Tree[][] getForest() {
		return forest;
	}
	public double[][] getTemperature() {
		return temperature;
	}

	public double getPercentBurned() {
		if(getNumTrees() == 0) return 0;
		return (double)getNumTreesStatus(0) / getNumTrees();
	}
	public int getNumTreesStatus(int status) {
		return numTrees[status];
	}
	public int getNumTrees() {
		int total = 0;
		
		for(int trees : numTrees)
			total += trees;
		
		return total;
	}

}
