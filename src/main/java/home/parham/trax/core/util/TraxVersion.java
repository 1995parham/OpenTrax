/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax 
 * 
 * [] Package Name : home.parham.core.util
 *
 * [] Creation Date : 11-03-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/

package home.parham.trax.core.util;

public final class TraxVersion {

	private static String version = "V4.0";
	private static String versionName = "MSK";

	public static String getVersion(){
		return version + " (" + versionName + ")";
	}
}
