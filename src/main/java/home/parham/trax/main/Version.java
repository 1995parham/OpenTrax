package home.parham.trax.main;

public final class Version {

	private static String version = "V4.1";
	private static String versionName = "MSK";

	public static String getVersion(){
		return version + " (" + versionName + ")";
	}
}
