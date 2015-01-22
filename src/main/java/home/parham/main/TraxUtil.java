/* 
   
   Date: 26th of Februar 2014
   version 0.1
   All source under GPL version 2 
   (GNU General Public License - http://www.gnu.org/)
   contact traxplayer@gmail.com for more information about this code
   
*/

package org.traxgame.main;

import java.util.Random;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public abstract class TraxUtil
{

    static boolean LOG=false;
    static Random random_generator;

    static
    {
	random_generator = new Random ();
    }
    
    public static int getRandom(int limit) {
	return random_generator.nextInt (limit);
    }

    public static void startLog() { LOG=true; }
    public static void stopLog() { LOG=false; }

    public static void log(String msg) {
	if (LOG) {
	    System.err.println(msg);
	}
    }
    
    public static String getRandomMove (Traxboard t) throws IllegalMoveException {
	String move;
	int losingMoves = 0;
	
	if (t.isGameOver()!=Traxboard.NOPLAYER) {
	    return new String ("");
	}
	ArrayList <String> moves=t.uniqueMoves();
	if (moves.size()==1) {
	    return moves.get (0);
	}
	ArrayList <String> moves_not_losing=new ArrayList <String>(moves.size());
	
	for (int i=0; i<moves.size(); i++) {
	    Traxboard t_copy=new Traxboard(t);
	    t_copy.makeMove(moves.get(i));
	    int gameOverValue =t_copy.isGameOver();
	    switch (gameOverValue) {
	    case Traxboard.WHITE:
	    case Traxboard.BLACK:
		if (t_copy.whoDidLastMove()==gameOverValue) {
		    log("Winning move found");
		    return (moves.get(i));	/* Winning move found */
		}
		/* losing move found */
	    losingMoves++;
	    log("Losing move found");
	    break;
	    case Traxboard.NOPLAYER:
	    case Traxboard.DRAW:
		moves_not_losing.add(moves.get(i));
		log("Not losing move found");
		log(moves_not_losing.toString());
		break;
	    default:
		/* This should never happen */
		assert (false);
	    }
	}
	if (moves_not_losing.size()==0) {
	    /* Only losing moves left */
	    log("Only losing moves left");
	    return moves.get (0);
	}
	return moves_not_losing.get(random_generator.nextInt(moves_not_losing.size()));
    }
    
    public static ArrayList <String> getInput() {
	ArrayList < String > result = new ArrayList < String > (10);
	String line;
	try {
	    line=new BufferedReader (new InputStreamReader (System.in)).readLine ();
	    if (line!=null) {
		StringTokenizer st = new StringTokenizer (line);
		while (st.hasMoreTokens ()) result.add (st.nextToken ());
	    }
	}
	catch (IOException e) {
	    e.printStackTrace ();
	}
	return result;
    }

    public static String reverseBorder(String border) {
      StringBuffer result=new StringBuffer(border.length());
      for (int i=0; i<border.length(); i++) {
	  switch (border.charAt(i)) {
	      case 'W': result.append('B'); break;
	      case 'B': result.append('W'); break;
	      case '+': result.append('+'); break;
	      case '-': result.append('-'); break;
	      default:
	        // This should never happen
		throw new IllegalArgumentException("This should never happen (032).");
	  }
      }
      return result.toString();
    }

    public static void main (String[]args) {
	//System.out.println (getInput ());
	startLog();
	
	Traxboard tb=new Traxboard();
	String move;
	try {
	    tb.makeMove("a1c");
	    tb.makeMove("b1d");
	    move=getRandomMove(tb);
	    System.out.println(tb+move);
	    tb.makeMove(move);
	    System.out.println(tb);
	    tb=new Traxboard();
	    tb.makeMove("a1c");
	    tb.makeMove("a1u");
	    System.out.println(tb.getBorder());
	    System.out.println(reverseBorder(tb.getBorder()));
	    move=getRandomMove(tb);
	    System.out.println(tb+move);
	    tb.makeMove(move);
	    System.out.println(tb);
	    System.out.println(tb.getBorder());
	    System.out.println(reverseBorder(tb.getBorder()));
	}
	catch (IllegalMoveException e) {
	    throw new AssertionError(e.getMessage());
	}
    }
}
