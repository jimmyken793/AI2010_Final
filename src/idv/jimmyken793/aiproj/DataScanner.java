package idv.jimmyken793.aiproj;

import idv.jimmyken793.aiproj.data.Data;
import idv.jimmyken793.aiproj.database.Database;

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
		if (args.length <= 1)
			return;
		Database d = new Database();
		for (int i = 1; i < args.length; i++) {
			parseFile(Data.constructData(args[0], args[i]), d);
		}
		d.close();
		// System.out.print(new
		// PidginConversationLog("/home/jimmy/.purple/logs/msn/jimmyken793@hotmail.com/pe19900829@hotmail.com/2010-06-08.010021+0800CST.html").getData());
		System.out.println("Done");
	}

	public static void parseString(String dataString, Database d) throws IOException, ClassNotFoundException, SQLException {
		// System.out.println(dataString);
		char[] data = (dataString.toCharArray());
		if (data.length == 0)
			return;
		int length = data.length - 1;
		for (int i = 0; i < length - 1; i++) {
			String w2 = dataString.substring(i, i + 2);
			if (Unicode.isChinese(w2)) {
				d.addCount(w2);
			}
			if (i != length - 2) {
				String w3 = dataString.substring(i, i + 3);
				if (Unicode.isChinese(w3)) {
					d.addCount(w3);
				}
			}
		}
	}

	public static void parseFile(Data file, Database d) throws IOException, ClassNotFoundException, SQLException {

		String f = file.getData();
		System.out.println(f.length() + "\t" + file.getName());
		parseString(f, d);
	}
}
