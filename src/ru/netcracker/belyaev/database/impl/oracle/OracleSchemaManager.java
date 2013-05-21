package ru.netcracker.belyaev.database.impl.oracle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class OracleSchemaManager {
	public static boolean userIsRegistered(OracleConnection conn, String name, String password) {
		String query = "select id from users where name=? and password=?";
		ResultSet rs = conn.executePreparedStatement(query, name, password);
		try {
			if(!rs.next()) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static String addUserToDatabase(OracleConnection conn, String name, String password) {
		String query = "begin insert into users (name, password) values(?,?) returning id into ?; end;";
		int[] arrayOfSQLTypes = {Types.NUMERIC};
		List<Object> statementResult = conn.executeCallableStatement(query, arrayOfSQLTypes, name, password);
		return String.valueOf((Integer) statementResult.get(0));
	}

}
