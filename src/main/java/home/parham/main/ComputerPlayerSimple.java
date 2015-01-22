package org.traxgame.main;

/* 
 Date: 18th of September 2009
 version 0.1
 All source under GPL version 2 
 (GNU General Public License - http://www.gnu.org/)
 contact traxplayer@gmail.com for more information about this code
 */

public class ComputerPlayerSimple extends ComputerPlayer {
	public String computerMove(Traxboard tb) {
		try {
			return TraxUtil.getRandomMove(tb);
		} catch (IllegalMoveException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
