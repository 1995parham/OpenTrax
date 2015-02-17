package home.parham.core.player;

/* 
 Date: 18th of September 2009
 version 0.1
 All source under GPL version 2 
 (GNU General Public License - http://www.gnu.org/)
 contact traxplayer@gmail.com for more information about this code
 */

import home.parham.core.domain.TraxBoard;
import home.parham.core.domain.TraxMove;
import home.parham.core.domain.TraxStatus;
import home.parham.core.exceptions.IllegalMoveException;
import java.util.ArrayList;
import java.util.Random;

public class PlayerSimple implements Player {
	public String move(TraxBoard tb){
		String move = null;
		try {
			move = getRandomMove(tb);
		} catch (IllegalMoveException e) {
			System.err.println("[" + e.getMove() + "] : " + e.getMessage());
		}
		return move;
	}

	public static String getRandomMove(TraxBoard traxBoard) throws IllegalMoveException{
		Random randomGenerator = new Random();
		if (traxBoard.isGameOver() != TraxStatus.NOPLAYER) {
			return "";
		}
		ArrayList<String> moves = traxBoard.uniqueMoves();

		if (moves.size() == 1) {
			return moves.get(0);
		}
		ArrayList<String> moves_not_losing = new ArrayList<String>(moves.size());

		for (String move1 : moves) {
			TraxBoard t_copy = new TraxBoard(traxBoard);
			t_copy.makeMove(new TraxMove(move1));
			TraxStatus gameOverValue = t_copy.isGameOver();
			switch (gameOverValue) {
				case WHITE:
				case BLACK:
					if (t_copy.whoDidLastMove() == gameOverValue) {
						return move1;	/* Winning move found */
					}
					break;
				case NOPLAYER:
				case DRAW:
					moves_not_losing.add(move1);
					break;
			}
		}
		if (moves_not_losing.size() == 0) {
			return moves.get(0);
		}
		return moves_not_losing.get(randomGenerator.nextInt(moves_not_losing.size()));
	}
}
