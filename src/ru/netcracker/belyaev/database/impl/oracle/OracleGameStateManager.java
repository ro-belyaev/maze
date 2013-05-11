package ru.netcracker.belyaev.database.impl.oracle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import ru.netcracker.belyaev.model.models.Game;

public class OracleGameStateManager {
	public static String registerGame(OracleConnection conn, Game game) {
		String registerGame = "begin insert into game (num_of_players, num_of_treasures, " +
				"size_x, size_y) values(?, ?, ?, ?) returning id into ?; end;";
		int[] arrayOfSQLTypes = {Types.NUMERIC};
		List<Object> statementResult = conn.executeCallableStatement(registerGame, arrayOfSQLTypes,
				String.valueOf(game.getBoard().getNumOfPlayer()),
				String.valueOf(game.getBoard().getNumOfTreasures()),
				String.valueOf(game.getBoard().getSizeX()),
				String.valueOf(game.getBoard().getSizeY()));
		return String.valueOf((Integer) statementResult.get(0));
	}
	
	public static void restoreGameState(OracleConnection conn, Game game,
			String gameId, String moveId) {
		String restoreState = "select g.size_x, g.size_y, m.cur_player_id " +
			"from game g inner join move m on (g.id = m.game_id) " +
				"where m.game_id=? and m.move_id=?";
		ResultSet rs = conn.executePreparedStatement(restoreState, gameId, moveId);
		try {
			rs.next();
			int sizeX = rs.getInt(1);
			int sizeY = rs.getInt(2);
			int curPlayerId = rs.getInt(3);
			game.getBoard().setSizeX(sizeX);
			game.getBoard().setSizeY(sizeY);
			game.setCurrentPlayerId(curPlayerId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean gameWasTerminated(OracleConnection conn, Game game, String gameId,
			String moveId) {
//		if game was terminated, game state couldn't be restored
		String query = "select action from move where game_id=? and move_id=?";
		ResultSet rs = conn.executePreparedStatement(query, gameId, moveId);
		String action = null;
		try {
			rs.next();
			action = rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(action.equals("terminate")) {
			game.terminateGame();
			return true;
		}
		return false;
	}
	
	public static boolean gameWasStarted(OracleConnection conn, String gameId) {
		String query = "select count(*) from game where id=?";
		ResultSet rs = conn.executePreparedStatement(query, gameId);
		int count = 0;
		try {
			rs.next();
			count = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(count > 0) {
			return true;
		} else {
			return false;
		}
	}
}
