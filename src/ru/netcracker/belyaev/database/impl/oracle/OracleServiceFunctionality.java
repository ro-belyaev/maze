package ru.netcracker.belyaev.database.impl.oracle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import ru.netcracker.belyaev.model.models.Game;

public class OracleServiceFunctionality {
	public static String saveMove(OracleConnection conn, Game game, String gameId, String moveId) {
		String saveMove = "begin insert into move " + 
				"(game_id, move_id, cur_player_id, game_state, action, action_direction) " +
				"values(?, ?, ?, ?, ?, ?) returning move_id into ?; end;";
		int[] arrayOfSQLTypes = {Types.NUMERIC};
		String playerId = String.valueOf(game.getCurrentPlayerID());
		String state = null;
		if(game.isGameOver() || game.isGameTerminated()) {
			state = "end";
		} else {
			state = "process";
		}
		String action = null;
		if(game.isGameTerminated()) {
			action = "terminate";
		} else if(game.getLastPlayerAction() == null) {
			action = "start_game";
		} else {
			action = game.getLastPlayerAction().name().toLowerCase();
		}
		String direction = null;
		if(game.getDirectionOfLastPlayerAction() == null) {
			direction = "null";
		} else {
			direction = game.getDirectionOfLastPlayerAction().name().toLowerCase();
		}
		List<Object> result = conn.executeCallableStatement(saveMove, arrayOfSQLTypes,
				gameId, moveId, playerId, state, action, direction);
		return String.valueOf((Integer) result.get(0));
	}
	
	public static String getLastMoveId(OracleConnection conn, String gameId) {
		String query = "select max(move_id) from move where game_id=" + gameId;
		int moveId = -1;
		try {
			ResultSet rs = conn.executeQuery(query);
			rs.next();
			moveId = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return String.valueOf(moveId);
	}
	
	public static String getNextPlayerIdByMoveId(OracleConnection conn, String gameId, String moveId) {
		String query = "select cur_player_id from move where game_id=? and move_id=?";
		int playerId = -1;
		try {
			ResultSet rs = conn.executePreparedStatement(query, gameId, moveId);
			rs.next();
			playerId = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return String.valueOf(playerId);
	}
	
	public static String[] getMoveInformationByMoveId(OracleConnection conn, String gameId, String moveId) {
		String query = "select action, direction from move where game_id=? and move_id=?";
		String action = null;
		String direction = null;
		try {
			ResultSet rs = conn.executePreparedStatement(query, gameId, moveId);
			rs.next();
			action = rs.getString(1);
			direction = rs.getString(2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String[] result = {action, direction};
		return result;
	}
	
	public static String[] getPlayersInformation(OracleConnection conn, String gameId) {
		String query = "select name from player where game_id=?";
		List<String> playersName = new ArrayList<>();
		try {
			ResultSet rs = conn.executePreparedStatement(query, gameId);
			while (rs.next()) {
				playersName.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return playersName.toArray(new String[playersName.size()]);
	}
	
}
