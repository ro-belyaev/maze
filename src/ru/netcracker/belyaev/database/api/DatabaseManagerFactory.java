package ru.netcracker.belyaev.database.api;

import ru.netcracker.belyaev.database.impl.oracle.OracleDatabaseManager;

public class DatabaseManagerFactory {
	public static DatabaseManager getDatabaseManagerInstance() {
		DatabaseManager result = null;
		try {
			result = (DatabaseManager) OracleDatabaseManager.class.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
