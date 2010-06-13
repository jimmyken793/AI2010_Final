package idv.jimmyken793.aiproj.data;

import java.io.IOException;
import java.lang.reflect.Constructor;

public abstract class InputData {
	public abstract String getData() throws IOException;

	@SuppressWarnings("unchecked")
	public static InputData constructData(String classname, String filename) {
		try {
			Class<InputData> cl = (Class<InputData>) Class.forName("idv.jimmyken793.aiproj.data." + classname);
			Class<InputData> cl1 = (Class<InputData>) Class.forName("idv.jimmyken793.aiproj.data.Data");
			Constructor<InputData> constructor;
			try {
				constructor = cl.getConstructor(new Class[] { String.class });
			} catch (Exception exc) {
				constructor = cl1.getConstructor(new Class[] { String.class });
			}
			InputData handler = constructor.newInstance(new Object[] { filename });
			return handler;
		} catch (Exception exc) {
			System.err.println("create instance of " + classname + " failed");
			exc.printStackTrace();
			return null;
		}
	}

	public abstract String getName();
}
