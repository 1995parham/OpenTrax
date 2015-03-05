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

public final class BoardViewer {

	public static final int EMPTY = 0,
			NS = 1, SN = 1, WE = 2, EW = 2, NW = 3, WN = 3, NE = 4, EN = 4, WS = 5,
			SW = 5, SE = 6, ES = 6;

	public static String view(TraxBoard board){
		StringBuffer result = new StringBuffer(1000);
		int i, j, k;
		int leftpiece, uppiece, upleftpiece;
		String cols = "     A     B     C     D     E     F     G     H     ";
		String rows = "1 2 3 4 5 6 7 8 ";

		if (board.isBoardEmpty())
			return new String();
		result.append(cols.substring(0, 5 + 6 * board.getColumnSize()));
		result.append('\n');
		for (i = 1; i <= board.getRowSize(); i++) {
			for (k = 0; k < 4; k++) {
				if (k == 2) {
					result.append(rows.substring(i * 2 - 2, i * 2));
					// result.append(rows,i*2-2,2);
				} else {
					result.append("  ");
				}
				for (j = 1; j <= board.getColumnSize(); j++) {
					switch (board.getAt(i, j)) {
						case NS:
							switch (k) {
								case 0:
									result.append("+--o--");
									break;
								case 1:
									result.append("|  o  ");
									break;
								case 2:
									result.append("######");
									break;
								case 3:
									result.append("|  o  ");
									break;
							}
							break;
						case WE:
							switch (k) {
								case 0:
									result.append("+--#--");
									break;
								case 1:
									result.append("|  #  ");
									break;
								case 2:
									result.append("ooo#oo");
									break;
								case 3:
									result.append("|  #  ");
									break;
							}
							break;
						case NW:
							switch (k) {
								case 0:
									result.append("+--o--");
									break;
								case 1:
									result.append("| o   ");
									break;
								case 2:
									result.append("oo   #");
									break;
								case 3:
									result.append("|   # ");
									break;
							}
							break;
						case NE:
							switch (k) {
								case 0:
									result.append("+--o--");
									break;
								case 1:
									result.append("|   o ");
									break;
								case 2:
									result.append("##   o");
									break;
								case 3:
									result.append("| #   ");
									break;
							}
							break;
						case SW:
							switch (k) {
								case 0:
									result.append("+--#--");
									break;
								case 1:
									result.append("|   # ");
									break;
								case 2:
									result.append("oo   #");
									break;
								case 3:
									result.append("| o   ");
									break;
							}
							break;
						case SE:
							switch (k) {
								case 0:
									result.append("+--#--");
									break;
								case 1:
									result.append("| #   ");
									break;
								case 2:
									result.append("##   o");
									break;
								case 3:
									result.append("|   o ");
									break;
							}
							break;
						case EMPTY:
							uppiece = board.getAt(i - 1, j);
							leftpiece = board.getAt(i, j - 1);
							upleftpiece = board.getAt(i - 1, j - 1);
							switch (k) {
								case 0:
									if ((uppiece == SN) || (uppiece == SW)
											|| (uppiece == SE)) {
										result.append("+--o--");
										break;
									}
									if ((uppiece == WE) || (uppiece == WN)
											|| (uppiece == EN)) {
										result.append("+--#--");
										break;
									}
									if ((upleftpiece != EMPTY) || (leftpiece != EMPTY)) {
										result.append("+     ");
										break;
									}
									result.append("      ");
									break;
								case 1:
									if (leftpiece == EMPTY)
										result.append("      ");
									else
										result.append("|     ");
									break;
								case 2:
									if (leftpiece == EMPTY)
										result.append("      ");
									if ((leftpiece == EW) || (leftpiece == EN)
											|| (leftpiece == ES))
										result.append("o     ");
									if ((leftpiece == NS) || (leftpiece == NW)
											|| (leftpiece == SW))
										result.append("#     ");
									break;
								case 3:
									if (leftpiece == EMPTY)
										result.append("      ");
									else
										result.append("|     ");
									break;
							}
							break;
					}
				}

				upleftpiece = board.getAt(i - 1, j - 1);
				leftpiece = board.getAt(i, j - 1);
				switch (k) {
					case 0:
						if ((upleftpiece != EMPTY) || (leftpiece != EMPTY))
							result.append("+");
						break;
					case 1:
						if (leftpiece != EMPTY)
							result.append("|");
						break;
					case 2:
						if ((leftpiece == EW) || (leftpiece == EN)
								|| (leftpiece == ES))
							result.append("o");
						if ((leftpiece == NS) || (leftpiece == NW)
								|| (leftpiece == SW))
							result.append("#");
						break;
					case 3:
						if (leftpiece != EMPTY)
							result.append("|");
						break;
				}
				result.append("\n");
			}
		}
		result.append("  ");
		for (j = 1; j <= board.getColumnSize(); j++) {
			leftpiece = board.getAt(board.getRowSize(), j - 1);
			uppiece = board.getAt(board.getRowSize(), j);
			if ((uppiece == EMPTY) && (leftpiece == EMPTY))
				result.append("      ");
			if ((uppiece == EMPTY) && (leftpiece != EMPTY))
				result.append("+     ");
			if ((uppiece == SN) || (uppiece == SW) || (uppiece == SE))
				result.append("+--o--");
			if ((uppiece == WE) || (uppiece == WN) || (uppiece == NE))
				result.append("+--#--");
		}
		if (board.getAt(board.getRowSize(), board.getColumnSize()) != EMPTY)
			result.append("+");
		result.append("\n");
		return result.toString();
	}
}