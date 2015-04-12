/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax
 *
 * [] Package Name : main.cli
 *
 * [] Creation Date : 11-02-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/
package home.parham.trax.core.engine;

import home.parham.trax.core.domain.TraxBoard;
import home.parham.trax.core.domain.TraxMove;
import home.parham.trax.core.domain.TraxStatus;
import home.parham.trax.core.exceptions.IllegalMoveException;

public class GnuTrax {

	private static GnuTrax instance = null;

	public static GnuTrax getInstance(){
		if (instance == null)
			instance = new GnuTrax();
		return instance;
	}

	private TraxBoard traxBoard;
	private TraxStatus secondPlayerStatus;


	private GnuTrax(){
		traxBoard = new TraxBoard();
	}

	public TraxBoard getTraxBoard(){
		return traxBoard;
	}

	public void setTraxBoard(TraxBoard traxBoard){
		this.traxBoard = traxBoard;
	}

	public TraxStatus getSecondPlayerStatus(){
		return secondPlayerStatus;
	}

	public void setSecondPlayerStatus(TraxStatus secondPlayerStatus){
		this.secondPlayerStatus = secondPlayerStatus;
	}

	public void gotAMove(String theMove){
		try {
			traxBoard.makeMove(new TraxMove(theMove));
			System.err.println(theMove);
		} catch (IllegalMoveException e) {
			System.err.println("[ " + e.getMove() + "]" + " : " + e.getMessage());
		}
	}

}
