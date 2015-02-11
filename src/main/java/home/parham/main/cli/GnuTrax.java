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

import home.parham.main.TraxBoard;
import home.parham.main.TraxUtil;
import home.parham.main.exceptions.IllegalMoveException;
import home.parham.main.player.ComputerPlayer;
import home.parham.main.player.ComputerPlayerSimple;
import home.parham.main.player.ComputerPlayerUct;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
	private int noise, display, searchDepth, searchTime, computerColour;
	private String playerName;
	private TraxBoard traxBoard;
	private ComputerPlayer computerPlayer;


	private GnuTrax(String computerAlgorithm){
		noise = 100;
		autodisplay = true;
		alarmv = true;
		logv = false;
		display = 1;
		computerColour = TraxBoard.BLACK;
		learning = true;
		searchDepth = 4;
		searchTime = 180;
		analyzeMode = false;
		playerName = "";
		ponder = true;
		traxBoard = new TraxBoard();
		if (computerAlgorithm.equals("simple")) {
			computerPlayer = new ComputerPlayerSimple();
		}
		if (computerAlgorithm.equals("uct")) {
			computerPlayer = new ComputerPlayerUct();
		} else {
			computerPlayer = new ComputerPlayerUct();
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

	public int getComputerColour(){
		return computerColour;
	}

	public void setComputerColour(int computerColour){
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

	public ComputerPlayer getComputerPlayer(){
		return computerPlayer;
	}

	public void setComputerPlayer(ComputerPlayer computerPlayer){
		this.computerPlayer = computerPlayer;
	}

	private void checkForWin(){
		int gameValue;

		gameValue = traxBoard.isGameOver();
		if (gameValue == TraxBoard.NOPLAYER)
			return;
		System.out.print("Game over. The result is ");
		switch (gameValue) {
			case TraxBoard.DRAW:
				System.out.println("Draw.");
				break;
			case TraxBoard.WHITE:
				System.out.println("White won.");
				break;
			case TraxBoard.BLACK:
				System.out.println("Black won.");
				break;
			default:
				/* This should never happen */
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


	private void pbem(){
		try {
			String s;
			TraxBoard tb = new TraxBoard();
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while ((s = reader.readLine()) != null) {
				for (String move : s.split("\\s")) {
					try {
						tb.makeMove(move);
					} catch (IllegalMoveException e) {
						System.err.println("Illegal move: " + move);
						System.exit(1);
					}
				}
			}
			System.out.println(computerPlayer.computerMove(tb));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args){
		Commands.welcome();
		GnuTrax.getInstance().run();
	}

	private void run(){
		ArrayList<String> command = new ArrayList<String>();
		while (true) {
			
			if (traxBoard.whoToMove() == TraxBoard.WHITE)
				System.out.print("White");
			else
				System.out.print("Black");
			
			System.out.print("():");
			
			if ((traxBoard.isGameOver() == TraxBoard.NOPLAYER)
					&& (traxBoard.whoToMove() == computerColour)) {
				/* the computer must give a move */
				System.out.println("Thinking ...");
				command.add(computerPlayer.computerMove(traxBoard));
				System.out.println(command.get(0));
			}
			else if ((traxBoard.whoToMove() != computerColour)
					|| (traxBoard.isGameOver() != TraxBoard.NOPLAYER)) {
				/* the human must give a move or a command */
				command = TraxUtil.getInput();
				if (command.size() == 0)
					continue;        /* read more input */
			}
			CommandDispatcher.dispatch(command);
		}
	}
}
