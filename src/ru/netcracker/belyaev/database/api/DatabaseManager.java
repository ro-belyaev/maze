package ru.netcracker.belyaev.database.api;

import ru.netcracker.belyaev.model.models.Game;

public interface DatabaseManager {
	void registerGame(Game game);
	void saveGameState(Game game);
	boolean restoreGameState(Game game);
	void terminateGame(Game game);
}
