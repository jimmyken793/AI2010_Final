package idv.jimmyken793.aiproj.data;

import java.io.IOException;

public class StringData extends InputData {
	private String data;
	public StringData(String data){
		this.data=data;
	}
	@Override
	public String getData() throws IOException {
		return data;
	}

	@Override
	public String getName() {
		return "StringData";
	}

}
