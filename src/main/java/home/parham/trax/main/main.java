/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax 
 * 
 * [] Package Name : home.parham.cli
 *
 * [] Creation Date : 11-02-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/

package home.parham.trax.main;

import home.parham.trax.cli.Commands;
import home.parham.trax.gui.GnuTraxGui;

public class main {
	public static void main(String args[]){
		Commands.welcome();

		GnuTraxGui.createAndShowGUI();
	}
}
