/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax 
 * 
 * [] Package Name : main.player
 *
 * [] Creation Date : 11-02-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/

package home.parham.trax.core.player;

public interface Player {
	String move(String otherPlayerMove);

	String getName();
}
