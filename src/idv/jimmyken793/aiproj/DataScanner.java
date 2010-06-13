package idv.jimmyken793.aiproj;

import idv.jimmyken793.aiproj.data.InputData;
import idv.jimmyken793.aiproj.database.Database;
import idv.jimmyken793.io.FileIO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * @author jimmy
 * 
 */
public class DataScanner {
	/**
	 * 
	 * @param args
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		if (args.length == 0) {
			System.out.println();
			System.out.println("Usage:");
			System.out.println("java -jar Datascanner.jar -Data_Mode Data_path_list [-Data_Mode Data_path_list]");
			System.out.println("Data modes:");
			System.out.println("PidginLog : pidgin conversation log");
			System.out.println("MsnLog : MSN Converstion Log");
			System.out.println("MsnXmlLog : MSN Conversation Log(XML Format)");
			System.out.println("UdnNews : UDN News page");
			System.out.println("Data : Raw data");
			System.out.println();
			return;
		}

		Database d = new Database();
		String className = "Data";
		for (int i = 0; i < args.length; i++) {
			if (args[i].charAt(0) == '-') {
				className = args[i].substring(1, args[i].length());
				System.out.println("Changed into " + className + " mode");
				continue;
			}
			ArrayList<String> files = FileIO.FileList(args[i]);
			Iterator<String> it = files.iterator();
			while (it.hasNext()) {
				String filename = it.next();
				parseFile(InputData.constructData(className, filename), d);
			}

		}
		d.close();
	}

	public static void parseFile(InputData inputData, Database d) throws IOException, ClassNotFoundException, SQLException {

		String f = inputData.getData();
		System.out.println(f.length() + "\t" + inputData.getName());
		parseString(f, d);
	}

	public static void parseString(String dataString, Database d) throws IOException, ClassNotFoundException, SQLException {
		// System.out.println(dataString);
		char[] data = (dataString.toCharArray());
		if (data.length == 0)
			return;
		int length = data.length - 1;
		int spos = -1;
		for (int i = 0; i < length - 1; i++) {
			String w2 = dataString.substring(i, i + 2);
			if (Unicode.isChinese(w2)) {
				d.countWord(w2);
				if (spos == -1) {
					spos = i;
				}
			} else {
				if (spos != -1) {
					d.countScentence(dataString.substring(spos, i + 1));
					spos = -1;
				}
			}
			if (i != length - 2) {
				String w3 = dataString.substring(i, i + 3);
				if (Unicode.isChinese(w3)) {
					d.countWord(w3);
				}
			}
		}
	}
}
