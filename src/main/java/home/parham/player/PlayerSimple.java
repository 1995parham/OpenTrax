package home.parham.player;

/* 
 Date: 18th of September 2009
 version 0.1
 All source under GPL version 2 
 (GNU General Public License - http://www.gnu.org/)
 contact traxplayer@gmail.com for more information about this code
 */

import home.parham.domain.TraxBoard;
import home.parham.exceptions.IllegalMoveException;
import home.parham.util.TraxUtil;

public class PlayerSimple implements Player {
	public String move(TraxBoard tb){
		try {
			return TraxUtil.getRandomMove(tb);
		} catch (IllegalMoveException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
