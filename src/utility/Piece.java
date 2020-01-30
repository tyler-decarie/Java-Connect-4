package utility;

import javafx.scene.shape.Circle;

public class Piece extends Circle{
	
	int xCord;
	int yCord;
	
	public Piece() {
		
	}
	public Piece(int x, int y) {
		this.xCord = x;
		this.yCord = y;
	}
	public int getxCord() {
		return xCord;
	}
	public void setxCord(int xCord) {
		this.xCord = xCord;
	}
	public int getyCord() {
		return yCord;
	}
	public void setyCord(int yCord) {
		this.yCord = yCord;
	}
	
}
