package ru.netcracker.belyaev.database.impl.oracle;

import java.util.ArrayList;
import java.util.List;


import ru.netcracker.belyaev.controller.OneMove;
import ru.netcracker.belyaev.database.api.DatabaseManager;
import ru.netcracker.belyaev.enums.Notification;
import ru.netcracker.belyaev.model.entities.Player;
import ru.netcracker.belyaev.model.entities.PortalEntity;
import ru.netcracker.belyaev.model.entities.SimpleEntity;
import ru.netcracker.belyaev.model.entities.Treasure;
import ru.netcracker.belyaev.model.models.Game;

public class OracleDatabaseManager implements DatabaseManager {
	private String gameId;
	private String moveId;

	@Override
	public void saveGameState(Game game) {
		this.gameId = game.getGameId();
		OracleConnection conn = new OracleConnection();
		List<Player> players = game.getBoard().getPlayers();
		List<Treasure> treasures = game.getBoard().getTreasure();
		
		this.moveId = OracleServiceFunctionality.getLastMoveId(conn, this.gameId);
		String newMoveId = String.valueOf(Integer.parseInt(this.moveId) + 1);
		this.moveId = OracleServiceFunctionality.saveMove(conn, game, this.gameId, newMoveId);
		OraclePlayerManager.savePlayers(conn, players, this.gameId, this.moveId);
		OracleTreasureManager.saveTreasures(conn, treasures, this.gameId, this.moveId);
	}

	@Override
	public boolean restoreGameState(Game game) {
		OracleConnection conn = new OracleConnection();
		this.gameId = game.getGameId();
		if(OracleGameStateManager.gameWasStarted(conn, this.gameId)) {
			this.moveId = OracleServiceFunctionality.getLastMoveId(conn, this.gameId);
			if(!OracleGameStateManager.gameWasTerminated(conn, game, this.gameId, this.moveId)) {
				OracleGameStateManager.restoreGameState(conn, game, this.gameId, this.moveId);
				OracleTreasureManager.restoreTreasures(conn, game, this.gameId, this.moveId);
				OraclePlayerManager.restorePlayers(conn, game, this.gameId, this.moveId);
				OracleSimpleEntityManager.restoreSimpleEntities(conn, game, this.gameId);
				OraclePortalEntityManager.restorePortalEntities(conn, game, this.gameId);
				return true;
			} else {
				game.getMessenger().notifyUser(Notification.GAME_IS_ALREADY_TERMINATED);
			}
		} else {
			game.getMessenger().notifyUser(Notification.BOARD_IS_NOT_CREATED);
		}
		return false;
	}

	@Override
	public String registerGame(Game game) {
		OracleConnection conn = new OracleConnection();
		
		this.gameId = OracleGameStateManager.registerGame(conn, game);
		game.setGameId(this.gameId);
		this.moveId = OracleServiceFunctionality.saveMove(conn, game, gameId, "0");
		
		List<Player> players = game.getBoard().getPlayers();
		List<Treasure> treasures = game.getBoard().getTreasure();
		List<SimpleEntity> simpleEntities = new ArrayList<>();
		simpleEntities.add(game.getBoard().getArch());
		simpleEntities.add(game.getBoard().getExit());
		simpleEntities.add(game.getBoard().getMage());
		List<PortalEntity> portalEntities = new ArrayList<>();
		portalEntities.add(game.getBoard().getRiver());
		portalEntities.add(game.getBoard().getWall());
		
		OraclePlayerManager.savePlayers(conn, players, this.gameId, this.moveId);
		OracleTreasureManager.saveTreasures(conn, treasures, gameId, this.moveId);
		OracleSimpleEntityManager.registerSimpleEntities(conn, simpleEntities, gameId);
		OraclePortalEntityManager.registerPortalEntities(conn, portalEntities, gameId);
		
		return this.gameId;
	}
	
	
	@Override
	public void terminateGame(Game game) {
		OracleConnection conn = new OracleConnection();
		this.gameId = game.getGameId();
		if(OracleGameStateManager.gameWasStarted(conn, this.gameId)) {
			this.moveId = OracleServiceFunctionality.getLastMoveId(conn, this.gameId);
			if(!OracleGameStateManager.gameWasTerminated(conn, game, this.gameId, this.moveId)) {
				String newMoveId = String.valueOf(Integer.parseInt(this.moveId) + 1);
				this.moveId = OracleServiceFunctionality.saveMove(conn, game, this.gameId, newMoveId);
				game.getMessenger().notifyUser(Notification.GAME_IS_TERMINATED);
			} else {
				game.getMessenger().notifyUser(Notification.GAME_IS_ALREADY_TERMINATED);
			}
		} else {
			game.getMessenger().notifyUser(Notification.BOARD_IS_NOT_CREATED);
		}
	}

	@Override
	public int registerUser(String name, String password) {
		OracleConnection conn = new OracleConnection();
		if(!OracleSchemaManager.userIsRegistered(conn, name, password)) {
			String id = OracleSchemaManager.addUserToDatabase(conn, name, password);
			System.out.println(id);
		} else {
			System.out.println("already registered");
		}
		return 0;
	}

	@Override
	public List<OneMove> getMoves(String startMoveId, String gameId) {
		this.gameId = gameId;
		int firstMoveId = Integer.parseInt(startMoveId);
		OracleConnection conn = new OracleConnection();
		int lastMoveId = Integer.parseInt(OracleServiceFunctionality.getLastMoveId(conn, this.gameId));
		List<OneMove> moves = new ArrayList<>();
		for (int i = firstMoveId; i < lastMoveId; i++) {
			String curPlayerId = OracleServiceFunctionality.getNextPlayerIdByMoveId(conn,
					this.gameId, String.valueOf(i));
			String[] info = OracleServiceFunctionality.getMoveInformationByMoveId(conn, this.gameId, String.valueOf(i+1));
			String action = info[0];
			String dir = info[1];
			OneMove oneMove = new OneMove(curPlayerId, action, dir);
			moves.add(oneMove);
		}
		return moves;
	}
	
//	public String getGameId() {
//		return this.gameId;
//	}
//	public String getMoveId() {
//		return this.moveId;
//	}


}
