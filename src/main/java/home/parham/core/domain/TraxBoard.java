/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax 
 * 
 * [] Package Name : home.parham.main.main
 *
 * [] Creation Date : 23-01-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/

package home.parham.core.domain;

import home.parham.core.exceptions.IllegalMoveException;
import java.util.ArrayList;

public class TraxBoard {

	private boolean boardEmpty;
	private TraxStatus wtm;
	private BoardArray board;
	private TraxStatus gameover;
	private int tilesNumber;
	private int firstRow, lastRow, firstColumn, lastColumn;

	private boolean boardEmpty_save;
	private TraxStatus wtm_save;
	private BoardArray board_save;
	private TraxStatus gameoverSave;
	private int num_of_tiles_save;
	private int firstrow_save, lastrow_save, firstcol_save, lastcol_save;

	public static final int EMPTY = 0, INVALID = 7,
			NS = 1, SN = 1, WE = 2, EW = 2, NW = 3, WN = 3, NE = 4, EN = 4, WS = 5,
			SW = 5, SE = 6, ES = 6;


	public TraxBoard(){
		wtm = TraxStatus.WHITE;
		gameover = TraxStatus.NOPLAYER;
		tilesNumber = 0;
		board = new BoardArray();
		board_save = new BoardArray();
		boardEmpty = true;

	}

	public TraxBoard(TraxBoard org){
		wtm = org.wtm;
		gameover = org.gameover;
		tilesNumber = org.tilesNumber;

		board = new BoardArray(org.board);
		board_save = new BoardArray(org.board_save);

		firstRow = org.firstRow;
		firstColumn = org.firstColumn;
		lastRow = org.lastRow;
		lastColumn = org.lastColumn;
		firstrow_save = org.firstrow_save;
		firstcol_save = org.firstcol_save;
		lastrow_save = org.lastrow_save;
		lastcol_save = org.lastcol_save;
		boardEmpty = org.boardEmpty;
	}

	public boolean isBoardEmpty(){
		return boardEmpty;
	}

	public boolean isBlank(int piece){
		return (piece == EMPTY);
	}

	/**
	 * Returns the numbers of used tiles. Can be used to determine if we are in
	 * the opening, middle or end-phase of the game
	 *
	 * @return the number of used tiles
	 */
	public int getNumOfTiles(){
		return tilesNumber;
	}

