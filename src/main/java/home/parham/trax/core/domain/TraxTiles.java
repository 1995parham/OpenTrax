/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax 
 * 
 * [] Package Name : main.domain
 *
 * [] Creation Date : 11-02-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/

package home.parham.trax.core.domain;

public enum TraxTiles {
	EMPTY(0), INVALID(7),
	NS(1), SN(1), WE(2), EW(2),
	NW(3), WN(3), NE(4), EN(4),
	WS(5), SW(5), SE(6), ES(6);

	int number;

	TraxTiles(int number){
		this.number = number;
	}

	@Override
	public String toString(){
		return this.name();
	}

	public static TraxTiles tilesFromNumber(int number){
		switch (number) {
			case 0:
				return EMPTY;
			case 1:
				return NS;
			case 2:
				return WE;
			case 3:
				return NW;
			case 4:
				return NE;
			case 5:
				return WS;
			case 6:
				return SE;
			default:
				return INVALID;
		}
		/*
		 * EMPTY = 0, INVALID = 7
		 * NS = 1, SN = 1, WE = 2, EW = 2
		 * NW = 3, WN = 3, NE = 4, EN = 4
		 * WS = 5, SW = 5, SE = 6, ES = 6
		*/
	}
}
