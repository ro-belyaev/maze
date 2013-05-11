package ru.netcracker.belyaev.database.impl.oracle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.entities.Treasure;
import ru.netcracker.belyaev.model.models.Game;

public class OracleTreasureManager {
	public static void saveTreasures(OracleConnection conn, List<Treasure> treasures,
			String gameId, String moveId) {
		String saveTreasure = "begin insert into treasure (game_id, move_id, color, " +
			"x_position, y_position, state, owner_id, worth) " +
				"values (?, ?, ?, ?, ?, ?, ?, ?); end;";
		for(Treasure someTreasure : treasures) {
			String color = String.valueOf(someTreasure.getColorID());
			String x = String.valueOf(someTreasure.getPosition().getX());
			String y = String.valueOf(someTreasure.getPosition().getY());
			String state = someTreasure.isFound() ? "found" : "not_found";
			String ownerId = String.valueOf(someTreasure.getOwnerID());
			String worth = someTreasure.isReal() ? "real" : "fake";
			conn.executePreparedStatement(saveTreasure, gameId, moveId, color,
					x, y, state, ownerId, worth);
		}
	}
	
	public static void restoreTreasures(OracleConnection conn, Game game,
			String gameId, String moveId) {
		String restoreTreasures = "select color, x_position, y_position, state, " +
			"owner_id, worth from treasure where game_id=? and move_id=?";
		ResultSet rs = conn.executePreparedStatement(restoreTreasures, gameId, moveId);
		Treasure newTreasure = null;
		try {
			while(rs.next()) {
				int color = rs.getInt(1);
				int posX = rs.getInt(2);
				int posY = rs.getInt(3);
				OnePointOnMap position = new OnePointOnMap(posX, posY);
				String stateString = rs.getString(4);
				boolean found = stateString.equals("found") ? true : false;
				int ownerId = rs.getInt(5);
				String worthString = rs.getString(6);
				boolean worth = worthString.equals("real") ? true : false;
				newTreasure = new Treasure(color, position, worth);
				if(found) {
					newTreasure.getTreasure();
				} else {
					newTreasure.dropTreasure();
				}
				newTreasure.setOwnerID(ownerId);
				game.getBoard().addTreasure(newTreasure);
				game.getBoard().incNumOfTreasures();
			}
		} catch (SQLException e) {
				e.printStackTrace();
		}
	}
}
