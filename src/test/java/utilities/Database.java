package utilities;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public abstract class Database {
	protected abstract String dbUser();

	protected abstract String dbPass();

	protected abstract String dbHost();

	protected abstract String dbName();

	protected abstract String dbPort();

	protected abstract String connUrl();

	protected abstract String driverName();

	/**
	 * 
	 * @return
	 */
	public Connection connectOverSSH() {
		// TODO
		String sshUser = "ec2-user";
		String sshHost = "54.68.21.0";
		int sshPort = 22;
		int localPort = 1234;
		int dbPort = 3306;
		String dbUser = "posiba";
		String dbPass = "@melinh#02";
		String dbHost = "127.0.0.1";
		String dbName = "posiba_core";
		String sshKey = "posiba-dev.pem";
		Connection conn = null;
		try {
			// Remote to SSH host
			JSch jsch = new JSch();
			Session session = jsch.getSession(sshUser, sshHost, sshPort);
			jsch.addIdentity(sshKey);
			JSch.setConfig("StrictHostKeyChecking", "no");
			System.out.println("[SSH] Establishing Connection...");
			session.connect();
			// session.delPortForwardingL(localPort);
			// Forward port
			int assingedPort = session.setPortForwardingL(localPort, dbHost, dbPort);
			System.out.println("localhost:" + assingedPort + " -> " + dbHost + ":" + dbPort);
			System.out.println("[SSH] Port Forwarded");

			// Connect MySQL database
			try {
				Class.forName(driverName());
				conn = DriverManager.getConnection(connUrl() + dbName, dbUser, dbPass);
				System.out.println("[PostgreSQL] Database connection established.");
				conn.setAutoCommit(false);
			} catch (Exception ex) {
				System.out.println("[PostgreSQL] Database connection failed.");
				ex.printStackTrace();
			}
			session.delPortForwardingL(localPort);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public Connection connectDB() {
		Connection conn = null;

		// Connect MySQL database
		try {
			Class.forName(driverName());
			conn = DriverManager.getConnection(connUrl() + dbName(), dbUser(), dbPass());
			System.out.println("[PostgreSQL] Database connection established.");
			conn.setAutoCommit(false);
		} catch (Exception ex) {
			System.out.println("[PostgreSQL] Database connection failed.");
			ex.printStackTrace();
		}
		return conn;
	}

	public Connection connectDB(String dbUser, String dbPass, String dbHost, String dbName) {
		Connection conn = null;

		// Connect MySQL database
		try {
			Class.forName(driverName());
			conn = DriverManager.getConnection(connUrl() + dbName, dbUser, dbPass);
			System.out.println("[PostgreSQL] Database connection established.");
			conn.setAutoCommit(false);
		} catch (Exception ex) {
			System.out.println("[PostgreSQL] Database connection failed.");
			ex.printStackTrace();
		}
		return conn;
	}

	/**
	 * Execute query
	 * 
	 * @param query
	 * @return result set
	 */
	public ResultSet executeQuery(String query) {
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = connectDB();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			conn.commit();
			conn.close();// Close connection: Hanh added on July 11 2016
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return rs;
	}

	/**
	 * Execute query with certain connection and query
	 * 
	 * @param conn
	 * @param query
	 * @return
	 */
	public static ResultSet executeQuery(Connection conn, String query) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			conn.commit();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return rs;
	}

	/**
	 * Execute update query
	 * 
	 * @param query
	 */
	public void executeUpdate(String query) {
		Statement stmt = null;
		Connection conn = connectDB();
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			conn.commit();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Execute query without returning result
	 * 
	 * @param query
	 */
	public void execute(String query) {
		Statement stmt = null;
		Connection conn = connectDB();
		try {
			stmt = conn.createStatement();
			stmt.execute(query);
			conn.commit();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Execute query with certain connection and without returning result
	 * 
	 * @param conn
	 * @param query
	 */
	public static void execute(Connection conn, String query) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.execute(query);
			conn.commit();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Get string data in an expected column from query. Author: Khoi Date: Jan 17, 2015
	 * 
	 * @param query
	 * @param column
	 * @return
	 */
	public String getData(String query, String column) {
		String value = "";
		ResultSet rs = executeQuery(query);
		try {
			while (rs.next()) {
				value = rs.getString(column);
			}
		} catch (SQLException e) {
			System.out.println("Cannot get data from DB!");
		}
		return value;
	}

	/**
	 * Get string data from expected column from query of DB. Author: Khoi Date: Jan 17, 2015
	 * 
	 * @param conn
	 * @param query
	 * @param column
	 * @return
	 */
	public static String getData(Connection conn, String query, String column) {
		String value = "";
		ResultSet rs = executeQuery(conn, query);
		try {
			while (rs.next()) {
				value = rs.getString(column);
			}
		} catch (SQLException e) {
			System.out.println("Cannot get data from DB!");
		}
		return value;
	}

	/**
	 * Get an List of data from expected column in a query of DB
	 * @param conn
	 * @param query
	 * @param column
	 * @return
	 */
	public static List<String> getDataList(Connection conn, String query, String column) {
		List<String> values = new ArrayList<String>();

		ResultSet rs = executeQuery(conn, query);
		try {
			while (rs.next()) {
				values.add(rs.getString(column));
			}
		} catch (SQLException e) {
			System.out.println("Cannot get data from DB!");
		}
		return values;
	}
	
	/**
	 * Get list string of expected column from query
	 * @param query
	 * @param column
	 * @return
	 */
	public List<String> getDataList(String query, String column) {
		List<String> values = new ArrayList<String>();

		ResultSet rs = executeQuery(query);
		try {
			while (rs.next()) {
				values.add(rs.getString(column));
			}
		} catch (SQLException e) {
			System.out.println("Cannot get data from DB!");
		}
		return values;
	}

	/**
	 * 
	 * @param query
	 * @param columnName
	 * @param parameters
	 * @return
	 */
	public List<String> getDataList(String query, String columnName, String[] parameters) {

		Connection conn = connectDB();
		PreparedStatement stmt = null;
		ResultSet rs;
		List<String> values = new ArrayList<String>();
		try {
			stmt = conn.prepareStatement(query);
			int j = 1; // Parameter index starts at 1
			for (int i = 0; i <= parameters.length - 1; i++) {
				stmt.setString(j++, parameters[i]);
			}
			rs = stmt.executeQuery();
			conn.commit();
			while (rs.next()) {
				values.add(rs.getString(columnName));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return values;
	}

	/**
	 * 
	 * Author: Khoi Date: Jan 17, 2015
	 * 
	 * @param query
	 * @param columns
	 * @return
	 */
	public Hashtable<String, String> getDataHash(String query, String[] columns) {
		Hashtable<String, String> value = new Hashtable<String, String>();
		ResultSet rs = executeQuery(query);
		try {
			while (rs.next()) {
				for (String column : columns) {
					value.put(column, rs.getString(column));
				}
			}
		} catch (SQLException e) {
			System.out.println("Cannot get data from DB!");
		}
		return value;
	}

	/*
	 * return array of string[]
	 */
	public ArrayList<String[]> getData(String query, String[] columns) {
		ResultSet rs = executeQuery(query);
		ArrayList<String[]> data = new ArrayList<String[]>();
		String[] dataRow;
		int col = 0;
		try {
			while (rs.next()) {
				dataRow = new String[columns.length];
				for (col = 0; col < columns.length; col++) {
					String strValue = rs.getString(columns[col]);
					dataRow[col] = strValue;
				}
				data.add(dataRow);
			}
		} catch (SQLException e) {
			System.out.println("Cannot get data from DB!");
		}
		return data;
	}

	/**
	 * Author: Khoi Date: Dec 09, 2015
	 * 
	 * @param conn
	 * @param query
	 * @param columns
	 * @return
	 */
	public static Hashtable<String, String> getData(Connection conn, String query, String[] columns) {
		Hashtable<String, String> value = new Hashtable<String, String>();
		ResultSet rs = executeQuery(conn, query);
		try {
			while (rs.next()) {
				for (String column : columns) {
					value.put(column, rs.getString(column));
				}
			}
		} catch (SQLException e) {
			System.out.println("Cannot get data from DB!");
		}
		return value;
	}
	
	public void executeUpdatedSP(String storeProcedure, String value) {
		CallableStatement stmt = null;
		Connection conn = connectDB();
		try {
			String query = "call " + storeProcedure + "(?)";
			stmt = conn.prepareCall(query);
			stmt.setString(1, value);
			stmt.executeUpdate();
			conn.commit();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void executeUpdatedSP(String storeProcedure, String value1, String value2) {
		CallableStatement stmt = null;
		Connection conn = connectDB();
		try {
			String query = "call " + storeProcedure + "(?,?)";
			stmt = conn.prepareCall(query);
			stmt.setString(1, value1);
			stmt.setString(2, value2);
			stmt.executeUpdate();
			conn.commit();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * 
	 * @param conn
	 */
	public static void close(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Get data from database and return a list of HashTable
	 * 
	 * @param query
	 * @param columns
	 * @return
	 */
	public ArrayList<Map<String, String>> getDataMaps(String query, String[] columns) {
		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> value = new LinkedHashMap<String, String>();
		ResultSet rs = executeQuery(query);
		try {
			while (rs.next()) {
				value = new Hashtable<String, String>();
				for (String column : columns) {
					value.put(column, rs.getString(column));
				}
				result.add(value);
			}
		} catch (SQLException e) {
			System.out.println("Cannot get data from DB!");
		}
		return result;
	}
	
	/**
	 * Get data from database and return a list of HashTable
	 * 
	 * @param query
	 * @param columns
	 * @return
	 */
	public ArrayList<Hashtable<String, String>> getDataHashes(String query, String[] columns) {
		ArrayList<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
		Hashtable<String, String> value = new Hashtable<String, String>();
		ResultSet rs = executeQuery(query);
		try {
			while (rs.next()) {
				value = new Hashtable<String, String>();
				for (String column : columns) {
					value.put(column, rs.getString(column));
				}
				result.add(value);
			}
		} catch (SQLException e) {
			System.out.println("Cannot get data from DB!");
		}
		return result;
	}
}
