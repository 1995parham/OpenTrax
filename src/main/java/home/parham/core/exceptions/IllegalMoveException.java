/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax 
 * 
 * [] Package Name : home.parham.main.main
 *
 * [] Creation Date : 23-01-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/

package home.parham.core.exceptions;


import home.parham.core.domain.TraxMove;

public class IllegalMoveException extends Exception {
	private TraxMove move;

	public IllegalMoveException(String message){
		super(message);
	}

	public IllegalMoveException(String message, TraxMove move){
		super(message);
		this.move = move;
	}

	public static final long serialVersionUID = 24162462L;

	public TraxMove getMove(){
		return move;
	}
}

