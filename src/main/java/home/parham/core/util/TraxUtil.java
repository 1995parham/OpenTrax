/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax 
 * 
 * [] Package Name : home.parham.main.main.util
 *
 * [] Creation Date : 11-02-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/

package home.parham.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

public final class TraxUtil {

	static boolean LOG = false;
	static Random randomGenerator = new Random();

	public static int getRandom(int limit){
		return randomGenerator.nextInt(limit);
	}

	public static void startLog(){
		LOG = true;
	}

	public static void stopLog(){
		LOG = false;
	}

	public static void log(String msg){
		if (LOG) {
			System.err.println(msg);
		}
	}


	public static ArrayList<String> getInput(InputStream inputStream){
		ArrayList<String> result = new ArrayList<String>(5);
		String line;
		try {
			line = new BufferedReader(new InputStreamReader(inputStream)).readLine();
			if (line != null) {
				StringTokenizer st = new StringTokenizer(line);
				while (st.hasMoreTokens())
					result.add(st.nextToken());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static ArrayList<String> getInput(){
		return getInput(System.in);
	}

	public static String reverseBorder(String border){
		StringBuffer result = new StringBuffer(border.length());
		for (int i = 0; i < border.length(); i++) {
			switch (border.charAt(i)) {
				case 'W':
					result.append('B');
					break;
				case 'B':
					result.append('W');
					break;
				case '+':
					result.append('+');
					break;
				case '-':
					result.append('-');
					break;
				default:
					// This should never happen
					throw new IllegalArgumentException("This should never happen (032).");
			}
		}
		return result.toString();
	}

}

