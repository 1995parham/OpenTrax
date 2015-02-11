/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax
 *
 * [] Package Name : home.parham.main.cli
 *
 * [] Creation Date : 11-02-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/
package home.parham.main.cli;

import home.parham.main.domain.TraxBoard;
import home.parham.main.domain.TraxStatus;
import home.parham.main.exceptions.IllegalMoveException;
import home.parham.main.player.Player;
import home.parham.main.player.PlayerSimple;
import home.parham.main.player.PlayerUct;
import home.parham.main.util.TraxUtil;

import java.util.ArrayList;

public class GnuTrax {

	private static GnuTrax instance = null;

	public static GnuTrax getInstance(){
		if (instance == null)
			instance = new GnuTrax("simple");
		return instance;
	}

	private boolean analyzeMode, autodisplay, logv, learning;
	private boolean ponder, alarmv;
	private int noise, display, searchDepth, searchTime;
	private TraxStatus computerColour;
	private String playerName;
	private TraxBoard traxBoard;
	private Player player;


	private GnuTrax(String computerAlgorithm){
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
		if (computerAlgorithm.equals("simple")) {
			player = new PlayerSimple();
		}
		if (computerAlgorithm.equals("uct")) {
			player = new PlayerUct();
		} else {
			player = new PlayerUct();
		}
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

	public Player getPlayer(){
		return player;
	}

	public void setPlayer(Player player){
		this.player = player;
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


	public static void main(String[] args){
		Commands.welcome();
		GnuTrax.getInstance().run();
	}

	private void run(){
		ArrayList<String> command = new ArrayList<String>();
		while (true) {
			command.clear();
			if (traxBoard.whoToMove() == TraxStatus.WHITE)
				System.out.print("White");
			else
				System.out.print("Black");

			System.out.print("():");

			if ((traxBoard.isGameOver() == TraxStatus.NOPLAYER)
					&& (traxBoard.whoToMove() == computerColour)) {
				/* the player must give a move */
				System.out.println("Thinking ...");
				command.add(player.move(traxBoard));
				System.out.println(command.get(0));
			} else if ((traxBoard.whoToMove() != computerColour)
					|| (traxBoard.isGameOver() != TraxStatus.NOPLAYER)) {
				/* the human must give a move or a command */
				command = TraxUtil.getInput();
				if (command.size() == 0)
					continue;        /* read more input */
			}
			CommandDispatcher.dispatch(command);
		}
	}
}
