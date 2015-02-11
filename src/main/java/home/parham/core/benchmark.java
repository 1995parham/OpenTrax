package home.parham.core;

/* 

 Date: 1th of Januar 2009
 version 0.1
 All source under GPL version 2 
 (GNU General Public License - http://www.gnu.org/)
 contact traxplayer@gmail.com for more information about this code

 */

import home.parham.core.domain.TraxBoard;
import home.parham.core.domain.TraxStatus;
import home.parham.core.exceptions.IllegalMoveException;
import home.parham.core.util.TraxUtil;

import java.util.ArrayList;
import java.util.Random;

public class benchmark {

	public static void main(String[] args){
		TraxBoard tb;
		TraxStatus game_value = TraxStatus.NOPLAYER; // To make the compiler happy
		ArrayList<String> move_list;
		String random_move;
		int draw = 0, p1 = 0, p2 = 0;
		int game_length;
		final int num_of_games = 10000;
		int r, i;
		Random random_generator = new Random();

		game_length = 0;
		for (i = 0; i < num_of_games; i++) {
			try {
				tb = new TraxBoard();
				do {
					random_move = TraxUtil.getRandomMove(tb);
					tb.makeMove(random_move);
					game_value = tb.isGameOver();
					game_length++;
				} while (game_value == TraxStatus.NOPLAYER);
			} catch (IllegalMoveException e) {
				e.printStackTrace();
				return;
			}
			switch (game_value) {
				case DRAW:
					draw++;
					break;
				case WHITE:
					p1++;
					break;
				case BLACK:
					p2++;
					break;
				default:
			}
		}

		System.out.println("p1=" + p1 + " p2=" + p2 + " draw=" + draw);
		System.out.println("(p1-p2)*100/total="
				+ (float) ((p1 - p2) * 100 / (p1 + p2 + draw)));
		System.out.println("average game length: " + (float) game_length
				/ num_of_games);
	}
}
