/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax 
 * 
 * [] Package Name : home.parham.core.domain
 *
 * [] Creation Date : 21-02-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/

package home.parham.trax.core.domain;

public final class ColumnRowGenerator {

	public static String generate(int i, int j){
		String retval;

		if (i == 0) {
			retval = "@";
		} else {
			String column = "";
			do {
				i--;
				column += (char) ((i % 26) + 'A');
				i /= 26;
			} while (i != 0);
			retval = new StringBuilder(column).reverse().toString();
		}

		retval += j;

		return retval;
	}

}
