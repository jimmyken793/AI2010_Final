package idv.jimmyken793.aiproj.data;

import idv.jimmyken793.io.FileIO;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PidginLog extends InputData{
	public static void main(String[] args) throws IOException {
		if(args.length>0)
			System.out.println(new PidginLog(args[0]).getData());
	}
	private String filename;

	public PidginLog(String filename) {
		this.filename = filename;
	}

	@Override
	public String getData() throws IOException {
		String file = FileIO.readFile(filename);
		Pattern p = Pattern.compile(">([^\n><]+)</span");
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
