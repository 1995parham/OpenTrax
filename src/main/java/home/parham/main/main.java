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

package home.parham.main;

import home.parham.cli.Commands;
import home.parham.cli.TraxCLI;

public class main {
	public static void main(String args[]){
		Commands.welcome();

		new TraxCLI().run();
	}
}
