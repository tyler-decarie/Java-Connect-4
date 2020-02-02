package utility;

import java.io.Serializable;

import javafx.scene.shape.Circle;

public class Piece extends Circle implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int row;
	int column;
	
	public Piece() {
		
	}
	public Piece(int x, int y) {
		this.row = y;
		this.column = x;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	@Override
	public String toString() {
		return "Piece: xCord=" + column + ", yCord=" + row;
	}
	
}
