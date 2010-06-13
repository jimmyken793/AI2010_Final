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
/**
 * 
 * @author jimmy
 *
 */
public class Database {
	private class CounterCache {
		String tableName;
		private Statement statement;
		private PreparedStatement insertData;
		private PreparedStatement increData;
		private HashSet<String> existed_word = new HashSet<String>();
		private HashSet<String> insertion = new HashSet<String>();

		private HashMap<String, Integer> increment = new HashMap<String, Integer>();

		public CounterCache(String table, Connection connection) throws SQLException {
			statement = connection.createStatement();
			tableName = table;
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS Data2 (count INTEGER,str TEXT PRIMARY KEY)");
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS Data3 (count INTEGER,str TEXT PRIMARY KEY)");
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS DataS (count INTEGER,str TEXT PRIMARY KEY)");
			insertData = connection.prepareStatement("INSERT OR REPLACE INTO " + tableName + "(count,str) VALUES (?,?);");
			increData = connection.prepareStatement("UPDATE " + tableName + " SET count = count+? WHERE str = ?;");
			ResultSet rs = statement.executeQuery("select * from " + tableName + ";");
			while (rs.next()) {
				existed_word.add(rs.getString("str"));
				increment.put(rs.getString("str"), 0);
			}
		}

		public void addCount(String id) {
			if (!existed_word.contains(id)) {
				insertion.add(id);
				increment.put(id, 1);
				existed_word.add(id);
			} else {
				increment.put(id, increment.get(id) + 1);
			}
		}

		public void flush() throws SQLException {
			{
				System.out.println("Processing " + insertion.size() + " INSERT entries");
				Iterator<String> it = insertion.iterator();
				int c = 0;
				if (it.hasNext()) {
					for (String i = it.next(); it.hasNext(); i = it.next()) {
						insertData.setInt(1, increment.get(i));
						insertData.setString(2, i);
						insertData.addBatch();
						increment.put(i, 0);
						c++;
					}
					insertion.clear();
				}
				System.out.println("Processed " + c + " INSERT entries");
			}
			insertData.executeBatch();
			{
				Set<Entry<String, Integer>> ent = increment.entrySet();
				System.out.println("Processing " + ent.size() + " UPDATE entries");
				Iterator<Entry<String, Integer>> it = ent.iterator();
				int c = 0;
				if (it.hasNext()) {
					for (Entry<String, Integer> i = it.next(); it.hasNext(); i = it.next()) {
						if (i.getValue() != 0) {
							increData.setInt(1, i.getValue());
							increData.setString(2, i.getKey());
							increData.addBatch();
							increment.put(i.getKey(), 0);
							c++;
						}
					}
				}

				System.out.println("Processed " + c + " UPDATE entries");
			}
			increData.executeBatch();
		}
	}
	protected Connection connection;
	protected Statement statement;
	private CounterCache count2;
	private CounterCache count3;

	private CounterCache countS;

	public Database() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		// Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:sqlite:database.db");
		// connection =
		// DriverManager.getConnection("jdbc:mysql://localhost/AI_Final?useUnicode=true&characterEncoding=Utf8",
		// "ai", "12345");
		connection.setAutoCommit(false);
		statement = connection.createStatement();
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS Data2 (count INTEGER,str TEXT PRIMARY KEY)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS Data3 (count INTEGER,str TEXT PRIMARY KEY)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS DataS (count INTEGER,str TEXT PRIMARY KEY)");
		count2 = new CounterCache("Data2", connection);
		count3 = new CounterCache("Data3", connection);
		countS = new CounterCache("DataS", connection);
	}

	public void close() throws SQLException {
		count2.flush();
		count3.flush();
		countS.flush();
		connection.commit();
		connection.close();
	}

	public void countScentence(String id) throws SQLException {
		countS.addCount(id);
	}

	public void countWord(String id) throws SQLException {
		if (id.length() == 2) {
			count2.addCount(id);
		} else if (id.length() == 3) {
			count3.addCount(id);
		}
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
