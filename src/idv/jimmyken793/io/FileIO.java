package idv.jimmyken793.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
/**
 * 
 * @author jimmy
 *
 */
public class FileIO {
	public static void saveFile(String filename, byte[] content) throws IOException {
		File f = new File(filename);
		if (f.exists()) {
			f.delete();
		}
		FileOutputStream fs = new FileOutputStream(filename);
		fs.write(content);
		fs.close();
	}

	public static String readFile(String filename) throws IOException {
		FileInputStream is = new FileInputStream(filename);
		byte[] buf = new byte[65535];
		byte[] content = new byte[65535];
		int ch;
		int size = 0;
		while (true) {
			ch = is.read(buf);
			if (ch > 0) {
				if (size + ch > content.length) {
					byte[] tmp = new byte[content.length + 65535];
					System.arraycopy(content, 0, tmp, 0, size);
					content = tmp;
				}
				System.arraycopy(buf, 0, content, size, ch);
				size += ch;
			} else {
				break;
			}
		}
		byte[] out = new byte[size];
		System.arraycopy(content, 0, out, 0, size);
		is.close();
		return new String(out);
	}

	public static void ensureDirectory(String filename) {
		File f = new File(filename);
		if (!f.mkdirs()) {
			f.delete();
			f.mkdirs();
		}
	}

	public static ArrayList<String> FileList(String filename){
		ArrayList<String> list=new ArrayList<String>();
		File f=new File(filename);
		if(f.isDirectory()){
			File[] childs=f.listFiles();
			for(int i=0;i<childs.length;i++){
				list.addAll(FileList(childs[i].toString()));
			}
		}else if(f.isFile()){
			list.add(filename);
		}
		return list;
	}
}
