/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax
 *
 * [] Package Name : home.parham.main.main.cli
 *
 * [] Creation Date : 11-02-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/
package home.parham.core.engine;

import home.parham.cli.BoardViewer;
import home.parham.core.domain.TraxBoard;
import home.parham.core.domain.TraxMove;
import home.parham.core.domain.TraxStatus;
import home.parham.core.exceptions.IllegalMoveException;

public class GnuTrax {

	private static GnuTrax instance = null;

	public static GnuTrax getInstance(){
		if (instance == null)
			instance = new GnuTrax();
		return instance;
	}

	private TraxStatus computerColour;
	private TraxBoard traxBoard;

	private GnuTrax(){
		computerColour = TraxStatus.BLACK;
		traxBoard = new TraxBoard();
	}


	public TraxStatus getComputerColour(){
		return computerColour;
	}

	public void setComputerColour(TraxStatus computerColour){
		this.computerColour = computerColour;
	}

	public TraxBoard getTraxBoard(){
		return traxBoard;
	}

	public void setTraxBoard(TraxBoard traxBoard){
		this.traxBoard = traxBoard;
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
