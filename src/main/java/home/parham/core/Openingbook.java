/* 

 Date: July 9 - 2014
 version 0.1
 All source under GPL version 2 
 (GNU General Public License - http://www.gnu.org/)
 contact traxplayer@gmail.com for more information about this code
*/

package home.parham.core;

import home.parham.core.domain.TraxBoard;
import home.parham.core.domain.TraxStatus;
import home.parham.core.exceptions.IllegalMoveException;
import home.parham.core.util.TraxUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Openingbook {

	private String inputfile;
	private HashMap<BookKey, BookValue> theBook;

	public Openingbook(){
		theBook = new HashMap<BookKey, BookValue>();
	}

	public int size(){
		return theBook.size();
	}

	public void setInputfile(String inputfile){
		this.inputfile = inputfile;
	}

	public void generateBook() throws IOException{
		this.generateBook(false, false);
	}

	public void generateBook(boolean always, boolean never) throws IOException{
		String s;
		TraxBoard tb;
		//int lineno=0;
		BufferedReader reader = new BufferedReader(new FileReader(new File(inputfile)));
		if ((always == false) && (never == false)) {
			while ((s = reader.readLine()) != null) {
				//lineno++;
				tb = new TraxBoard();
				boolean useless = false;
				boolean resign = false;
				try {
					for (String move : s.split("\\s")) {
						if (move.equalsIgnoreCase("resign")) {
							resign = true;
							break;
						}
						tb.makeMove(move);
					}
				} catch (IllegalMoveException e) {
					useless = true;
				}
				if (!resign && tb.isGameOver() == TraxStatus.NOPLAYER) {
					useless = true;
				}
				if (useless) {
					continue;
				}
				TraxStatus gameOverValue;
				if (resign) {
					gameOverValue = tb.whoDidLastMove();
				} else {
					gameOverValue = tb.isGameOver();
				}
				tb = new TraxBoard();
				for (String move : s.split("\\s")) {
					try {
						if (move.equalsIgnoreCase("resign")) {
							break;
						}
						tb.makeMove(move);
					} catch (IllegalMoveException e) {
						// This should never happen
						throw new RuntimeException(e);
					}
					BookKey bk = new BookKey(tb.getBorder(), tb.whoToMove());
					BookValue bv = search(tb);
					if (bv == null) {
						bv = new BookValue();
					}
					updateBook(tb, bv, gameOverValue);
				}
			}
		}
		if ((always == true) || (never == true)) {
			while ((s = reader.readLine()) != null) {
				tb = new TraxBoard();
				try {
					for (String move : s.split("\\s")) {
						tb.makeMove(move);
					}
					BookKey bk = new BookKey(tb.getBorder(), tb.whoDidLastMove());

					theBook.remove(bk);
					BookValue bv = new BookValue();
					bv.alwaysPlay = always;
					bv.neverPlay = never;
					theBook.put(bk, bv);
				} catch (IllegalMoveException e) {
					e.printStackTrace();
				}
			}
		}
		reader.close();
	}

	private void updateBook(TraxBoard tb, BookValue bv, TraxStatus gameOverValue){
		/* Need to find the right bookKey if any exist */
		BookKey bk1;
		BookKey bk2;


		for (int i = 1; i <= 4; i++) {
			bk1 = new BookKey(tb.getBorder(), tb.whoToMove());
			if (theBook.get(bk1) != null) {
				switch (gameOverValue) {
					case WHITE:
						bv.white++;
						break;
					case BLACK:
						bv.black++;
						break;
					case DRAW:
						bv.draw++;
						break;
					default: /* This should never happen */
						throw new RuntimeException("This should never happen (055)");
				}
				theBook.put(bk1, bv);
				return;

			}
			if (i < 4) tb = tb.rotate();
		}
		String newBorder = TraxUtil.reverseBorder(tb.getBorder());
		/*TODO How to handle tb.whoToMove() == TraxBoard.NOPLAYER ? Is that a problem ? */
		TraxStatus newWTM = (tb.whoToMove() == TraxStatus.WHITE) ? TraxStatus.BLACK : TraxStatus.WHITE;
		for (int i = 1; i <= 4; i++) {
			bk2 = new BookKey(newBorder, newWTM);
			if (theBook.get(bk2) != null) {
				switch (gameOverValue) {
					case WHITE:
						bv.black++;
						break;
					case BLACK:
						bv.white++;
						break;
					case DRAW:
						bv.draw++;
						break;
					default: // This should never happen
						throw new RuntimeException("This should never happen (056)");
				}
				theBook.put(bk2, bv);
				return;
			}
			if (i < 4) {
				tb = tb.rotate();
				newBorder = TraxUtil.reverseBorder(tb.getBorder());
			}
		}
		theBook.put(new BookKey(tb.getBorder(), tb.whoToMove()), bv);
	}

	public void loadBook() throws IOException{
		loadBook("url");
	}

	public void loadBook(String from) throws IOException{
		String s;
		BufferedReader reader;

		if (from.equals("url")) {
			try {
				reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResource("games/book.bin").openStream()));
			} catch (NullPointerException e) {
				throw new IOException(e);
			}

		} else {
			reader = new BufferedReader(new FileReader(new File("games/book.bin")));
		}
		theBook = new HashMap<BookKey, BookValue>();
		while ((s = reader.readLine()) != null) {
			String[] elems = s.split("\\s");
			if (elems.length == 7) {
				BookKey bk = new BookKey(elems[1], elems[0]);
				BookValue bv = new BookValue(elems[2], elems[3], elems[4], elems[5], elems[6]);
				theBook.put(bk, bv);
			}
		}
		reader.close();
	}

	private void saveBook(int threshold) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("games/book.bin")));
		for (Map.Entry<BookKey, BookValue> entry : theBook.entrySet()) {
			if ((entry.getValue().neverPlay) || (entry.getValue().alwaysPlay) || (entry.getValue().white + entry.getValue().black + entry.getValue().draw >= threshold)) {
				writer.write(entry.getKey() + " " + entry.getValue() + "\n");
			}
		}
		writer.close();
	}

	public String toString(){
		StringBuffer result = new StringBuffer();
		for (Map.Entry<BookKey, BookValue> entry : theBook.entrySet()) {
			result.append(entry.getKey() + " " + entry.getValue() + "\n");
		}
		return result.toString();
	}

	public BookValue search(TraxBoard tb){
		BookValue result;

		result = theBook.get(new BookKey(tb.getBorder(), tb.whoDidLastMove()));
		return result;
	}

	public class BookValue {
		public int black, white, draw;
		public boolean alwaysPlay;
		public boolean neverPlay;

		public int score(TraxStatus wtm){
			if (alwaysPlay) return Integer.MAX_VALUE;
			if (neverPlay) return Integer.MIN_VALUE;
			if (wtm == TraxStatus.WHITE) return (TraxUtil.getRandom(50) + 1000 * black / (black + white + draw + 1));
			return (TraxUtil.getRandom(50) + 1000 * white / (black + white + draw + 1));
		}

		public BookValue(){
			this(false, false);
		}

		public BookValue(boolean alwaysPlay, boolean neverPlay, int black, int white, int draw){
			this.alwaysPlay = alwaysPlay;
			this.neverPlay = neverPlay;
			this.black = black;
			this.white = white;
			this.draw = draw;
		}

		public BookValue(String alwaysPlay, String neverPlay, String black, String white, String draw){
			this(alwaysPlay, neverPlay, Integer.parseInt(black), Integer.parseInt(white), Integer.parseInt(draw));
		}

		public BookValue(String alwaysPlay, String neverPlay, int black, int white, int draw){
			this.alwaysPlay = (alwaysPlay.equals("false") ? false : true);
			this.neverPlay = (neverPlay.equals("false") ? false : true);
			this.black = black;
			this.white = white;
			this.draw = draw;
		}

		public BookValue(boolean alwaysPlay, boolean neverPlay){
			this(alwaysPlay, neverPlay, 0, 0, 0);
		}

		public String toString(){
			return ((alwaysPlay ? "true" : "false") + " " + (neverPlay ? "true" : "false") + " " + black + " " + white + " " + draw);
		}
	}

	private class BookKey {
		public TraxStatus wtm;
		public String border;

		public String toString(){
			return wtm + " " + border;
		}

		public BookKey reverse(){
			return new BookKey(TraxUtil.reverseBorder(border), wtm == TraxStatus.WHITE ? TraxStatus.BLACK : TraxStatus.WHITE);
		}

		public BookKey(String border, TraxStatus wtm){
			this.wtm = wtm;
			this.border = border;
		}

		public BookKey(String border, String wtm){
			this(border, TraxStatus.valueOf(wtm));
		}

		public BookKey(TraxBoard tb){
			this(tb.getBorder(), tb.whoToMove());
		}

		public int hashCode(){
			return border.hashCode() + wtm.hashCode();
		}

		public boolean equals(Object obj){
			if (this == obj) {
				return true;
			}
			if ((obj == null) || (obj.getClass() != this.getClass())) {
				return false;
			}
			BookKey oe = (BookKey) obj;
			return (oe.wtm == this.wtm && oe.border.equals(this.border));
		}
	}
}