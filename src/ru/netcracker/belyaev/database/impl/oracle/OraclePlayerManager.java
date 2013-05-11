package ru.netcracker.belyaev.database.impl.oracle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import ru.netcracker.belyaev.enums.PlayerState;
import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.entities.Player;
import ru.netcracker.belyaev.model.entities.Treasure;
import ru.netcracker.belyaev.model.models.Game;

public class OraclePlayerManager {
	public static void savePlayers(OracleConnection conn, List<Player> players, String gameId,
			String moveId) {
		String savePlayer = "begin insert into player (game_id, move_id, player_id, " + 
							"name, position_x, position_y, num_of_health, num_of_shots, state) " +
							"values(?, ?, ?, ?, ?, ?, ?, ?, ?); end;";
		for(Player player : players) {
			String uid = String.valueOf(player.getUID());
			String x = String.valueOf(player.getPosition().getX());
			String y = String.valueOf(player.getPosition().getY());
			String numOfHealth = String.valueOf(player.getHealth());
			String numOfShoots = String.valueOf(player.getNumOfShots());
			String state = player.getState().name().toLowerCase();
			conn.executePreparedStatement(savePlayer,
					gameId, moveId, uid, player.getName(),
					x, y, numOfHealth, numOfShoots, state);
		}
	}
	
	public static void restorePlayers(OracleConnection conn, Game game, String gameId, String moveId) {
		String loadPlayers = "select player_id, name, position_x, position_y, " + 
				"num_of_health, num_of_shots, state from player " +
				"where game_id=? and move_id=? order by player_id";
		ResultSet rs = conn.executePreparedStatement(loadPlayers, gameId, moveId);
		List<Treasure> treasures = game.getBoard().getTreasure();
		try {
			while(rs.next()) {
				int uid = rs.getInt(1);
				String name = rs.getString(2);
				int posX = rs.getInt(3);
				int posY = rs.getInt(4);
				int numOfHealth = rs.getInt(5);
				int numOfShots = rs.getInt(6);
				String stateString = rs.getString(7);
				PlayerState state = PlayerState.valueOf(stateString.toUpperCase());
				OnePointOnMap position = new OnePointOnMap(posX, posY);
				Player newPlayer = new Player(name, position, uid);
				newPlayer.setNumOfHealt(numOfHealth);
				newPlayer.setNumOfShots(numOfShots);
				newPlayer.setState(state);
				Treasure treasureOfCurrentPlayer = getTreasureOfCurrentPlayer(treasures, newPlayer);
				if(treasureOfCurrentPlayer != null) {
					newPlayer.takeTreasure(treasureOfCurrentPlayer);
				}
				game.getBoard().addPlayer(newPlayer);
				game.getBoard().incNumOfPlayers();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Treasure getTreasureOfCurrentPlayer(List<Treasure> treasures, Player player) {
		for(Treasure treasure : treasures) {
			if(treasure.isFound() && treasure.getOwnerID() == player.getUID()) {
				return treasure;
			}
		}
		return null;
	}
}
