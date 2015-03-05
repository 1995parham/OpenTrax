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

package home.parham.core.domain;

import java.util.ArrayList;
import java.util.List;

public class BoardArray {
	private List<List<TraxTiles>> board;
	private int size;

	public BoardArray(){
		size = 1;
		board = new ArrayList<List<TraxTiles>>(size);
		for (int i = 0; i < size; i++) {
			board.add(new ArrayList<TraxTiles>(size));
			for (int j = 0; j < size; j++) {
				board.get(i).add(TraxTiles.EMPTY);
			}
		}
	}

	public BoardArray(BoardArray org){
		size = org.size;
		board = new ArrayList<List<TraxTiles>>(size);
		for (int i = 0; i < size; i++) {
			board.add(new ArrayList<TraxTiles>(size));
			for (int j = 0; j < size; j++) {
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
			expandFromBegin();
			row++;
			column++;
		} else if (column == -1) {
			expandFromBegin();
			column++;
			row++;
		} else if (column < -1) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (row < -1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		if (row > size) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (row == size) {
			expandFromEnd();
		} else if (column > size) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (column == size) {
			expandFromEnd();
		}

		board.get(row).set(column, tile);
	}

	public int size(){
		return size;
	}

	private void expandFromEnd(){
		board.add(new ArrayList<TraxTiles>());

		for (int i = 0; i < size; i++) {
			board.get(i).add(TraxTiles.EMPTY);
		}
		for (int i = 0; i <= size; i++) {
			board.get(size).add(TraxTiles.EMPTY);
		}
		size++;
	}

	private void expandFromBegin(){
		board.add(0, new ArrayList<TraxTiles>());

		for (int i = 1; i <= size; i++) {
			board.get(i).add(0, TraxTiles.EMPTY);
		}
		for (int i = 0; i <= size; i++) {
			board.get(0).add(TraxTiles.EMPTY);
		}
		size++;
	}
}
