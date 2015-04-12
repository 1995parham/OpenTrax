/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax 
 * 
 * [] Package Name : home.parham.core.domain
 *
 * [] Creation Date : 19-02-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/

package home.parham.trax.core.domain;

import java.util.ArrayList;
import java.util.List;

public class BoardArray {
	private List<List<TraxTiles>> board;

	public BoardArray(){
		int size = 1;
		board = new ArrayList<List<TraxTiles>>(size);
		for (int i = 0; i < size; i++) {
			board.add(new ArrayList<TraxTiles>(size));
			for (int j = 0; j < size; j++) {
				board.get(i).add(TraxTiles.EMPTY);
			}
		}
	}

	public BoardArray(BoardArray org){
		board = new ArrayList<List<TraxTiles>>(org.getRowSize());
		for (int i = 0; i < org.getRowSize(); i++) {
			board.add(new ArrayList<TraxTiles>(org.getColumnSize()));
			for (int j = 0; j < org.getColumnSize(); j++) {
				board.get(i).add(org.board.get(i).get(j));
			}
		}
	}

	public TraxTiles get(int row, int column){
		try {
			return board.get(row).get(column);
		} catch (IndexOutOfBoundsException e) {
			return TraxTiles.EMPTY;
		}
	}

	public void put(int row, int column, TraxTiles tile){
		/* Expand board if needed. */
		if (row == -1) {
			expandFromFirstRow();
			row++;
		} else if (row < -1) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (row > getRowSize()) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (row == getRowSize()) {
			expandFromLastRow();
		}
		if (column == -1) {
			expandFromFirstColumn();
			column++;
		} else if (column < -1) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (column > getColumnSize()) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (column == getColumnSize()) {
			expandFromLastColumn();
		}

		board.get(row).set(column, tile);
	}

	public int size(){
		return Math.max(getColumnSize(), getRowSize());
	}

	public int getColumnSize(){
		return board.get(0).size();
	}

	public int getRowSize(){
		return board.size();
	}

	private void expandFromLastRow(){
		board.add(new ArrayList<TraxTiles>());

		for (int i = 0; i < getColumnSize(); i++) {
			board.get(getRowSize() - 1).add(TraxTiles.EMPTY);
		}
	}

	private void expandFromLastColumn(){
		for (int i = 0; i < getRowSize(); i++) {
			board.get(i).add(TraxTiles.EMPTY);
		}
	}

	private void expandFromFirstRow(){
		int csize = getColumnSize();
		board.add(0, new ArrayList<TraxTiles>());

		for (int i = 0; i < csize; i++) {
			board.get(0).add(TraxTiles.EMPTY);
		}
	}

	private void expandFromFirstColumn(){
		for (int i = 0; i < getRowSize(); i++) {
			board.get(i).add(0, TraxTiles.EMPTY);
		}
	}

	@Override
	public String toString(){
		String retval = "";
		for (int i = 0; i < getRowSize(); i++) {
			retval += board.get(i).toString() + "\n";
		}
		return retval;
	}
}
