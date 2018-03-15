import processing.core.*;

public class GUI extends PApplet {
	int c, count = 0;
	double treeDensity = 0.01;
	Simulator sim;
	Display display;

	public void setup() {
		size(640, 550); // set the size of the screen.

		// Create a simulator
		sim = new Simulator(100, 100, 0.01);		
		
		// Create the display
		// parameters: (10,10) is upper left of display
		// (620, 530) is the width and height
		display = new Display(this, 10, 10, 620, 530);

		display.setNumCols(100);		// NOTE:  these must match your simulator!!
		display.setNumRows(100);
	
		// Set different grid values to different colors
		// TODO:  uncomment these lines!
		
		display.setColor(Tree.ON_FIRE, color(255, 0, 0)); 
		display.setColor(Tree.ASH, color(125, 125, 125));
		display.setColor(Tree.LIVING, color(0, 200, 0));

		// You can use classes instead, for example:
		// display.setColor(Tree.class, color(0, 255, 0));
		// display.setColor(Ash.class, color(0, 255, 0));
		
	}

	@Override
	public void draw() {
		background(200);
		
		sim.doOneStep();
		display.drawForest(sim.getForest());

		if(sim.getForest().getNumTreesStatus(1) == 0) {
			count++;
			
			if(count > 100) {
				sim.reset(treeDensity);
				treeDensity += 0.01;
				count = 0;
			}
			
			System.out.println("Count: " + count + ", Tree Density:" + treeDensity);
		}
	}
}