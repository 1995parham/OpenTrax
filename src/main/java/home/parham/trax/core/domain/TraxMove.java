/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax 
 * 
 * [] Package Name : home.parham.core.domain
 *
 * [] Creation Date : 17-02-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/

package home.parham.trax.core.domain;

import home.parham.trax.core.exceptions.IllegalMoveException;

public class TraxMove {
	private String move;
	private int column;
	private int row;
	private char tile;

	public TraxMove(String move){
		if (move.length() >= 3) {
			this.move = move.toUpperCase();

			int columnIndex = 0;
			this.column = move.charAt(columnIndex) - '@';
			columnIndex++;
			while (Character.isLetter(move.charAt(columnIndex))) {
				this.column = this.column * 26 + move.charAt(columnIndex) - 'A';
				columnIndex++;
			}
			int rowIndex = columnIndex;
			this.row = 0;
			while (Character.isDigit(move.charAt(rowIndex))) {
				rowIndex++;
			}
			this.row = Integer.valueOf(move.substring(columnIndex, rowIndex));
			this.tile = move.charAt(rowIndex);
		} else {
			this.move = move;
			this.column = -1;
			this.row = -1;
			this.tile = '-';
		}
	}

	public String getMove(){
		return move;
	}

	public int getColumn(){
		return column;
	}

	public int getRow(){
		return row;
	}

	public char getTile(){
		return tile;
	}

	@Override
	public String toString(){
		return move;
	}

	public boolean equals(String obj){
		return move.equalsIgnoreCase(obj);
	}

	public void validate() throws IllegalMoveException{
		if (move.length() < 3)
			throw new IllegalMoveException("Not a move.", this);

		switch (tile) {
			case '/':
			case '+':
			case '\\':
				break;
			default:
				throw new IllegalMoveException("Unknown tile.", this);
		}
	}
}
