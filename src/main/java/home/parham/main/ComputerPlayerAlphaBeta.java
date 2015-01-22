package org.traxgame.main;

/* 
 Date: 23th of October 2009
 version 0.1
 All source under GPL version 2
 (GNU General Public License - http://www.gnu.org/)
 contact traxplayer@gmail.com for more information about this code
 */

import java.util.ArrayList;

public class ComputerPlayerAlphaBeta extends ComputerPlayer {
	private int whiteCorners;
	private int blackCorners;

	public ComputerPlayerAlphaBeta() {
		whiteCorners = -1; // undefined
		blackCorners = -1; // undefined
	}

	public String computerMove(Traxboard tb) {
		return new String();
	}

	/*
	 * private void countCorners(Traxboard tb) { String
	 * border=tb.getBorders(false); int state=0;
	 * 
	 * for (int i=0; i<border.length(); i++) { switch (state) { case 0: switch
	 * (border[i]) { case 'W': state=1; break; case 'B': state=12; break;
	 * default: // This should never happen assert(false); break; } break; case
	 * 1: switch (border[i]) { case 'W': break; case 'B': state=9; break; case
	 * '+': state=0; break; case '-': state=2; break; default: // This should
	 * never happen assert(0); break; } break; case 2: switch (border[i]) { case
	 * 'W': state=1; checkCorner(tb,row,col,direction,"W-W",white); break; case
	 * 'B': state=3; break; default: // This should never happen assert(0);
	 * break; } break; case 3: switch (border[i]) { case 'W': state=6;
	 * checkCorner(tb,row,col,direction,"W-BW",white); break; case 'B':
	 * state=12; break; case '+': state=0; break; case '-': state=4; break;
	 * default: // This should never happen assert(0); break; } break; case 4:
	 * switch (border[i]) { case 'W': state=5; break; case 'B': state=12;
	 * checkCorner(tb,row,col,direction,"B-B",black); break; default: // This
	 * should never happen assert(0); break; } break; case 5: switch (border[i])
	 * { case 'W': state=1; break; case 'B': state=9;
	 * checkCorner(tb,row,col,direction,"B-WB",black); break; case '+': state=0;
	 * break; case '-': state=2; break; default: // This should never happen
	 * assert(0); break; } break; case 6: switch (border[i]) { case 'W':
	 * state=1; break; case 'B': state=9; break; case '+': state=0; break; case
	 * '-': state=7; break; default: // This should never happen assert(0);
	 * break; } break; case 7: switch (border[i]) { case 'W': state=8;
	 * checkCorner(tb,row,col,direction,"W-W",white); break; case 'B': state=3;
	 * checkCorner(tb,row,col,direction,"BW-B",black); break; default: // This
	 * should never happen assert(0); break; } break; case 8: switch (border[i])
	 * { case 'W': state=1; break; case 'B': state=9;
	 * checkCorner(tb,row,col,direction,"BW-WB",black); break; case '+':
	 * state=0; break; case '-': state=2; break; default: // This should never
	 * happen assert(0); break; } break; case 9: switch (border[i]) { case 'W':
	 * state=6; break; case 'B': state=12; break; case '+': state=0; break; case
	 * '-': state=10; break; default: // This should never happen assert(0);
	 * break; } break; case 10: switch (border[i]) { case 'W': state=5;
	 * checkCorner(tb,row,col,direction,"WB-W",white); break; case 'B':
	 * state=11; checkCorner(tb,row,col,direction,"B-B",black); break; default:
	 * // This should never happen assert(0); break; } break; case 11: switch
	 * (border[i]) { case 'W': state=6;
	 * checkCorner(tb,row,col,direction,"WB-BW",white); break; case 'B':
	 * state=12; break; case '+': state=0; break; case '-': state=4; break;
	 * default: // This should never happen assert(0); break; } break; case 12:
	 * switch (border[i]) { case 'W': state=6; break; case 'B': state=12; break;
	 * case '+': state=0; break; case '-': state=4; break; default: // This
	 * should never happen assert(0); break; } break; default: // This should
	 * never happen assert(0); break; } } }
	 */
}
