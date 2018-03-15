package updated;

import processing.core.*;

public class GUI extends PApplet {
	int height, width, numRows, numCols;
	boolean initializer;
	long startTime, endTime;
	Forest forest;
	Display display;

	public void setup() {
		height = 800;
		width = 800*2;
		numRows = 100;
		numCols = 100;
		int gapSize = 10;
		
		size(width + 3*gapSize, height + 2*gapSize);
		display = new Display(this, gapSize, gapSize, width, height);

		display.setup(numRows, numCols);
		
		display.setColorTree(Tree.ON_FIRE, color(225, 0, 0)); 
		display.setColorTree(Tree.ASH, color(12, 14, 1));
		display.setColorTree(Tree.LIVING, color(47, 225, 0));
		
		for(int i = 0; i < 256; i++) {
			display.setColorTemp(i, color(i, 0, 0));
		}
		
		// Forest(int r, int c, int heatSpreadDist, double heatLostPerUpdate, double heatDissipation)
		// forest = new Forest(numRows, numCols);
		initializer = true;
		forest = new Forest(numRows, numCols, 2, 1200, 30);
		forest.initialize(0);
		startTime = System.nanoTime();
	}

	@Override
	public void draw() {
		background(200);

		display.draw(forest);
		forest.update();
		
		if(forest.getNumTreesStatus(Tree.ON_FIRE) == 0) {
			if(initializer != true) {
				endTime = System.nanoTime();
				long duration = (endTime - startTime) / 1000000000;
			
				System.out.println("Time: " + duration + " seconds");
				System.out.println("Percent Burned: " + (int)(forest.getPercentBurned()*100) + "%");
				System.out.println("=====");
			} else initializer = false;
			
			double density = (Math.random()*10 + 10) / 100.0;
//			double density = (Math.random()*Math.random());
			System.out.println("Density: " + density);
			forest.reset(density);
			
			startTime = System.nanoTime();
		}
		
	}
}