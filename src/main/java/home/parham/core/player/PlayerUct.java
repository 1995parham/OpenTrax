/* 
   Date: 9th of July 2014
   version 0.1
   All source under GPL version 2
   (GNU General Public License - http://www.gnu.org/)
   contact traxplayer@gmail.com for more information about this code
*/

package home.parham.core.player;

import home.parham.core.Openingbook;
import home.parham.core.domain.TraxBoard;
import home.parham.core.domain.TraxStatus;
import home.parham.core.exceptions.IllegalMoveException;
import home.parham.core.util.TraxUtil;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerUct implements Player {
	private int maxSimulations;
	private Openingbook book = new Openingbook();

	public PlayerUct(){
		//maxSimulations = 10000;
		this(20000);
	}

	public PlayerUct(int maxSimulations){
		this.maxSimulations = maxSimulations;
		try {
			book.loadBook();
		} catch (IOException e) {
			TraxUtil.log("No games/book.bin found.");
		}
	}

	private String simpleMove(TraxBoard tb){
		int percent = TraxUtil.getRandom(100);

		if (tb.getNumOfTiles() == 0) {
			if (percent < 50) {
				return new String("@0/");
			} else {
				return new String("@0+");
			}
		}
		if (tb.getNumOfTiles() == 1) {
			if (tb.getAt(1, 1) == TraxBoard.NW) {
				if (percent < 15) {
					return new String("B1\\");
				}
				if (percent < 90) {
					return new String("@1/");
				} else {
					return new String("B1+");
				}
			}
			if (tb.getAt(1, 1) == TraxBoard.NS) {
				if (percent < 5) {
					return new String("A0+");
				}
				if (percent < 15) {
					return new String("A0/");
				}
				if (percent < 25) {
					return new String("B1/");
				}
				return new String("B1+");
			}
		}
		if (tb.getNumOfTiles() == 2) {
			if (tb.getAt(1, 1) == TraxBoard.NW) {
				if (tb.getAt(1, 2) == TraxBoard.NE) return new String("A2/");
				if (tb.getAt(2, 1) == TraxBoard.WS) return new String("B1/");
			}
		}
		return null;
	}

	private String openingMove(TraxBoard tb){
		String bestMove = null;

		if (book == null) {
			//System.out.println("book is null");
			return null;
		}
		int bestScore = Integer.MIN_VALUE;

		for (String move : tb.uniqueMoves()) {
			TraxBoard t_copy = new TraxBoard(tb);
			try {
				t_copy.makeMove(move);
			} catch (IllegalMoveException e) {
				throw new RuntimeException("This should never happen. (027)");
			}
			Openingbook.BookValue bv = book.search(t_copy);
			if (bv != null) {
				//System.out.println(move+": "+bv.alwaysPlay+bv.score(t_copy.whoToMove()));
				if (bv.alwaysPlay) {
					return move;
				}
				int score = bv.score(t_copy.whoToMove()) + TraxUtil.getRandom(50);
				if ((score == bestScore && (TraxUtil.getRandom(10) % 2 == 0)) || score > bestScore) {
					bestMove = move;
					bestScore = score;
				}
			}
		}
		if (bestMove != null) return bestMove;
		return null;
	}

	public String move(TraxBoard tb){
		String simple = null;

		simple = simpleMove(tb);
		if (simple != null) {
			//System.out.println("Simple move found");
			TraxUtil.log("Simple move found");
			return simple;
		}
		simple = openingMove(tb);
		if (simple != null) {
			System.out.println("Move found in opening book.");
			return simple;
		}

		UctNode root = new UctNode(tb);
		int maxSimulations;

		maxSimulations = this.maxSimulations / (65 - tb.getNumOfTiles() / 2);
		for (int simulationCount = 0; simulationCount < maxSimulations; simulationCount++) {
			//System.out.println(root);
			playSimulation(root);
			if (simulationCount % 250 == 0) {
				//System.err.print(".");
				;
			}
		}
		// DEBUG
	/*
	ArrayList<UctNode> children=root.getChildren();
	for (int i = 0; i < children.size (); i++) {
	    System.err.println("Child: "+i+children.get(i));
	}
	*/

		return root.getWorse().getMove();
	}

	private TraxStatus playRandomGame(TraxBoard tb){
		while (tb.isGameOver() == TraxStatus.NOPLAYER) {
			try {
				tb.makeMove(PlayerSimple.getRandomMove(tb));
			} catch (IllegalMoveException e) {
				throw new RuntimeException();
			}
		}
		return tb.isGameOver();
	}


	private UctNode UCTselect(UctNode node){
		float value;
		float bestUct = -1;
		int best_index = -1;
		ArrayList<UctNode> children = node.getChildren();

		if (children == null) {
			node.createChildren();
			children = node.getChildren();
		}
		if (children.size() == 0) {
			System.err.println(node);
			throw new RuntimeException("Doh 2!");
		}
		for (int i = 0; i < children.size(); i++) {
			value = children.get(i).UctValue();
			if (value > bestUct) {
				best_index = i;
				bestUct = value;
			}
		}
		return children.get(best_index);
	}

	private TraxStatus playSimulation(UctNode node){
		TraxStatus result;

		if (node.getVisits() < 5) {
			result = playRandomGame(new TraxBoard(node.getPosition()));
			node.update(result);
			return result;
		} else {
			if ((node.getChildren() == null) || (node.getChildren().size() == 0)) {
				node.createChildren();
			}
			UctNode next = UCTselect(node);
			if (next == null) {
				System.err.println(node);
				System.err.println(next);
				throw new RuntimeException("Doh!");
			}
			if (next.getPosition().isGameOver() != TraxStatus.NOPLAYER) {
				result = next.getPosition().isGameOver();
				next.update(result);
				return result;
			}
			result = playSimulation(next);
			next.update(result);
		}
		return result;
	}

}
