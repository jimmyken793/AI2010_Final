package idv.jimmyken793.aiproj.database;

import java.sql.Connection;
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
 * Class for caching counter.
 * 
 * @author Jimmy
 * 
 */
public class DataSet {
	protected Connection connection;
	String tableName;
	private Statement statement;
	private PreparedStatement insertData;
	private PreparedStatement increData;
	private HashSet<String> existed_word = new HashSet<String>();
	private HashSet<String> insertion = new HashSet<String>();

	private HashMap<String, Integer> increment = new HashMap<String, Integer>();

	public DataSet(String table, Connection connection) throws SQLException {
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

	public void addCount(String id, int count) {
		if (!existed_word.contains(id)) {
			insertion.add(id);
			increment.put(id, count);
			existed_word.add(id);
		} else {
			increment.put(id, increment.get(id) + count);
		}
	}

	public void flush() throws SQLException {
		{
			System.out.println("Processing " + insertion.size() + " INSERT entries");
			Iterator<String> it = insertion.iterator();
			int c = 0;
			while (it.hasNext()) {
				String i = it.next();
				insertData.setInt(1, increment.get(i));
				insertData.setString(2, i);
				insertData.addBatch();
				increment.put(i, 0);
				c++;
			}
			insertion.clear();

			System.out.println("Processed " + c + " INSERT entries");
		}
		insertData.executeBatch();
		{
			Set<Entry<String, Integer>> ent = increment.entrySet();
			System.out.println("Processing " + ent.size() + " UPDATE entries");
			Iterator<Entry<String, Integer>> it = ent.iterator();
			int c = 0;
			while (it.hasNext()) {
				Entry<String, Integer> i = it.next();
				if (i.getValue() != 0) {
					increData.setInt(1, i.getValue());
					increData.setString(2, i.getKey());
					increData.addBatch();
					increment.put(i.getKey(), 0);
					c++;
				}
			}

			System.out.println("Processed " + c + " UPDATE entries");
		}
		increData.executeBatch();
	}
}
