/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax 
 * 
 * [] Package Name : home.parham.cli
 *
 * [] Creation Date : 11-02-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/

package home.parham.cli;

import home.parham.core.domain.TraxBoard;
import home.parham.core.domain.TraxStatus;
import home.parham.core.engine.GnuTrax;
import home.parham.core.player.Player;
import home.parham.core.player.PlayerSimple;
import home.parham.core.util.TraxUtil;

import java.util.ArrayList;

public class TraxCLI implements Runnable {
	@Override
	public void run(){
		TraxBoard traxBoard = GnuTrax.getInstance().getTraxBoard();
		TraxStatus computerColour = GnuTrax.getInstance().getSecondPlayerStatus();
		Player player = new PlayerSimple();

		ArrayList<String> command = new ArrayList<String>();

		while (true) {
			command.clear();

			System.out.println(BoardViewer.view(traxBoard));

			if (traxBoard.isGameOver() == TraxStatus.WHITE)
				System.out.println("While is won");
			if (traxBoard.isGameOver() == TraxStatus.BLACK)
				System.out.println("Black is won");
			if (traxBoard.isGameOver() == TraxStatus.DRAW)
				System.out.println("Draw");

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
