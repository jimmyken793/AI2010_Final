package idv.jimmyken793.aiproj.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class Database {
	protected Connection connection;
	protected Statement statement;
	private PreparedStatement insertData;
	private PreparedStatement increData;
	private HashMap<String, String> insertion = new HashMap<String, String>();
	private HashSet<String> existed_word = new HashSet<String>();
	private HashMap<String, Integer> increment = new HashMap<String, Integer>();

	public Database() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		// Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:sqlite:database.db");
		// connection =
		// DriverManager.getConnection("jdbc:mysql://localhost/AI_Final?useUnicode=true&characterEncoding=Utf8",
		// "ai", "12345");
		connection.setAutoCommit(false);
		statement = connection.createStatement();
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS Data (id INTEGER,count INTEGER,str TEXT PRIMARY KEY)");
		insertData = connection.prepareStatement("INSERT OR REPLACE INTO Data(count,str) VALUES (?,?);");
		increData = connection.prepareStatement("UPDATE Data SET count = count+? WHERE str = ?;");
		ResultSet rs = statement.executeQuery("select * from Data;");
		while (rs.next()) {
			existed_word.add(rs.getString("str"));
			increment.put(rs.getString("str"), 0);
		}
	}

	static final int SIZE = 0x9FFF - 0x4E00 + 1;

	public static int processStr(String str) {
		if (str.length() == 2)
			return (str.charAt(0) - 0x4E00) * SIZE + (str.charAt(1) - 0x4E00);
		else if (str.length() == 3)
			return (str.charAt(0) - 0x4E00) * SIZE * SIZE + (str.charAt(1) - 0x4E00) * SIZE + (str.charAt(2) - 0x4E00);
		else
			return 0;
	}

	public void addCount(String id) throws SQLException {
		if (!existed_word.contains(id)) {
			insertion.put(id, id);
			increment.put(id, 1);
			existed_word.add(id);
		} else {
			increment.put(id, increment.get(id) + 1);
		}
	}

	public void close() throws SQLException {
		{
			Set<Entry<String, String>> ent = insertion.entrySet();
			System.out.println("Processing " + ent.size() + " entries");
			Iterator<Entry<String, String>> it = ent.iterator();
			if (it.hasNext()) {
				for (Entry<String, String> i = it.next(); it.hasNext(); i = it.next()) {
					insertData.setInt(1, increment.get(i.getKey()));
					insertData.setString(2, i.getValue());
					insertData.addBatch();
					increment.remove(i.getKey());
				}
				insertData.executeBatch();
			} else {

			}
		}
		{
			Set<Entry<String, Integer>> ent = increment.entrySet();
			System.out.println("Processing " + ent.size() + " entries");
			Iterator<Entry<String, Integer>> it = ent.iterator();
			if (it.hasNext()) {
				for (Entry<String, Integer> i = it.next(); it.hasNext(); i = it.next()) {
					if (i.getValue() != 0) {
						increData.setInt(1, i.getValue());
						increData.setString(2, i.getKey());
						increData.addBatch();
					}
				}
			}
		}
		increData.executeBatch();
		connection.commit();
		connection.close();
	}

	public void dump() throws SQLException {
		Statement stat = connection.createStatement();
		ResultSet rs = stat.executeQuery("select * from Data;");
		while (rs.next()) {
			System.out.println("id = " + rs.getInt("id"));
			System.out.println("count = " + rs.getInt("count"));
			System.out.println("Str = " + rs.getString("str"));
		}
		rs.close();
	}
}