	public TraxBoard rotate(){
		TraxBoard result = new TraxBoard(this);

		int size = result.board.size();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				switch (board.get(size - 1 - j, i)) {
					case NS:
						result.board.put(i, j, TraxTiles.WE);
						break;
					case WE:
						result.board.put(i, j, TraxTiles.NS);
						break;
					case EMPTY:
						result.board.put(i, j, TraxTiles.EMPTY);
						break;
					case NW:
						result.board.put(i, j, TraxTiles.NE);
						break;
					case NE:
						result.board.put(i, j, TraxTiles.SE);
						break;
					case SE:
						result.board.put(i, j, TraxTiles.SW);
						break;
					case SW:
						result.board.put(i, j, TraxTiles.NW);
						break;
				}
			}
		}
		result.setCorners();
		return result;
	}

	private void setCorners(){
		int size = board.size();

		firstRow = -1;
		firstColumn = -1;
		lastColumn = -1;
		lastRow = -1;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if ((firstRow < 0) && (board.get(i, j) != TraxTiles.EMPTY))
					firstRow = i;
				if ((lastRow < 0) && (board.get(size - 1 - i, j) != TraxTiles.EMPTY))
					lastRow = 16 - i;
				if ((firstColumn < 0) && (board.get(j, i) != TraxTiles.EMPTY))
					firstColumn = i;
				if ((lastColumn < 0) && (board.get(j, size - 1 - i) != TraxTiles.EMPTY))
					lastColumn = 16 - i;
			}
		}
	}

	private void saveState(){
		wtm_save = wtm;
		boardEmpty_save = boardEmpty;
		gameoverSave = gameover;
		num_of_tiles_save = tilesNumber;
		firstrow_save = firstRow;
		firstcol_save = firstColumn;
		lastrow_save = lastRow;
		lastcol_save = lastColumn;
		board_save = new BoardArray(board);
	}

	private void restoreState(){
		wtm = wtm_save;
		boardEmpty = boardEmpty_save;
		gameover = gameoverSave;
		tilesNumber = num_of_tiles_save;
		firstRow = firstrow_save;
		firstColumn = firstcol_save;
		lastRow = lastrow_save;
		lastColumn = lastcol_save;
		board = new BoardArray(board_save);
	}

	public int getRowSize(){
		return lastRow - firstRow + 1;
	}

	public int getColumnSize(){
		return lastColumn - firstColumn + 1;
	}


	/**
	 * Given a row and column, the method checks if that (row,col) is occupied
	 * or free
	 *
	 * @param col the row number 1-8
	 * @param row the column number 1-8
	 * @return true if the place (row,col) is free or false otherwise
	 */
	public boolean isBlank(int row, int col){
		return (getAt(row, col) == EMPTY);
	}

	/**
	 * Try to make the specified move. If it is legal, then update the board.
	 * Accepts upper-case and lower-case letters.
	 * Just new notation accepted.
	 *
	 * @param move: The move
	 * @throws home.parham.core.exceptions.IllegalMoveException
	 */
	public void makeMove(TraxMove move) throws IllegalMoveException{
		int col, row, neighbor;
		char tile;
		int ohs_up = 0, ohs_down = 0, ohs_right = 0, ohs_left = 0, eks_up = 0, eks_down = 0, eks_right = 0, eks_left = 0;

		move.validate();
		col = move.getColumn();
		row = move.getRow();
		tile = move.getTile();

		if (gameover != TraxStatus.NOPLAYER)
			throw new IllegalMoveException("Game is over.", move);

		if (boardEmpty) {
			if (move.equals("@0+")) {
				putAt(0, 0, NS);
				switchPlayer();
				return;
			}
			if (move.equals("@0/")) {
				putAt(0, 0, NW);
				switchPlayer();
				return;
			}
			throw new IllegalMoveException("Only @0/ and @0+ accepted as first move.", move);
		}

		if (!isBlank(row, col))
			throw new IllegalMoveException("Occupied.", move);

		saveState();

		int up = getAt(row - 1, col);
		int down = getAt(row + 1, col);
		int left = getAt(row, col - 1);
		int right = getAt(row, col + 1);

		if (up == SN || up == SE || up == SW)
			ohs_up = 1;
		if (up == EW || up == NW || up == NE)
			eks_up = 1;
		if (down == NS || down == NE || down == NW)
			ohs_down = 1;
		if (down == EW || down == SW || down == SE)
			eks_down = 1;
		if (left == EN || left == ES || left == EW)
			ohs_left = 1;
		if (left == WS || left == WN || left == NS)
			eks_left = 1;
		if (right == WN || right == WE || right == WS)
			ohs_right = 1;
		if (right == ES || right == NS || right == EN)
			eks_right = 1;

		neighbor = ohs_up + 2 * ohs_down + 4 * ohs_left + 8 * ohs_right
				+ 16 * eks_up + 32 * eks_down + 64 * eks_left + 128 * eks_right;

		switch (neighbor) {
			case 0:
				throw new IllegalMoveException("No Neighbors.", move);
			case 1:
				if (tile == '/') {
					putAt(row, col, NW);
				} else if (tile == '\\') {
					putAt(row, col, NE);
				} else if (tile == '+') {
					putAt(row, col, NS);
				} else {
					throw new IllegalMoveException("Illegal Direction.", move);
				}
				break;
			case 2:
				if (tile == '/') {
					putAt(row, col, SE);
				} else if (tile == '\\') {
					putAt(row, col, SW);
				} else if (tile == '+') {
					putAt(row, col, NS);
				} else {
					throw new IllegalMoveException("illegal direction.", move);
				}
				break;
			case 4:
				if (tile == '/') {
					putAt(row, col, WN);
				} else if (tile == '\\') {
					putAt(row, col, WS);
				} else if (tile == '+') {
					putAt(row, col, WE);
				} else {
					throw new IllegalMoveException("illegal direction.", move);
				}
				break;
			case 8:
				if (tile == '/') {
					putAt(row, col, ES);
				} else if (tile == '\\') {
					putAt(row, col, EN);
				} else if (tile == '+') {
					putAt(row, col, EW);
				} else {
					throw new IllegalMoveException("illegal direction.", move);
				}
				break;
			case 16:
				if (tile == '/') {
					putAt(row, col, SE);
				} else if (tile == '\\') {
					putAt(row, col, SW);
				} else if (tile == '+') {
					putAt(row, col, WE);
				} else {
					throw new IllegalMoveException("illegal direction.", move);
				}
				break;
			case 18:
				if (tile == '/') {
					putAt(row, col, SE);
				} else if (tile == '\\') {
					putAt(row, col, SW);
				} else {
					throw new IllegalMoveException("illegal direction.", move);
				}
				break;
			case 20:
				if (tile == '\\') {
					putAt(row, col, WS);
				} else if (tile == '+') {
					putAt(row, col, WE);
				} else {
					throw new IllegalMoveException("illegal direction.", move);
				}
				break;
			case 24:
				if (tile == '/') {
					putAt(row, col, SE);
				} else if (tile == '+') {
					putAt(row, col, WE);
				} else {
					throw new IllegalMoveException("illegal direction.");
				}
				break;
			case 32:
				if (tile == '/') {
					putAt(row, col, NW);
				} else if (tile == '\\') {
					putAt(row, col, NE);
				} else if (tile == '+') {
					putAt(row, col, WE);
				} else {
					throw new IllegalMoveException("illegal direction", move);
				}
				break;
			case 33:
				if (tile == '/') {
					putAt(row, col, NW);
				} else if (tile == '\\') {
					putAt(row, col, NE);
				} else {
					throw new IllegalMoveException("illegal direction.");
				}
				break;
			case 36:
				if (tile == '/') {
					putAt(row, col, NW);
				} else if (tile == '+') {
					putAt(row, col, WE);
				} else {
					throw new IllegalMoveException("illegal direction.");
				}
				break;
			case 40:
				if (tile == '/')
					throw new IllegalMoveException("illegal direction.");
				if (tile == '\\')
					putAt(row, col, EN);
				if (tile == '+')
					putAt(row, col, EW);
				break;
			case 64:
				if (tile == '/')
					putAt(row, col, ES);
				if (tile == '\\')
					putAt(row, col, EN);
				if (tile == '+')
					putAt(row, col, NS);
				if (tile == 'S')
					putAt(row, col, NS);
				if (tile == 'C')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'L')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'R')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'U')
					putAt(row, col, SE);
				if (tile == 'D')
					putAt(row, col, NE);
				break;
			case 65:
				if (tile == '/')
					throw new IllegalMoveException("illegal direction.");
				if (tile == '\\')
					putAt(row, col, NE);
				if (tile == '+')
					putAt(row, col, NS);
				if (tile == 'S')
					putAt(row, col, NS);
				if (tile == 'C')
					putAt(row, col, NE);
				if (tile == 'L')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'R')
					putAt(row, col, NE);
				if (tile == 'U')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'D')
					putAt(row, col, NE);
				break;
			case 66:
				if (tile == '/')
					putAt(row, col, SE);
				if (tile == '\\')
					throw new IllegalMoveException("illegal direction.");
				if (tile == '+')
					putAt(row, col, SN);
				if (tile == 'S')
					putAt(row, col, SN);
				if (tile == 'C')
					putAt(row, col, SE);
				if (tile == 'L')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'R')
					putAt(row, col, SE);
				if (tile == 'U')
					putAt(row, col, SE);
				if (tile == 'D')
					throw new IllegalMoveException("illegal direction.");
				break;
			case 72:
				if (tile == '/')
					putAt(row, col, ES);
				if (tile == '\\')
					putAt(row, col, EN);
				if (tile == '+')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'S')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'C')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'L')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'R')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'U')
					putAt(row, col, NE);
				if (tile == 'D')
					putAt(row, col, SE);
				break;
			case 128:
				if (tile == '/')
					putAt(row, col, WN);
				if (tile == '\\')
					putAt(row, col, WS);
				if (tile == '+')
					putAt(row, col, NS);
				if (tile == 'S')
					putAt(row, col, NS);
				if (tile == 'C')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'L')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'R')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'U')
					putAt(row, col, WS);
				if (tile == 'D')
					putAt(row, col, WN);
				break;
			case 129:
				if (tile == '/')
					putAt(row, col, NW);
				if (tile == '\\')
					throw new IllegalMoveException("illegal direction.");
				if (tile == '+')
					putAt(row, col, NS);
				if (tile == 'S')
					putAt(row, col, NS);
				if (tile == 'C')
					putAt(row, col, NW);
				if (tile == 'L')
					putAt(row, col, NW);
				if (tile == 'R')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'U')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'D')
					putAt(row, col, NW);
				break;
			case 130:
				if (tile == '/')
					throw new IllegalMoveException("illegal direction.");
				if (tile == '\\')
					putAt(row, col, SW);
				if (tile == '+')
					putAt(row, col, SN);
				if (tile == 'S')
					putAt(row, col, SN);
				if (tile == 'C')
					putAt(row, col, SW);
				if (tile == 'L')
					putAt(row, col, SW);
				if (tile == 'R')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'U')
					putAt(row, col, SW);
				if (tile == 'D')
					throw new IllegalMoveException("illegal direction.");
				break;
			case 132:
				if (tile == '/')
					putAt(row, col, WN);
				if (tile == '\\')
					putAt(row, col, WS);
				if (tile == '+')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'S')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'C')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'L')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'R')
					throw new IllegalMoveException("illegal direction.");
				if (tile == 'U')
					putAt(row, col, WN);
				if (tile == 'D')
					putAt(row, col, WS);
				break;
			default:
			/* This should never happen */
				throw new RuntimeException("This should never happen. (013)");
		}
		if (row == 0)
			row++;
		if (col == 0)
			col++;

		if (!forcedMove(row - 1, col)) {
			restoreState();
			throw new IllegalMoveException("illegal filled cave.");
		}
		if (!forcedMove(row + 1, col)) {
			restoreState();
			throw new IllegalMoveException("illegal filled cave.");
		}
		if (!forcedMove(row, col - 1)) {
			restoreState();
			throw new IllegalMoveException("illegal filled cave.");
		}
		if (!forcedMove(row, col + 1)) {
			restoreState();
			throw new IllegalMoveException("illegal filled cave.");
		}
		
		/* note that switchPlayer() "must" come before isGameOver() */
		switchPlayer();
		/* update the gameOver attribute */
		isGameOver();

	}

	public void switchPlayer(){
		switch (wtm) {
			case WHITE:
				wtm = TraxStatus.BLACK;
				break;
			case BLACK:
				wtm = TraxStatus.WHITE;
				break;
		}
	}

	public TraxStatus isGameOver(){
		boolean isWhiteWins = false;
		boolean isBlackWins = false;
		int sp;

		if (gameover != TraxStatus.NOPLAYER)
			return gameover;
		if (tilesNumber < 4) {
			gameover = TraxStatus.NOPLAYER;
			return gameover;
		}

//		if (uniqueMoves().size() == 0) {
//			gameover = TraxStatus.DRAW;
//			return gameover;
//		}

		// Now check loop wins
		for (int i = 1; i < 8; i++) {
			for (int j = 1; j < 8; j++) {
				switch (getAt(i, j)) {
					case NW:
						if (checkLine(i, j, 'u', 'l'))
							isBlackWins = true;
						break;
					case SE:
						if (checkLine(i, j, 'u', 'l'))
							isWhiteWins = true;
						break;
					case EMPTY:
					case NS:
					case WE:
					case NE:
					case WS:
						break;
				}
			}
		}

		if (isWhiteWins && isBlackWins) {
			gameover = whoDidLastMove();
			return gameover;
		}
		if (isWhiteWins) {
			gameover = TraxStatus.WHITE;
			return gameover;
		}
		if (isBlackWins) {
			gameover = TraxStatus.BLACK;
			return gameover;
		}
		return TraxStatus.NOPLAYER;
	}

	public ArrayList<String> uniqueMoves(){
		/*
		 * complex throw away a lot of equal moves
		 * and symmetries (hopefully)
		*/

		ArrayList<String> moves = new ArrayList<String>(100); // 50 might be enough

		int i, j, k;
		int dl, dr, ur, ul, rr;
		int[][] neighbors = new int[board.size()][board.size()]; // which neighbors - default all
		// values 0
		boolean[][][] dirlist = new boolean[board.size()][board.size()][3]; // which directions for
		// move
		// 0 /, 1 \, 2 +
		// true means already used
		// default all values false
		int ohs_up, ohs_down, ohs_right, ohs_left, eks_up, eks_down, eks_right, eks_left;
		int up, down, left, right;
		int iBegin, jBegin, iEnd, jEnd;
		boolean lrsym, udsym, rsym;

		if (gameover != TraxStatus.NOPLAYER) {
			return new ArrayList<String>(0);
		}

		/*
		 * Empty board only have
		 * 1) @0/
		 * 2) @0+
		 * moves.
		*/
		if (boardEmpty) {
			moves.add("@0/");
			moves.add("@0+");
			moves.trimToSize();
			return moves;
		}
		if (getRowSize() * getColumnSize() == 1) {
			switch (getAt(1, 1)) {
				case NW:
					moves.add("@1+");
					moves.add("@1/");
					moves.add("@1\\");
					moves.add("B1+");
					moves.add("B1/");
					moves.add("B1\\");
					break;
				case NS:
					moves.add("@1+");
					moves.add("@1/");
					moves.add("@1\\");
					moves.add("A0/");
					moves.add("A0+");
					moves.add("A0\\");
					break;
			}
			moves.trimToSize();
			return moves;
		}

		for (i = 0; i < board.size(); i++)
			for (j = 0; j < board.size(); j++)
				for (k = 0; k < 3; k++)
					dirlist[i][j][k] = false;

		lrsym = isLeftRightMirror();
		udsym = isUpDownMirror();
		rsym = isRotateMirror();
		iBegin = 1;
		jBegin = 1;
		iEnd = getRowSize();
		jEnd = getColumnSize();
		if (lrsym)
			jEnd = (getColumnSize() + 1) / 2;
		if (rsym || udsym)
			iEnd = (getRowSize() + 1) / 2;

		for (i = iBegin; i <= iEnd; i++) {
			for (j = jBegin; j <= jEnd; j++) {
				if (!(isBlank(i, j))) {
					neighbors[i][j] = 0;
				} else {
					ohs_up = 0;
					ohs_down = 0;
					ohs_right = 0;
					ohs_left = 0;
					eks_up = 0;
					eks_down = 0;
					eks_right = 0;
					eks_left = 0;
					up = getAt(i - 1, j);
					down = getAt(i + 1, j);
					left = getAt(i, j - 1);
					right = getAt(i, j + 1);

					if (up == SN || up == SW || up == SE) {
						ohs_up = 1;
					} else if (up != EMPTY) {
						eks_up = 1;
					}

					if (down == NS || down == NW || down == NE) {
						ohs_down = 1;
					} else if (down != EMPTY) {
						eks_down = 1;
					}

					if (left == EN || left == ES || left == WE)
						ohs_left = 1;
					else if (left != EMPTY)
						eks_left = 1;

					if (right == WE || right == WS || right == WN)
						ohs_right = 1;
					else if (right != EMPTY)
						eks_right = 1;

					neighbors[i][j] = ohs_up + 2 * ohs_down + 4 * ohs_left
							+ 8 * ohs_right + 16 * eks_up + 32 * eks_down + 64
							* eks_left + 128 * eks_right;
				}
			}
		}

		for (i = iBegin; i <= iEnd; i++) {
			for (j = jBegin; j <= jEnd; j++) {
				if (neighbors[i][j] != 0) {
					dl = getAt(i + 1, j - 1);
					dr = getAt(i + 1, j + 1);
					ur = getAt(i - 1, j + 1);
					ul = getAt(i - 1, j - 1);
					rr = getAt(i, j + 2);
					switch (neighbors[i][j]) {
						case 1:
							if (dr == NS || dr == NW || dr == NE)
								dirlist[i][j + 1][1] = true;
							if (dr == WN || dr == WS || dr == WE)
								dirlist[i + 1][j][1] = true;
							if (dl == EW || dl == ES || dl == ES)
								dirlist[i + 1][j][0] = true;
							if (ur == SW || ur == SE || ur == SN)
								dirlist[i][j + 1][0] = true;
							break;
						case 2: {
							if (dr == NS || dr == NW || dr == NE)
								dirlist[i][j + 1][1] = true;
							if (ur == SW || ur == SE || ur == SN)
								dirlist[i][j + 1][0] = true;
							break;
						}
						case 4: {
							if (dl == ES || dl == EN || dl == EW)
								dirlist[i + 1][j][0] = true;
							if (dr == WN || dr == WS || dr == WE)
								dirlist[i + 1][j][1] = true;
							if (ur == SW || ur == SN || ur == SE)
								dirlist[i][j + 1][0] = true;
							if (dr == NS || dr == NE || dr == NW)
								dirlist[i][j + 1][1] = true;
							break;
						}
						case 8: {
							if (dl == ES || dl == EN || dl == EW)
								dirlist[i + 1][j][0] = true;
							if (dr == WN || dr == WE || dr == WS)
								dirlist[i + 1][j][1] = true;
							break;
						}
						case 16: {
							if (dr == SW || dr == SE || dr == WE)
								dirlist[i][j + 1][1] = true;
							if (dr == SE || dr == SN || dr == EN)
								dirlist[i + 1][j][1] = true;
							if (dl == NW || dl == NS || dl == WS)
								dirlist[i + 1][j][0] = true;
							if (ur == NW || ur == NE || ur == WE)
								dirlist[i][j + 1][0] = true;
							break;
						}
						case 18:
						case 33: {
							dirlist[i][j + 1][1] = true;
							dirlist[i][j + 1][0] = true;
							dirlist[i][j][2] = true;
							break;
						}
						case 20:
						case 65: {
							if (rr != EMPTY)
								dirlist[i][j + 1][2] = true;
							dirlist[i + 1][j][0] = true;
							dirlist[i + 1][j][1] = true;
							dirlist[i][j + 1][0] = true;
							dirlist[i][j][0] = true;
							break;
						}
						case 24:
						case 129: {
							dirlist[i + 1][j][1] = true;
							dirlist[i][j][1] = true;
							break;
						}
						case 32: {
							if (dr == SE || dr == SW || dr == EW)
								dirlist[i][j + 1][1] = true;
							if (ur == NW || ur == NE || ur == WE)
								dirlist[i][j + 1][0] = true;
							break;
						}
						case 36: {
							if (ul == NW || ul == SW || ul == NS)
								dirlist[i - 1][j][1] = true;
							dirlist[i][j + 1][1] = true;
							dirlist[i][j + 1][0] = true;
							dirlist[i][j][1] = true;
							break;
						}
						case 40:
						case 130: {
							dirlist[i][j][0] = true;
							break;
						}
						case 64: {
							if (dl == WN || dl == WS || dl == NS)
								dirlist[i + 1][j][0] = true;
							if (dr == EN || dr == ES || dr == NS)
								dirlist[i + 1][j][1] = true;
							if (ur == NW || ur == NE || ur == WE)
								dirlist[i][j + 1][0] = true;
							if (dr == SE || dr == SW || dr == EW)
								dirlist[i][j + 1][1] = true;
							break;
						}
						case 66: {
							if (ul == EW || ul == ES || ul == EN)
								dirlist[i - 1][j][1] = true;
							dirlist[i][j + 1][1] = true;
							dirlist[i][j + 1][0] = true;
							dirlist[i][j][1] = true;
							break;
						}
						case 72:
						case 132: {
							dirlist[i + 1][j][0] = true;
							dirlist[i + 1][j][1] = true;
							dirlist[i][j][2] = true;
							break;
						}
						case 128: {
							if (dl == WS || dl == WN || dl == SN)
								dirlist[i + 1][j][0] = true;
							if (dr == EN || dr == ES || dr == NS)
								dirlist[i + 1][j][1] = true;
							break;
						}
					}
				}
			}
		}

		// remove left-right symmetry moves
		if (lrsym && getColumnSize() % 2 == 1) {
			for (i = iBegin; i <= iEnd; i++) {
				dirlist[i][jEnd][0] = true;
			}
		}
		// remove up-down symmetry moves
		if (udsym && getRowSize() % 2 == 1) {
			for (j = jBegin; j <= jEnd; j++) {
				dirlist[iEnd][j][1] = true;
			}
		}

		// collects the moves
		for (i = iBegin; i <= iEnd; i++)
			for (j = jBegin; j <= jEnd; j++) {
				// remove rotation symmetry moves
				if (rsym && getRowSize() % 2 == 1) {
					int jMiddle = (getColumnSize() + 1) / 2;
					if (j > jMiddle && i == iEnd) {
						continue;
					}
				}
				if (neighbors[i][j] != 0) {
					ohs_up = 0;
					ohs_down = 0;
					ohs_right = 0;
					ohs_left = 0;
					eks_up = 0;
					eks_down = 0;
					eks_right = 0;
					eks_left = 0;
					up = getAt(i - 1, j);
					down = getAt(i + 1, j);
					left = getAt(i, j - 1);
					right = getAt(i, j + 1);

					if (up == SN || up == SW || up == SE)
						ohs_up = 1;
					else if (up != EMPTY)
						eks_up = 1;
					if (down == NS || down == NW || down == NE)
						ohs_down = 1;
					else if (down != EMPTY)
						eks_down = 1;
					if (left == EN || left == ES || left == WE)
						ohs_left = 1;
					else if (left != EMPTY)
						eks_left = 1;
					if (right == WE || right == WS || right == WN)
						ohs_right = 1;
					else if (right != EMPTY)
						eks_right = 1;

					if (!dirlist[i][j][0]) {
						saveState();
						if ((ohs_up + ohs_left > 0)
								|| (eks_right + eks_down > 0))
							putAt(i, j, NW);
						if ((eks_up + eks_left > 0)
								|| (ohs_right + ohs_down > 0))
							putAt(i, j, SE);
						if (forcedMove(i - 1, j) && forcedMove(i + 1, j)
								&& forcedMove(i, j - 1) && forcedMove(i, j + 1)) {
							moves.add(ColumnRowGenerator.generate(j, i) + "/");
						}
						restoreState();
					}
					if (!dirlist[i][j][1]) {
						saveState();
						if ((ohs_up + ohs_right > 0)
								|| (eks_left + eks_down > 0))
							putAt(i, j, NE);
						if ((eks_up + eks_right > 0)
								|| (ohs_left + ohs_down > 0))
							putAt(i, j, SW);
						if (forcedMove(i - 1, j) && forcedMove(i + 1, j)
								&& forcedMove(i, j - 1) && forcedMove(i, j + 1)) {
							moves.add(ColumnRowGenerator.generate(j, i) + "\\");
						}
						restoreState();
					}
					if (!dirlist[i][j][2]) {
						saveState();
						if ((ohs_up + ohs_down > 0)
								|| (eks_left + eks_right > 0))
							putAt(i, j, NS);
						if ((eks_up + eks_down > 0)
								|| (ohs_left + ohs_right > 0))
							putAt(i, j, WE);
						if (forcedMove(i - 1, j) && forcedMove(i + 1, j)
								&& forcedMove(i, j - 1) && forcedMove(i, j + 1)) {
							moves.add(ColumnRowGenerator.generate(j, i) + "+");
						}
						restoreState();
					}
				}
			}
		moves.trimToSize();
		return moves;
	}

	private boolean checkLine(int row, int col, char direction, char type){
		/*
		 * type can be
		 * h : horizontal
		 * v : vertical
		 * l : loop
		*/

		int start_row = row;
		int start_col = col;
		int ix = 0;
		String newdir;

		newdir = " uurllr" // 'u' 1.. 6
				+ "ddlrrl" // 'd' 7..12
				+ "llduud" // 'l' 13..18
				+ "rruddu"; // 'r' 19..24

		for (; ; ) {
			if (isBlank(row, col))
				return false; // no line starts with a empty space or we are out of range
			switch (direction) {
				case 'u':
					// newdir's first line
					ix = 0;
					break;
				case 'd':
					// newdir's second line
					ix = 6;
					break;
				case 'l':
					// newdir's third line
					ix = 12;
					break;
				case 'r':
					// newdir's fourth line
					ix = 18;
					break;
			}
			ix += getAt(row, col);
			direction = newdir.charAt(ix);
			switch (direction) {
				case 'u':
					row--;
					break;
				case 'd':
					row++;
					break;
				case 'l':
					col--;
					break;
				case 'r':
					col++;
					break;
			}
//			if ((type == 'h') && (col == 9))
//				return true; // left-right win
//			if ((type == 'v') && (row == 9))
//				return true; // top-bottom win
			if ((row == start_row) && (col == start_col)) {
				/* loop win */
				return type == 'l';
			}
		}
	}

	public boolean isLeftRightMirror(){
		int piece, i, j, j2;

		for (i = 1; i <= getRowSize(); i++) {
			j2 = getColumnSize();
			for (j = 1; j <= ((getColumnSize() + 1) / 2); j++) {
				piece = getAt(i, j);
				switch (getAt(i, j2)) {
					case NW:
						if (piece != NE)
							return false;
						break;
					case NE:
						if (piece != NW)
							return false;
						break;
					case SW:
						if (piece != SE)
							return false;
						break;
					case SE:
						if (piece != SW)
							return false;
						break;
					case NS:
						if (piece != NS)
							return false;
						break;
					case WE:
						if (piece != WE)
							return false;
						break;
					case EMPTY:
						if (piece != EMPTY)
							return false;
						break;
				}
				j2--;
			}
		}
		return true;
	}

	public boolean isUpDownMirror(){
		int piece, i, j, i2;

		i2 = getRowSize();
		for (i = 1; i <= ((getRowSize() + 1) / 2); i++) {
			for (j = 1; j <= getColumnSize(); j++) {
				piece = getAt(i, j);
				switch (getAt(i2, j)) {
					case NW:
						if (piece != SW)
							return false;
						break;
					case NE:
						if (piece != SE)
							return false;
						break;
					case SW:
						if (piece != NW)
							return false;
						break;
					case SE:
						if (piece != NE)
							return false;
						break;
					case NS:
						if (piece != NS)
							return false;
						break;
					case WE:
						if (piece != WE)
							return false;
						break;
					case EMPTY:
						if (piece != EMPTY)
							return false;
						break;
				}
			}
			i2--;
		}
		return true;
	}

	// 90 degree rotation
	public boolean isRotateMirror(){
		int i, j, piece, i2, j2;

		i2 = getRowSize();
		for (i = 1; i <= ((getRowSize() + 1) / 2); i++) {
			j2 = getColumnSize();
			for (j = 1; j <= getColumnSize(); j++) {
				piece = getAt(i, j);
				switch (getAt(i2, j2)) {
					case NW:
						if (piece != SE)
							return false;
						break;
					case NE:
						if (piece != SW)
							return false;
						break;
					case SW:
						if (piece != NE)
							return false;
						break;
					case SE:
						if (piece != NW)
							return false;
						break;
					case NS:
						if (piece != NS)
							return false;
						break;
					case WE:
						if (piece != WE)
							return false;
						break;
					case EMPTY:
						if (piece != EMPTY)
							return false;
						break;
				}
				j2--;
			}
			i2--;
		}
		return true;
	}

	public TraxStatus whoToMove(){
		return wtm;
	}

	public TraxStatus whoDidLastMove(){
		if (boardEmpty)
			return TraxStatus.NOPLAYER;

		if (wtm == TraxStatus.WHITE) {
			return TraxStatus.BLACK;
		} else if (wtm == TraxStatus.BLACK) {
			return TraxStatus.WHITE;
		}

		return TraxStatus.NOPLAYER;
	}

	public int getAt(int row, int col){
		return board.get(row - 1, col - 1).number;
	}

	public void putAt(int row, int col, int piece){
		if ((row + col < 0))
			throw new AssertionError();
		if (piece == EMPTY) {
			if (board.get(row - 1, col - 1) != TraxTiles.EMPTY)
				tilesNumber--;
			board.put(row - 1, col - 1, TraxTiles.tilesFromNumber(piece));
			return;
		} else {
			if (boardEmpty) {
				boardEmpty = false;
				firstColumn = 0;
				firstRow = 0;
				lastRow = 0;
				lastColumn = 0;
				tilesNumber = 1;
				board.put(0, 0, TraxTiles.tilesFromNumber(piece));
				return;
			}
			if (row == 0) {
				firstRow--;
			}
			if (col == 0) {
				firstColumn--;
			}
			if (row > getRowSize()) {
				lastRow += row - getRowSize();
			}
			if (col > getColumnSize()) {
				lastColumn += col - getColumnSize();
			}
			tilesNumber++;
		}
		board.put(row - 1, col - 1, TraxTiles.tilesFromNumber(piece));
	}

	public boolean forcedMove(int brow, int bcol){
		if (!isBlank(brow, bcol))
			return true;

		int up = getAt(brow - 1, bcol);
		int down = getAt(brow + 1, bcol);
		int left = getAt(brow, bcol - 1);
		int right = getAt(brow, bcol + 1);

		int neighbors = 0;

		if (!isBlank(up))
			neighbors++;
		if (!isBlank(down))
			neighbors++;
		if (!isBlank(left))
			neighbors++;
		if (!isBlank(right))
			neighbors++;

		if (neighbors < 2)
			return true; // Less than two pieces bordering

		int white_up = 0, black_up = 0, white_down = 0, black_down = 0, white_left = 0, black_left = 0, white_right = 0, black_right = 0;

		if (up == SN || up == SW || up == SE)
			white_up = 1;
		if (up == WE || up == NW || up == NE)
			black_up = 1;
		if (down == NS || down == NW || down == NE)
			white_down = 1;
		if (down == WE || down == SW || down == SE)
			black_down = 1;
		if (left == EW || left == EN || left == ES)
			white_left = 1;
		if (left == NS || left == NW || left == SW)
			black_left = 1;
		if (right == WE || right == WN || right == WS)
			white_right = 1;
		if (right == NS || right == NE || right == SE)
			black_right = 1;

		int white = white_up + white_down + white_left + white_right;
		int black = black_up + black_down + black_left + black_right;

		if ((white > 2) || (black > 2)) // Illegal filled cave
			return false;

		if ((white < 2) && (black < 2)) // Done
			return true;

		int piece = EMPTY;
		if (white == 2) {
			switch (white_up + 2 * white_down + 4 * white_left + 8
					* white_right) {
				case 3:
					piece = NS;
					break;
				case 12:
					piece = WE;
					break;
				case 5:
					piece = NW;
					break;
				case 9:
					piece = NE;
					break;
				case 6:
					piece = WS;
					break;
				case 10:
					piece = SE;
					break;
			}
		} else { // right==2
			switch (black_up + 2 * black_down + 4 * black_left + 8
					* black_right) {
				case 12:
					piece = NS;
					break;
				case 3:
					piece = WE;
					break;
				case 10:
					piece = NW;
					break;
				case 6:
					piece = NE;
					break;
				case 9:
					piece = WS;
					break;
				case 5:
					piece = SE;
					break;
			}
		}
		putAt(brow, bcol, piece);
		return forcedMove(brow - 1, bcol) && forcedMove(brow + 1, bcol) && forcedMove(brow, bcol - 1) && forcedMove(brow, bcol + 1);
	}

	private int neighborValue(int x, int y){
		int value = 0;
		int up = getAt(x - 1, y);
		int down = getAt(x + 1, y);
		int left = getAt(x, y - 1);
		int right = getAt(x, y + 1);

		if (up == TraxBoard.SN || up == TraxBoard.SE || up == TraxBoard.SW) {
			value += 1;
		} /* ohs_up */
		if (up == TraxBoard.EW || up == TraxBoard.NW || up == TraxBoard.NE) {
			value += 16;
		} /* eks_up */
		if (down == TraxBoard.NS || down == TraxBoard.NE
				|| down == TraxBoard.NW) {
			value += 2;
		} /* ohs_down */
		if (down == TraxBoard.EW || down == TraxBoard.SW
				|| down == TraxBoard.SE) {
			value += 32;
		} /* eks_down */
		if (left == TraxBoard.EN || left == TraxBoard.ES
				|| left == TraxBoard.EW) {
			value += 4;
		} /* ohs_left */
		if (left == TraxBoard.WS || left == TraxBoard.WN
				|| left == TraxBoard.NS) {
			value += 64;
		} /* eks_left */
		if (right == TraxBoard.WN || right == TraxBoard.WE
				|| right == TraxBoard.WS) {
			value += 8;
		} /* ohs_right */
		if (right == TraxBoard.ES || right == TraxBoard.NS
				|| right == TraxBoard.EN) {
			value += 128;
		} /* eks.right */
		return value;
	}

	public ArrayList<Integer> getLegalTiles(int x, int y){
		ArrayList<Integer> result = new ArrayList<Integer>();
		if (boardEmpty) {
			result.add(TraxBoard.NW);
			result.add(TraxBoard.NS);
		}
		switch (neighborValue(x, y)) {
			case 0:
				return result;
			case 1:
				result.add(TraxBoard.NW);
				result.add(TraxBoard.NS);
				result.add(TraxBoard.NE);
				return result;
			case 128:
				result.add(TraxBoard.WS);
				result.add(TraxBoard.NS);
				result.add(TraxBoard.WN);
				return result;
			case 2:
				result.add(TraxBoard.SW);
				result.add(TraxBoard.SE);
				result.add(TraxBoard.SN);
				return result;
			case 32:
				result.add(TraxBoard.WE);
				result.add(TraxBoard.WN);
				result.add(TraxBoard.NE);
				return result;
			case 8:
				result.add(TraxBoard.EW);
				result.add(TraxBoard.ES);
				result.add(TraxBoard.EN);
				return result;
			case 4:
				result.add(TraxBoard.WE);
				result.add(TraxBoard.WS);
				result.add(TraxBoard.WN);
				return result;
			case 64:
				result.add(TraxBoard.NS);
				result.add(TraxBoard.NE);
				result.add(TraxBoard.SE);
				return result;
			case 16:
				result.add(TraxBoard.WE);
				result.add(TraxBoard.WS);
				result.add(TraxBoard.SE);
				return result;
			case 36:
				result.add(TraxBoard.WN);
				result.add(TraxBoard.WE);
				return result;
			case 66:
				result.add(TraxBoard.SN);
				result.add(TraxBoard.SE);
				return result;
			case 132:
				result.add(TraxBoard.WN);
				result.add(TraxBoard.WS);
				return result;
			case 72:
				result.add(TraxBoard.EN);
				result.add(TraxBoard.ES);
				return result;
			case 65:
				result.add(TraxBoard.NS);
				result.add(TraxBoard.NE);
				return result;
			case 20:
				result.add(TraxBoard.WE);
				result.add(TraxBoard.WS);
				return result;
			case 33:
				result.add(TraxBoard.NW);
				result.add(TraxBoard.NE);
				return result;
			case 18:
				result.add(TraxBoard.SW);
				result.add(TraxBoard.SE);
				return result;
			case 129:
				result.add(TraxBoard.NW);
				result.add(TraxBoard.NS);
				return result;
			case 24:
				result.add(TraxBoard.EW);
				result.add(TraxBoard.ES);
				return result;
			case 40:
				result.add(TraxBoard.EW);
				result.add(TraxBoard.EN);
				return result;
			case 130:
				result.add(TraxBoard.SN);
				result.add(TraxBoard.SW);
				return result;
			default:
				return null;
		}
	}

}

