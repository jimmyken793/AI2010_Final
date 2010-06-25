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

	protected Connection connection;
	protected Statement statement;
	private CounterCache count2;
	private CounterCache count3;
	private CounterCache countS;

	private class CounterCache {
		String tableName;
		private Statement statement;
		private PreparedStatement insertData;
		private PreparedStatement increData;
		private HashSet<String> insertion = new HashSet<String>();

		private HashMap<String, Integer> keySymbol = new HashMap<String, Integer>();
		private HashMap<Integer, String> idKey = new HashMap<Integer, String>();
		private HashMap<Integer, Integer> increment = new HashMap<Integer, Integer>();
		private HashMap<Integer, Integer> base = new HashMap<Integer, Integer>();

		public int getData(String key) {
			if (!keySymbol.containsKey(key)) {
				return 0;
			}
			int id = keySymbol.get(key);
			return base.get(id) + increment.get(id);
		}

		public CounterCache(String table, Connection connection) throws SQLException {
			statement = connection.createStatement();
			tableName = table;
			insertData = connection.prepareStatement("INSERT OR REPLACE INTO " + tableName + "(count,str) VALUES (?,?);");
			increData = connection.prepareStatement("UPDATE " + tableName + " SET count = count+? WHERE str = ?;");
			ResultSet rs = statement.executeQuery("select * from " + tableName + ";");
			while (rs.next()) {
				int id = keySymbol.size();
				keySymbol.put(rs.getString(2), id);
				idKey.put(id, rs.getString(2));
				increment.put(id, 0);
				base.put(id, rs.getInt(1));
			}
		}

		public void addCount(String id) {
			addCount(id, 1);
		}

		public void addCount(String id, int count) {
			if (!keySymbol.containsKey(id)) {

				int kid = keySymbol.size();
				keySymbol.put(id, kid);
				idKey.put(kid, id);
				increment.put(kid, count);
				base.put(kid, 0);
				insertion.add(id);
			} else {
				increment.put(keySymbol.get(id), increment.get(keySymbol.get(id)) + count);
			}
		}

		public void flush() throws SQLException {
			{
				System.out.println("Processing " + insertion.size() + " INSERT entries");
				Iterator<String> it = insertion.iterator();
				int c = 0;
				if (it.hasNext()) {
					for (String i = it.next(); it.hasNext(); i = it.next()) {
						int key = keySymbol.get(i);
						insertData.setInt(1, increment.get(key));
						insertData.setString(2, i);
						insertData.addBatch();
						increment.put(key, 0);
						c++;
					}
					insertion.clear();
				}
				System.out.println("Processed " + c + " INSERT entries");
			}
			insertData.executeBatch();
			{
				Set<Entry<Integer, Integer>> ent = increment.entrySet();
				System.out.println("Processing " + ent.size() + " UPDATE entries");
				Iterator<Entry<Integer, Integer>> it = ent.iterator();
				int c = 0;
				if (it.hasNext()) {
					for (Entry<Integer, Integer> i = it.next(); it.hasNext(); i = it.next()) {
						if (i.getValue() != 0) {
							increData.setInt(1, i.getValue());
							increData.setString(2, idKey.get(i.getKey()));
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

	public int getData2(String key) {
		return count2.getData(key);
	}

	public int getData3(String key) {
		return count3.getData(key);
	}

	public int getDataS(String key) {
		return countS.getData(key);
	}

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
