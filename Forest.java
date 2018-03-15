
public class Forest {
	private Tree[][] forest;
	private int height, width;
	
	public Forest(int r, int c) {
		height = r;
		width = c;
		forest = new Tree[height][width];
	}

	public void propogateFire(Forest f, int r, int c) {
		this.forest[r][c].setStatus(0);
		
		for(int row = r-1; row <= r+1; row++) {
			for(int col = c-1; col <= c+1; col++) {
				if(inBounds(row, col)) {
					Tree t = f.forest[row][col];

					if(t != null && t.getStatus() == 2)
						this.forest[row][col].setStatus(1);
					
				}
			}
		}
	}
	
	public void initializeForest(double treeDensity) {
		int numTrees = (int)(height*width*treeDensity);
		
		for(int i = 0; i < numTrees; i++) {
			int randY = (int)(Math.random()*height);
			int randX = (int)(Math.random()*width);
			
			if(getObjectAt(randY, randX) == null) {
				Tree t = new Tree();
				
				if(i == numTrees - 1)
					t.setStatus(1);
				
				forest[randY][randX] = t;
			} else i--;
		}
	}

	public int getNumTreesStatus(int status) {
		int count = 0;
		
		for(int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				Tree t = forest[row][col];
				
				if(t != null && t.getStatus() == status)
					count++;
			}
		}
		
		return count;
	}
	public int getNumTrees() {
		int count = 0;
		
		for(int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				Tree t = forest[row][col];
				
				if(t != null)
					count++;
			}
		}
		
		return count;
	}
	public double percentBurned() {
		if(getNumTrees() == 0) return 0;
		return (double)getNumTreesStatus(0) / getNumTrees();
	}
	
	public boolean inBounds(int r, int c) {
		if(r >= 0 && r < height && c >= 0 && c < width) return true;
		return false;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public Tree getObjectAt(int r, int c) {
		return forest[r][c];
	}
	public Tree[][] getForest() {
		return forest;
	}
}
