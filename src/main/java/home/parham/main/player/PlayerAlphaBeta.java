package home.parham.main.player;

/* 
 Date: 23th of October 2009
 version 0.1
 All source under GPL version 2
 (GNU General Public License - http://www.gnu.org/)
 contact traxplayer@gmail.com for more information about this code
*/

import home.parham.main.TraxBoard;

public class PlayerAlphaBeta implements Player {
	private int whiteCorners;
	private int blackCorners;

	public PlayerAlphaBeta(){
		whiteCorners = -1; // undefined
		blackCorners = -1; // undefined
	}

	public String move(TraxBoard tb){
		return new String();
	}
}
