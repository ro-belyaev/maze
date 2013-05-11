package ru.netcracker.belyaev.database.impl.oracle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OracleConnection {
	private Connection conn;
	public OracleConnection() {
		try {
			Class.forName ("oracle.jdbc.OracleDriver");
			conn = DriverManager.getConnection
				     ("jdbc:oracle:thin:@//localhost:1521/my_first_db", "system", "n2ynrsBD");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private Statement getStatement() {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stmt;
	}
	
	public ResultSet executeQuery(String query) {
		ResultSet rs = null;
		try {
			rs = this.getStatement().executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
//	public int executeUpdate(String query) {
//		int result = 0;
//		try {
//			result = this.getStatement().executeUpdate(query);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	public List<Object> executeCallableStatement(String query, int[] arrayOfSQLTypes,
			String ... InArgs) {
		CallableStatement stmt = null;
		List<Object> result = null;
		try {
			stmt = conn.prepareCall(query);
			int position = 1;
			for(String arg : InArgs) {
				if(arg.equals("null")) {
					stmt.setString(position, null);
				} else {
					stmt.setString(position, arg);
				}
				position++;
			}
			int returnPosition = position;
			for(int type : arrayOfSQLTypes) {
				stmt.registerOutParameter(position, type);
				position++;
			}
			stmt.execute();
//			Object result = new Object[arrayOfSQLTypes.length]; // ???
			result = new ArrayList<>();
			for(int i = 0, retPos = returnPosition; i < arrayOfSQLTypes.length; i++, retPos++) {
				if(arrayOfSQLTypes[i] == Types.NUMERIC) {
					result.add(new Integer(stmt.getInt(retPos)));
				} else if(arrayOfSQLTypes[i] == Types.VARCHAR) {
					result.add(stmt.getString(retPos));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public ResultSet executePreparedStatement(String query, String ... InArgs) {
		PreparedStatement stmt = null;
		ResultSet result = null;
		try {
			stmt = conn.prepareStatement(query);
			int position = 1;
			for(String arg : InArgs) {
				if(arg.equals("null")) {
					stmt.setString(position, null);
				} else {
					stmt.setString(position, arg);
				}
				position++;
			}
			result = stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
