package idv.jimmyken793.aiproj.data;

import idv.jimmyken793.io.FileIO;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UdnNews extends InputData {
	String filename;

	public UdnNews(String filename) {
		this.filename = filename;
	}

	@Override
	public String getData() throws IOException {
		String file = FileIO.readFile(filename,"Big5");
		System.out.println(file.length());
		Pattern p = Pattern.compile("<[pP]>([^<]+)");
		Matcher m = p.matcher(file);
		String out = new String();
		while (m.find()) {
			out = out + m.group(1) + " ";
		}
		return out;
	}

	@Override
	public String getName() {
		return filename;
	}

}
