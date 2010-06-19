package idv.jimmyken793.aiproj.data;

import idv.jimmyken793.io.FileIO;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YahooNews extends InputData {
	String filename;

	public YahooNews(String filename) {
		this.filename = filename;
	}

	@Override
	public String getData() throws IOException {
		String file = FileIO.readFile(filename, "UTF-8");
		return parseFile(file);
	}

	private String parseFile(String file) {
		Pattern p = Pattern.compile("<p>([^</p>]+)</p>");
		Matcher m = p.matcher(file);
		String out = new String();
		while (m.find()) {
			String t = Pattern.compile("<span>([^</span>]+)</span>", Pattern.CASE_INSENSITIVE).matcher(m.group(1)).replaceAll("-$1-");
			t = Pattern.compile("<a[^>]+>([^</a>]+)</a>", Pattern.CASE_INSENSITIVE).matcher(t).replaceAll("-$1-");
			out = out + " " + t;
		}
		return out;
	}

	@Override
	public String getName() {
		return filename;
	}

}
