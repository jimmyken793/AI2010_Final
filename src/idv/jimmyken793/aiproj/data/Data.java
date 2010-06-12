package idv.jimmyken793.aiproj.data;

import idv.jimmyken793.io.FileIO;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class Data {
	private String filename;

	@SuppressWarnings("unchecked")
	public static Data constructData(String classname, String filename) {
		try {
			Class<Data> cl = (Class<Data>) Class.forName("idv.jimmyken793.aiproj.data." + classname);
			Class<Data> cl1 = (Class<Data>) Class.forName("idv.jimmyken793.aiproj.data.Data");
			Constructor<Data> constructor;
			try {
				constructor = cl.getConstructor(new Class[] { String.class });
			} catch (Exception exc) {
				constructor = cl1.getConstructor(new Class[] { String.class });
			}
			Data handler = (Data) constructor.newInstance(new Object[] { filename });
			return handler;
		} catch (Exception exc) {
			System.err.println("create Event " + classname + " failed");
			exc.printStackTrace();
			return null;
		}
	}

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
