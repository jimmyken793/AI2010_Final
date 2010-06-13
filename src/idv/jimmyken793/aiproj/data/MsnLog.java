package idv.jimmyken793.aiproj.data;

import idv.jimmyken793.io.FileIO;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MsnLog extends InputData{
	private String filename;

	public MsnLog(String filename) {
		this.filename = filename;
	}

	@Override
	public String getData() throws IOException {
		String file = FileIO.readFile(filename,"UTF-16LE");
		Pattern p = Pattern.compile("<td[^>]*>([^<])*</td>");
		Matcher m = p.matcher(file);
		String out = new String();
		while (m.find()) {
			out = out + m.group(1) + " ";
		}

		return out;
	}

	public String getName() {
		return filename;
	}

}
