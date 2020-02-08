package utility;

import java.io.Serializable;

import javafx.scene.shape.Circle;

/**
 * 
 * @author Tyler Decarie, Travis Brady
 * 
 * A class to create a Piece object to be placed on the board and transported across the network
 * Every Piece is a Circle, and has a x coordinate and y coordinate
 */
public class Piece extends Circle implements Serializable{
	
	//Attributes
	private static final long serialVersionUID = 1L;
	int row; //row the piece is in
	int column; //column the piece is in
	
	//Constructors
	public Piece() {
		
	}
	
	/** Constructor for Piece
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 */
	public Piece(int x, int y) {
		this.row = y;
		this.column = x;
	}
	
	/**
	 * 
	 * @return column coordinate of a piece
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * 
	 * @param column sets the column of a piece
	 */
	public void setColumn(int column) {
		this.column = column;
	}
	
	/**
	 * 
	 * @return the row coordinate of a piece
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * 
	 * @param row sets the row coordinate of a piece
	 */
	public void setRow(int row) {
		this.row = row;
	}
	
	@Override
	public String toString() {
		return "Piece: xCord=" + column + ", yCord=" + row;
	}
	
}
