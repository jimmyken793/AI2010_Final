package idv.jimmyken793.aiproj.chinese;

public class PhoneticSymbolSet {
	private Character[] data = new Character[4];

	public PhoneticSymbolSet(String data) {
		push(data);
	}

	public PhoneticSymbolSet() {

	}

	public void push(char in) {
		in = KeyToSymbol(in);
		int c = classify(in);
		switch (c) {
		case 0:
		case 1:
		case 2:
		case 3:
			data[c] = in;
		}
	}

	public void push(String in) {
		char[] d = KeyToSymbol(in).toCharArray();
		System.out.println(KeyToSymbol(in));
		for (int i = 0; i < d.length; i++) {
			push(d[i]);
		}
	}

	public void delete() {
		for (int i = 3; i >= 0; i--) {
			if (data[i] != null) {
				if (data[i] != ' ') {
					data[i] = null;
					break;
				}
				data[i] = null;
			}
		}
	}

	public void clear() {
		for (int i = 3; i >= 0; i--) {
			data[i] = null;
		}
	}

	public boolean isDone() {
		if (data[3] == null)
			return false;
		return true;
	}

	public String toString() {
		String out = "";
		for (int i = 0; i < 4; i++) {
			if (data[i] != null)
				out += data[i];
		}
		return out;
	}

	public String getKeys() {
		String out = "";
		for (int i = 0; i < 4; i++) {
			if (data[i] != null)
				out += SymbolToKey(data[i]);
		}
		return out;

	}

	public static String KeyToSymbol(String key) {
		char[] c = key.toCharArray();
		char[] o = new char[c.length];
		for (int i = 0; i < c.length; i++) {
			o[i] = KeyToSymbol(c[i]);
		}
		return new String(o);
	}

	private static int classify(char key) {
		switch (key) {
		case 'ㄅ':
		case 'ㄆ':
		case 'ㄇ':
		case 'ㄈ':
		case 'ㄉ':
		case 'ㄊ':
		case 'ㄋ':
		case 'ㄌ':
		case 'ㄍ':
		case 'ㄎ':
		case 'ㄏ':
		case 'ㄐ':
		case 'ㄑ':
		case 'ㄒ':
		case 'ㄓ':
		case 'ㄔ':
		case 'ㄕ':
		case 'ㄖ':
		case 'ㄗ':
		case 'ㄘ':
		case 'ㄙ':
			return 0;
		case 'ㄧ':
		case 'ㄨ':
		case 'ㄩ':
			return 1;
		case 'ㄚ':
		case 'ㄛ':
		case 'ㄜ':
		case 'ㄞ':
		case 'ㄤ':
		case 'ㄠ':
		case 'ㄝ':
		case 'ㄦ':
		case 'ㄟ':
		case 'ㄣ':
		case 'ㄡ':
		case 'ㄥ':
		case 'ㄢ':
			return 2;
		case ' ':
		case 'ˊ':
		case 'ˇ':
		case 'ˋ':
		case '˙':
			return 3;
		}
		return -1;
	}

	public static char KeyToSymbol(char key) {
		switch (key) {
		case ',':
			return 'ㄝ';
		case '-':
			return 'ㄦ';
		case '.':
			return 'ㄡ';
		case '/':
			return 'ㄥ';
		case '0':
			return 'ㄢ';
		case '1':
			return 'ㄅ';
		case '2':
			return 'ㄉ';
		case '3':
			return 'ˇ';
		case '4':
			return 'ˋ';
		case '5':
			return 'ㄓ';
		case '6':
			return 'ˊ';
		case '7':
			return '˙';
		case '8':
			return 'ㄚ';
		case '9':
			return 'ㄞ';
		case ';':
			return 'ㄤ';
		case 'a':
			return 'ㄇ';
		case 'b':
			return 'ㄖ';
		case 'c':
			return 'ㄏ';
		case 'd':
			return 'ㄎ';
		case 'e':
			return 'ㄍ';
		case 'f':
			return 'ㄑ';
		case 'g':
			return 'ㄕ';
		case 'h':
			return 'ㄘ';
		case 'i':
			return 'ㄛ';
		case 'j':
			return 'ㄨ';
		case 'k':
			return 'ㄜ';
		case 'l':
			return 'ㄠ';
		case 'm':
			return 'ㄩ';
		case 'n':
			return 'ㄙ';
		case 'o':
			return 'ㄟ';
		case 'p':
			return 'ㄣ';
		case 'q':
			return 'ㄆ';
		case 'r':
			return 'ㄐ';
		case 's':
			return 'ㄋ';
		case 't':
			return 'ㄔ';
		case 'u':
			return 'ㄧ';
		case 'v':
			return 'ㄒ';
		case 'w':
			return 'ㄊ';
		case 'x':
			return 'ㄌ';
		case 'y':
			return 'ㄗ';
		case 'z':
			return 'ㄈ';
		}
		return key;
	}

	public static char SymbolToKey(char key) {
		switch (key) {
		case 'ㄝ':
			return ',';
		case 'ㄦ':
			return '-';
		case 'ㄡ':
			return '.';
		case 'ㄥ':
			return '=';
		case 'ㄢ':
			return '0';
		case 'ㄅ':
			return '1';
		case 'ㄉ':
			return '2';
		case 'ˇ':
			return '3';
		case 'ˋ':
			return '4';
		case 'ㄓ':
			return '5';
		case 'ˊ':
			return '6';
		case '˙':
			return '7';
		case 'ㄚ':
			return '8';
		case 'ㄞ':
			return '9';
		case 'ㄤ':
			return ';';
		case 'ㄇ':
			return 'a';
		case 'ㄖ':
			return 'b';
		case 'ㄏ':
			return 'c';
		case 'ㄎ':
			return 'd';
		case 'ㄍ':
			return 'e';
		case 'ㄑ':
			return 'f';
		case 'ㄕ':
			return 'g';
		case 'ㄘ':
			return 'h';
		case 'ㄛ':
			return 'i';
		case 'ㄨ':
			return 'j';
		case 'ㄜ':
			return 'k';
		case 'ㄠ':
			return 'l';
		case 'ㄩ':
			return 'm';
		case 'ㄙ':
			return 'n';
		case 'ㄟ':
			return 'o';
		case 'ㄣ':
			return 'p';
		case 'ㄆ':
			return 'q';
		case 'ㄐ':
			return 'r';
		case 'ㄋ':
			return 's';
		case 'ㄔ':
			return 't';
		case 'ㄧ':
			return 'u';
		case 'ㄒ':
			return 'v';
		case 'ㄊ':
			return 'w';
		case 'ㄌ':
			return 'x';
		case 'ㄗ':
			return 'y';
		case 'ㄈ':
			return 'z';
		}
		return key;
	}
}
