/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax 
 * 
 * [] Package Name : home.parham.main.main.player
 *
 * [] Creation Date : 11-02-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/

package home.parham.core.player;

import home.parham.core.domain.TraxBoard;

public interface Player {
	public String move(TraxBoard traxBoard);
}