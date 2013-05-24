package ru.netcracker.belyaev.servlets;

import com.google.gson.*;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.netcracker.belyaev.controller.OneMove;
import ru.netcracker.belyaev.controller.OnePlayer;
import ru.netcracker.belyaev.database.api.DatabaseManagerFactory;
import ru.netcracker.belyaev.model.models.InformerModel;
import ru.netcracker.belyaev.routing.Routing;
import ru.netcracker.belyaev.servlets.service.GameState;
import ru.netcracker.belyaev.servlets.service.MoveInGame;
import ru.netcracker.belyaev.servlets.service.PlayerInGame;

public class GetGameState extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String movesOnClient = request.getParameter("movesInTable");
		String gameId = request.getParameter("gameId");
		GameState gameState = new GameState();
		
		List<OnePlayer> players = DatabaseManagerFactory.getDatabaseManagerInstance()
				.getPlayers(gameId);
		for (OnePlayer player : players) {
			gameState.addPlayer(new PlayerInGame(player.getName(), player.getUID()));
		}
		
		List<OneMove> moves = DatabaseManagerFactory.getDatabaseManagerInstance()
				.getMoves(movesOnClient, gameId);
		Routing routing = new Routing();
		
		for (OneMove move : moves) {
			makeMove(move, routing);
			InformerModel informer = routing.getInformer();
			int currentPlayerId = Integer.parseInt(move.getCurrentPlayerId());
			MoveInGame moveInGame = new MoveInGame(informer, currentPlayerId);
			gameState.addMove(moveInGame);
//			routing.cleanInformer();
		}
	}
	
	private void makeMove(OneMove move, Routing routing) {
		String currentPlayerId = move.getCurrentPlayerId();
		String playerAction = move.getPlayerAction();
		String actionDirection = move.getActionDirection();
		switch (playerAction) {
			case "go":
				routing.go(currentPlayerId, actionDirection);
				break;
			case "shoot":
				routing.shoot(currentPlayerId, actionDirection);
				break;
			case "take_treasure":
//				routing.takeTreasure(currentPlayerId, treasureColor)
				break;
			case "drop_treasure":
				routing.dropTreasure(currentPlayerId);
				break;
			case "predict":
				routing.askPrediction(currentPlayerId);
				break;
			case "exit":
				routing.exit(currentPlayerId);
				break;
		}
	}
	
	

}
