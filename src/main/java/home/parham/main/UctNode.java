package org.traxgame.main;

/*

 Date: 22th of September 2009
 version 0.1
 All source under GPL version 2 
 (GNU General Public License - http://www.gnu.org/)
 contact traxplayer@gmail.com for more information about this code

 */

import java.util.ArrayList;

public class UctNode {
	private int visits, wins, draws;
	private UctNode parent;
	private Traxboard position;
	private String move;
	private ArrayList<UctNode> children;

	public UctNode(Traxboard position) {
		this(position, null, null);
	}

	public UctNode(Traxboard position, String move, UctNode parent) {
		visits = 0;
		wins = 0;
		draws = 0;
		children = null;
		this.parent = parent;
		this.position = position;
		this.move = move;
	}

	public String toString() {
		StringBuffer result = new StringBuffer(1000);
		result.append("visits=" + getVisits() + ", ");
		result.append("wins=" + getWins() + ", ");
		result.append("draws=" + getDraws() + ", ");
		result.append("winrate=" + getWinrate() + "\n");
		result.append(getPosition() + "\n");
		if (move == null) {
			result.append("move is null\n");
		} else {
			result.append("move=" + move + "\n");
		}
		if (children == null) {
			result.append("children is null\n");
		} else {
			result.append("children.size()=" + children.size() + "\n");
		}
		return new String(result);
	}

	public void incWins() {
		wins++;
	}

	public void incDraws() {
		draws++;
	}

	public void incVisits() {
		visits++;
	}

	public void createChildren() {
		String move;

		children = new ArrayList<UctNode>(10);
		if (position.isGameOver() != Traxboard.NOPLAYER) {
			return;
		}
		ArrayList<String> moves = position.uniqueMoves();
		for (int i = 0; i < moves.size(); i++) {
			move = moves.get(i);
			Traxboard tcopy = new Traxboard(position);
			try {
				tcopy.makeMove(move);
			} catch (IllegalMoveException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
			UctNode child = new UctNode(new Traxboard(tcopy), move, this);
			children.add(child);
		}
	}

	float getWinrate() {
		if (visits == 0)
			return 0;
		return (float) ((0.25 * draws + wins) / visits);
	}

	Traxboard getPosition() {
		return position;
	}

	int getWins() {
		return wins;
	}

	int getDraws() {
		return draws;
	}

	int getVisits() {
		return visits;
	}

	UctNode getParent() {
		return parent;
	}

	ArrayList<UctNode> getChildren() {
		return children;
	}

	String getMove() {
		return move;
	}

	UctNode getWorse() {
		assert (children.size() > 0); /*
									 * Don't call this method for nodes without
									 * children created
									 */
		int worse_index = 0;
		float worse_winrate = 1;
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i).getWinrate() < worse_winrate) {
				worse_winrate = children.get(i).getWinrate();
				worse_index = i;
			}
		}
		return children.get(worse_index);
	}

	UctNode getBest() {
		assert (children.size() > 0); /*
									 * Don't call this method for nodes without
									 * children created
									 */
		int best_index = 0;
		float best_winrate = -1;
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i).getWinrate() > best_winrate) {
				best_winrate = children.get(i).getWinrate();
				best_index = i;
			}
		}
		return children.get(best_index);
	}

	void update(int result) {
		incVisits();
		switch (result) {
		case Traxboard.DRAW:
			incDraws();
			break;
		case Traxboard.WHITE:
		case Traxboard.BLACK:
			if (result == position.whoToMove()) {
				incWins();
			}
			break;
		default:
			/* This should never happen */
			assert (false);
		}
	}

	float UctValue() {
		if (getVisits() == 0) {
			return 10000 + TraxUtil.getRandom(100);
		}
		if (getParent() == null) {
			return getWinrate(); /* only the root should have no parent */
		}
		return (getWinrate() + 10 * (float) Math.sqrt(Math.log(getParent()
				.getVisits()) / (5 * getVisits())));
	}

	void printPrincipalVariation() {
		UctNode next = this;

		while (next.getChildren().size() > 0) {
			System.out.print(next.getBest().getMove() + " ");
			next = getBest();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		try {
			Traxboard tb = new Traxboard();
			tb.makeMove("a1s");
			tb.makeMove("b1s");
			UctNode n = new UctNode(tb);
			System.out.println(tb);
			n.createChildren();
			System.out.println(n);
		} catch (IllegalMoveException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
