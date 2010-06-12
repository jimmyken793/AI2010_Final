package idv.jimmyken793.aiproj;

public class Unicode {
	public static boolean isChinese(String str) {
		int len=str.length();
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			if (!(c >= 0x4E00 && c <= 0x9FFF))
				return false;
		}
		return true;
	}
}
