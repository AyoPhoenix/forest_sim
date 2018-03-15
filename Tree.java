
public class Tree {
	public static final int ASH = 0;
	public static final int ON_FIRE = 1;
	public static final int LIVING = 2;
	
	private int status;
	
 	public Tree(int status) {
 		this.status = status;
 	}
	public Tree() {
		this.status = 2;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
