package idv.jimmyken793.aiproj.data;

import idv.jimmyken793.io.FileIO;

import java.io.IOException;
import java.lang.reflect.Constructor;
/**
 * 
 * @author jimmy
 *
 */
public class Data extends InputData{

	private String filename;

	public Data(String filename) {
		this.filename = filename;
	}

	public String getData() throws IOException {
		return FileIO.readFile(filename);
	}

	public String getName() {
		return filename;
	}
}
