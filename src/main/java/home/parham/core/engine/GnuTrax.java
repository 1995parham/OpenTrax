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

import home.parham.core.domain.TraxBoard;
import home.parham.core.domain.TraxStatus;
import home.parham.core.exceptions.IllegalMoveException;

public class GnuTrax {

	private static GnuTrax instance = null;

	public static GnuTrax getInstance(){
		if (instance == null)
			instance = new GnuTrax();
		return instance;
	}

	private boolean analyzeMode, autodisplay, logv, learning;
	private boolean ponder, alarmv;
	private int noise, display, searchDepth, searchTime;
	private TraxStatus computerColour;
	private String playerName;
	private TraxBoard traxBoard;

	private GnuTrax(){
		noise = 100;
		autodisplay = true;
		alarmv = true;
		logv = false;
		display = 1;
		computerColour = TraxStatus.BLACK;
		learning = true;
		searchDepth = 4;
		searchTime = 180;
		analyzeMode = false;
		playerName = "";
		ponder = true;
		traxBoard = new TraxBoard();
	}


	public boolean isAnalyzeMode(){
		return analyzeMode;
	}

	public void setAnalyzeMode(boolean analyzeMode){
		this.analyzeMode = analyzeMode;
	}

	public boolean isAutodisplay(){
		return autodisplay;
	}

	public void setAutodisplay(boolean autodisplay){
		this.autodisplay = autodisplay;
	}

	public boolean isLogv(){
		return logv;
	}

	public void setLogv(boolean logv){
		this.logv = logv;
	}

	public boolean isLearning(){
		return learning;
	}

	public void setLearning(boolean learning){
		this.learning = learning;
	}

	public boolean isPonder(){
		return ponder;
	}

	public void setPonder(boolean ponder){
		this.ponder = ponder;
	}

	public boolean isAlarmv(){
		return alarmv;
	}

	public void setAlarmv(boolean alarmv){
		this.alarmv = alarmv;
	}

	public int getNoise(){
		return noise;
	}

	public void setNoise(int noise){
		this.noise = noise;
	}

	public int getDisplay(){
		return display;
	}

	public void setDisplay(int display){
		this.display = display;
	}

	public int getSearchDepth(){
		return searchDepth;
	}

	public void setSearchDepth(int searchDepth){
		this.searchDepth = searchDepth;
	}

	public int getSearchTime(){
		return searchTime;
	}

	public void setSearchTime(int searchTime){
		this.searchTime = searchTime;
	}

	public TraxStatus getComputerColour(){
		return computerColour;
	}

	public void setComputerColour(TraxStatus computerColour){
		this.computerColour = computerColour;
	}

	public String getPlayerName(){
		return playerName;
	}

	public void setPlayerName(String playerName){
		this.playerName = playerName;
	}

	public TraxBoard getTraxBoard(){
		return traxBoard;
	}

	public void setTraxBoard(TraxBoard traxBoard){
		this.traxBoard = traxBoard;
	}

	private void checkForWin(){
		TraxStatus gameValue;

		gameValue = traxBoard.isGameOver();
		if (gameValue == TraxStatus.NOPLAYER)
			return;
		System.out.print("Game over. The result is ");
		switch (gameValue) {
			case DRAW:
				System.out.println("Draw.");
				break;
			case WHITE:
				System.out.println("White won.");
				break;
			case BLACK:
				System.out.println("Black won.");
				break;
		}
	}


	public void gotAMove(String theMove){
		try {
			traxBoard.makeMove(theMove);
		} catch (IllegalMoveException e) {
			System.out.println(theMove + ":  " + e);
			return;
		}
		System.out.println(traxBoard);
		checkForWin();
	}

}
