package updated;

public class Tree {
	public static final int ASH = 0;
	public static final int ON_FIRE = 1;
	public static final int LIVING = 2;
	
	public static final double DEFAULT_ENERGY = Forest.TEMP_MAX;
	
	private int status;
	private double storedEnergy;
	
	public Tree(int status, double storedEnergy) {
		this.status = status;
		this.storedEnergy = storedEnergy;
	}
 	public Tree(int status) {
 		this.status = status;
 		
 		switch(status) {
 			case ASH: case ON_FIRE:
 				storedEnergy = 0;
 			case LIVING:
 				storedEnergy = DEFAULT_ENERGY;
 		}
 	}
	public Tree() {
		this(2);
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public double getStoredEnergy() {
		return storedEnergy;
	}
	public void setStoredEnergy(double storedEnergy) {
		this.storedEnergy = storedEnergy;
	}
}
