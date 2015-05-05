/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax 
 * 
 * [] Package Name : home.parham.core.player
 *
 * [] Creation Date : 03-03-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/

package home.parham.trax.core.player;

import home.parham.trax.core.domain.TraxBoard;
import home.parham.trax.core.domain.TraxMove;
import home.parham.trax.core.domain.TraxStatus;
import home.parham.trax.core.exceptions.IllegalMoveException;

import java.util.ArrayList;
import java.util.Random;

public class PlayerSimple implements Player {
	private TraxBoard tb;

	public PlayerSimple() {
		tb = new TraxBoard();
	}


	public String move(String otherPlayerMove) {
		String move = null;
		try {
			if (!otherPlayerMove.equals(""))
				tb.makeMove(new TraxMove(otherPlayerMove));
			move = getRandomMove(tb);
			tb.makeMove(new TraxMove(move));
		} catch (IllegalMoveException e) {
			System.err.println(this.getClass().getName() + " : " + "[" + e.getMove() + "] : " + e.getMessage());
		}
		return move;
	}

	public String getName() {
		return "AI";
	}

	public static String getRandomMove(TraxBoard traxBoard) throws IllegalMoveException {
		Random randomGenerator = new Random();
		if (traxBoard.isGameOver() != TraxStatus.NOPLAYER) {
			return "";
		}
		ArrayList<String> moves = traxBoard.uniqueMoves();

		if (moves.size() == 1) {
			return moves.get(0);
		}
		ArrayList<String> moves_not_losing = new ArrayList<String>(moves.size());

		for (String move : moves) {
			TraxBoard t_copy = new TraxBoard(traxBoard);
			t_copy.makeMove(new TraxMove(move));
			TraxStatus gameOverValue = t_copy.isGameOver();
			switch (gameOverValue) {
				case WHITE:
				case BLACK:
					if (t_copy.whoDidLastMove() == gameOverValue) {
						return move;	/* Winning move found */
					}
					break;
				case NOPLAYER:
				case DRAW:
					moves_not_losing.add(move);
					break;
			}
		}
		if (moves_not_losing.size() == 0) {
			return moves.get(0);
		}
		return moves_not_losing.get(randomGenerator.nextInt(moves_not_losing.size()));
	}
}

