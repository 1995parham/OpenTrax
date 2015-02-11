/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax 
 * 
 * [] Package Name : home.parham.main.util
 *
 * [] Creation Date : 11-02-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/

package home.parham.main.util;

import home.parham.main.TraxBoard;
import home.parham.main.exceptions.IllegalMoveException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

public final class TraxUtil {

	static boolean LOG = false;
	static Random randomGenerator = new Random();

	public static int getRandom(int limit){
		return randomGenerator.nextInt(limit);
	}

	public static void startLog(){
		LOG = true;
	}

	public static void stopLog(){
		LOG = false;
	}

	public static void log(String msg){
		if (LOG) {
			System.err.println(msg);
		}
	}

	public static String getRandomMove(TraxBoard traxBoard) throws IllegalMoveException{
		int losingMoves = 0;
		if (traxBoard.isGameOver() != TraxBoard.NOPLAYER) {
			return new String();
		}
		ArrayList<String> moves = traxBoard.uniqueMoves();

		if (moves.size() == 1) {
			return moves.get(0);
		}
		ArrayList<String> moves_not_losing = new ArrayList<String>(moves.size());

		for (String move1 : moves) {
			TraxBoard t_copy = new TraxBoard(traxBoard);
			t_copy.makeMove(move1);
			int gameOverValue = t_copy.isGameOver();
			switch (gameOverValue) {
				case TraxBoard.WHITE:
				case TraxBoard.BLACK:
					if (t_copy.whoDidLastMove() == gameOverValue) {
						log("Winning move found");
						return move1;	/* Winning move found */
					}
		/* losing move found */
					losingMoves++;
					log("Losing move found");
					break;
				case TraxBoard.NOPLAYER:
				case TraxBoard.DRAW:
					moves_not_losing.add(move1);
					log("Not losing move found");
					log(moves_not_losing.toString());
					break;
			}
		}
		if (moves_not_losing.size() == 0) {
		/* Only losing moves left */
			log("Only losing moves left");
			return moves.get(0);
		}
		return moves_not_losing.get(randomGenerator.nextInt(moves_not_losing.size()));
	}

	public static ArrayList<String> getInput(InputStream inputStream){
		ArrayList<String> result = new ArrayList<String>(5);
		String line;
		try {
			line = new BufferedReader(new InputStreamReader(inputStream)).readLine();
			if (line != null) {
				StringTokenizer st = new StringTokenizer(line);
				while (st.hasMoreTokens())
					result.add(st.nextToken());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static ArrayList<String> getInput(){
		return getInput(System.in);
	}

	public static String reverseBorder(String border){
		StringBuffer result = new StringBuffer(border.length());
		for (int i = 0; i < border.length(); i++) {
			switch (border.charAt(i)) {
				case 'W':
					result.append('B');
					break;
				case 'B':
					result.append('W');
					break;
				case '+':
					result.append('+');
					break;
				case '-':
					result.append('-');
					break;
				default:
					// This should never happen
					throw new IllegalArgumentException("This should never happen (032).");
			}
		}
		return result.toString();
	}

	public static void main(String[] args){
		startLog();

		TraxBoard tb = new TraxBoard();
		String move;
		try {
			tb.makeMove("a1c");
			tb.makeMove("b1d");
			move = getRandomMove(tb);
			System.out.println(tb + move);
			tb.makeMove(move);
			System.out.println(tb);
			tb = new TraxBoard();
			tb.makeMove("a1c");
			tb.makeMove("a1u");
			System.out.println(tb.getBorder());
			System.out.println(reverseBorder(tb.getBorder()));
			move = getRandomMove(tb);
			System.out.println(tb + move);
			tb.makeMove(move);
			System.out.println(tb);
			System.out.println(tb.getBorder());
			System.out.println(reverseBorder(tb.getBorder()));
		} catch (IllegalMoveException e) {
			throw new AssertionError(e.getMessage());
		}
	}
}

