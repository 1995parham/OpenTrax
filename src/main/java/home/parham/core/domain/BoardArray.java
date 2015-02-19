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

public class BoardArray {
	private ArrayList<ArrayList<TraxTiles>> board;
	private int size;

	public BoardArray(){
		size = 16;
		board = new ArrayList<ArrayList<TraxTiles>>(size);
		for (int i = 0; i < size; i++) {
			board.add(new ArrayList<TraxTiles>(size));
			for (int j = 0; j < size; j++) {
				board.get(i).add(TraxTiles.EMPTY);
			}
		}
	}

	public BoardArray(BoardArray org){
		size = org.size;
		board = new ArrayList<ArrayList<TraxTiles>>(size);
		for (int i = 0; i < size; i++) {
			board.add(new ArrayList<TraxTiles>(size));
			for (int j = 0; j < size; j++) {
				board.get(i).add(org.board.get(i).get(j));
			}
		}
	}

	public TraxTiles get(int row, int column){
		return board.get(row).get(column);
	}

	public void put(int row, int column, TraxTiles tile){
		/* Expand board if needed. */
		if (row > size) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (row == size) {
			expand();
		} else if (column > size) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (column == size) {
			expand();
		}

		board.get(row).set(column, tile);
	}

	public int size(){
		return size;
	}

	private void expand(){
		board.add(new ArrayList<TraxTiles>());
		for (int i = 0; i < size; i++) {
			board.get(i).add(TraxTiles.EMPTY);
		}
		for (int i = 0; i <= size; i++) {
			board.get(size).add(TraxTiles.EMPTY);
		}
		size++;
	}
}
