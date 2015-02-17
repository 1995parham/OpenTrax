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

package home.parham.core.domain;

import home.parham.core.exceptions.IllegalMoveException;

public class TraxMove {
	private String move;
	private int column;
	private int row;
	private char tile;

	public TraxMove(String move){
		if (move.length() == 3) {
			this.move = move.toUpperCase();
			this.column = move.charAt(0) - '@';
			this.row = move.charAt(1) - '0';
			this.tile = move.charAt(2);
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

	@Override
	public boolean equals(Object obj){
		return move.equals(obj);
	}

	public void validate() throws IllegalMoveException{
		if (move.length() != 3)
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
