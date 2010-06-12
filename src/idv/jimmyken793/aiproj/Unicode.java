package idv.jimmyken793.aiproj;

public class Unicode {
	public static boolean isChinese(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (!(c >= 0x4E00 && c <= 0x9FFF))
				return false;
		}
		return true;
	}
}
