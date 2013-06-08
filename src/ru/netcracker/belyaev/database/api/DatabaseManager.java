package ru.netcracker.belyaev.database.api;

import java.util.List;

import ru.netcracker.belyaev.controller.OneMove;
import ru.netcracker.belyaev.controller.OnePlayer;
import ru.netcracker.belyaev.model.models.Game;

public interface DatabaseManager {
	String registerGame(Game game);
	void saveGameState(Game game);
//	boolean restoreGameState(Game game);
	boolean restoreLastGameState(Game game);
	boolean restoreSpecificGameState(Game game, String moveId);
	void terminateGame(Game game);
	int registerUser(String name, String password);
	List<OneMove> getMoves(String startMoveId, String gameId);
	List<OnePlayer> getPlayers(String gameId);
}
