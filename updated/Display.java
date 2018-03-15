package updated;
import java.util.LinkedHashMap;
import java.util.Map;

import processing.core.*;

public class Display {
	// Colors used for empty locations.
	private static final int EMPTY_COLOR = 0xFFFFFFFF;

	// Color used for objects that have no defined color.
	private static final int UNKNOWN_COLOR = 0x66666666;

	private PApplet p; // the applet we want to display on

	private int x, y, w, h; // (x, y) of upper left corner of display
	// the width and height of the display

	private float dx, dy; // calculate the width and height of each box
	// in the field display using the size of the field
	// and the width and height of the display

	private int rows, cols;

	// A map for storing colors for participants in the simulation
	private Map<Object, Integer> colorsTree;
	private Map<Object, Integer> colorsTemp;
	private Map<Object, PImage> images;

	// (x, y) is the upper-left corner of the display in pixels
	// w and h are the width and height of the display in pixels
	public Display(PApplet p, int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.p = p;

		colorsTree = new LinkedHashMap<Object, Integer>();
		colorsTemp = new LinkedHashMap<Object, Integer>();
		images = new LinkedHashMap<Object, PImage>();
	}

//	public void drawField(Forest f) {
//		Object thing;
//		Integer displayColor;
//		for (int i = 0; i < f.getWidth(); i++) {
//			for (int j = 0; j < f.getHeight(); j++) {
//				thing = f.getObjectAt(i, j);
//				if (thing != null) {
//					displayColor = getColor(thing.getClass());
//					p.fill(displayColor);
//				} else {
//					p.fill(this.EMPTY_COLOR);
//				}
//				p.rect(x + i * dx, y + j * dy, dx, dy);
//			}
//		}
//	}
	
	public void draw(Forest forest) {
		drawForest(forest);
		drawTemperature(forest);
	}

	public void drawForest(Forest forest) {
		Tree[][] f = forest.getForest();

		int numcols = f[0].length;
		int numrows = f.length;

		for (int i = 0; i < numrows; i++) {
			for (int j = 0; j < numcols; j++) {
				Tree tree = f[i][j];
				int pieceColor = 0;

				if (tree == null) {
					pieceColor = this.EMPTY_COLOR;
				} else {
					int treestate = tree.getStatus();
					pieceColor = getColorTree(treestate);
				}
				
				p.fill(pieceColor);
				p.rect(x + j * dx, y + i * dy, dx, dy);

			}
		}
	}
	
	public void drawTemperature(Forest forest) {
		double[][] t = forest.getTemperature();
		double defaultTemp = Forest.TEMP_DEFAULT;
		double maxTemp = Forest.TEMP_MAX;
		double factor = 255.0 / (maxTemp - defaultTemp);

		int numRows = t.length;
		int numCols = t[0].length;
		
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numCols; c++) {
				double temp = t[r][c];
				int pieceColor;
				
				if(temp <= defaultTemp)
					pieceColor = 0;
				else {
					int tempProportion = (int)(factor * (temp - defaultTemp));
					pieceColor = getColorTemp(tempProportion);
				}
				
				p.fill(pieceColor);
				p.rect((x + w/2 + 10) + c * dx, y + r * dy, dx, dy);
			}
		}		
	}

	/**
	 * Define a color to be used for a given value in the grid.
	 * 
	 * @param pieceType
	 *          The type of piece in the grid.
	 * @param color
	 *          The color to be used for the given type of piece.
	 */
	public void setColorTree(int pieceType, Integer color) {
		colorsTree.put(pieceType, color);
	}
	public void setColorTemp(int pieceType, Integer color) {
		colorsTemp.put(pieceType, color);
	}
	
	/**
	 * Define an Image to be used for a given value in the grid.
	 * 
	 * @param pieceType
	 *          The type of piece in the grid.
	 * @param img
	 *          The image to be used for the given type of piece.
	 */
	public void setImage(Object pieceType, PImage img) {
		images.put(pieceType, img);
	}

	/**
	 * Define a color to be used for a given value in the grid.
	 * 
	 * @param pieceType
	 *          The type of piece in the grid.
	 * @param filename
	 *          The file path to the image to be used for the given type of piece.
	 */
	public void setImage(Object pieceType, String filename) {
		PImage img = p.loadImage(filename);
		setImage(pieceType, img);
	}

	/**
	 * @return The color to be used for a given class of animal.
	 */
	private Integer getColorTree(Object pieceType) {
		Integer col = colorsTree.get(pieceType);
		if (col == null) { // no color defined for this class
			return UNKNOWN_COLOR;
		} else {
			return col;
		}
	}
	private Integer getColorTemp(Object pieceType) {
		Integer col = colorsTemp.get(pieceType);
		if (col == null) { // no color defined for this class
			return UNKNOWN_COLOR;
		} else {
			return col;
		}
	}
	
	private PImage getImage(Object pieceType) {
		PImage img = images.get(pieceType);
		return img;
	}

	// return the y pixel value of the upper-left corner of location l
	private float yCoordOf(Location l) {
		return y + l.getRow() * dy;
	}

	// return the x pixel value of the upper-left corner of location l
	private float xCoordOf(Location l) {
		return x + l.getCol() * dx;
	}

	// Return location at coordinates x, y on the screen
	public Location gridLocationAt(float mousex, float mousey) {
		Location l = new Location((int) Math.floor((mousey - y) / dy),
				(int) Math.floor((mousex - x) / dx));
		return l;
	}

	// Return whether (x, y) is over the board or not
	public boolean overBoard(float mx, float my) {
		return (mx >= x && mx <= x + w && my > y && my < y + h);
	}

	public void setNumCols(int numCols) {
		rows = numCols;
		dx = w / 2 / rows;
	}

	public void setNumRows(int numRows) {
		cols = numRows;
		dy = h / cols;
	}

	public void setup(int numRows, int numCols) {
		setNumRows(numRows);
		setNumCols(numCols);
	}
}