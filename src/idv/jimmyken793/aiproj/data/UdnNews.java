package idv.jimmyken793.aiproj.data;

import idv.jimmyken793.io.FileIO;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UdnNews extends InputData {
	String filename;

	public UdnNews(String filename) {
		this.filename = filename;
	}

	@Override
	public String getData() throws IOException {
		String file = FileIO.readFile(filename, "Big5");
		String out = "";
		Pattern p = Pattern.compile("window\\.location\\.href=\"([^\"]+jsp[^\"]+)\";");
		Matcher m = p.matcher(file);
		if (m.find()) {
			try {
				String efile = getUrl(m.group(1), "Big5");
				out = parseFile(efile);
			} catch (IOException e) {
				System.out.println("Download failed");
			}
		}
		out += " " + parseFile(file);

		return out;
	}

	private String parseFile(String file) {
		Pattern p = Pattern.compile("<[pP]>([^<]+)");
		Matcher m = p.matcher(file);
		String out = new String();
		while (m.find()) {
			out = out + m.group(1) + " ";
		}
		return out;
	}

	private static String getUrl(String url, String encoding) throws IOException {
		URL u = new URL(url);
		URLConnection uc;
		System.out.print("download: " + url);
		uc = u.openConnection();
		uc.setRequestProperty("User-Agent", " Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.4) Gecko/20100417 Ubuntu/10.04 (lucid) Firefox/3.6.4");
		uc.setRequestProperty("Accept", " text/html");
		uc.setRequestProperty("Accept-Language", "zh-tw,en-us;q=0.7,en;q=0.3");
		uc.setRequestProperty("Accept-Charset", "UTF-8,*");
		// uc.setRequestProperty("Keep-Alive", "115");
		// uc.setRequestProperty("Connection", "keep-alive");
		uc.setRequestProperty("Cache-Control", "max-age=0");
		uc.connect();
		InputStream is = uc.getInputStream();
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
		System.out.println(" " + size + " Bytes");
		return new String(out, encoding);

	}

	@Override
	public String getName() {
		return filename;
	}

}
