package idv.jimmyken793.aiproj;

import idv.jimmyken793.io.FileIO;

import java.io.IOException;
import java.sql.SQLException;

public class DataScanner {

	/**
	 * @param args
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		Database d = new Database();
		for (int i = 0; i < args.length; i++) {
			parseFile(args[i], d);
		}
		d.close();
	}

	public static void parseString(String dataString, Database d) throws IOException, ClassNotFoundException, SQLException {
		char[] data = (dataString.toCharArray());
		// System.out.println(data);
		if (data.length == 0)
			return;
		char chs0, chs1;
		chs0 = data[0];
		int length = data.length - 1;
		boolean prev = chs0 >= 0x4E00 && chs0 <= 0x9FFF;
		for (int i = 0; i < length; i++) {
			chs1 = data[i + 1];
			boolean cur = chs1 >= 0x4E00 && chs1 <= 0x9FFF;
			if (cur) {
				if (prev)
					d.addCount(chs0, chs1);
				chs0 = chs1;
				prev = cur;
			} else {
				chs0 = data[i + 1];
				prev = chs0 >= 0x4E00 && chs0 <= 0x9FFF;
				i++;
			}
		}
	}

	public static void parseFile(String filename, Database d) throws IOException, ClassNotFoundException, SQLException {
		String f = new String(FileIO.readFile(filename));
		System.out.println(f.getBytes().length + "\t" + filename);
		parseString(f, d);
	}
}
