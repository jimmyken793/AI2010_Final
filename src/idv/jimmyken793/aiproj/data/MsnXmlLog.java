package idv.jimmyken793.aiproj.data;

import idv.jimmyken793.io.FileIO;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MsnXmlLog extends InputData{
	private String filename;

	public MsnXmlLog(String filename) {
		this.filename = filename;
	}

	@Override
	public String getData() throws IOException {
		String file = FileIO.readFile(filename);
		Pattern p = Pattern.compile("<Text[^>]*>([^<])*</Text>");
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
