package ru.netcracker.belyaev.servlets;

import com.google.gson.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
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
import ru.netcracker.belyaev.servlets.service.MoveState;
import ru.netcracker.belyaev.servlets.service.PlayerState;

public class GetGameState extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("------------------");
		PrintWriter out = response.getWriter();
		String movesOnClient = request.getParameter("movesInTable");
		System.out.println("moves on client is " + movesOnClient);
		String gameId = request.getParameter("gameId");
		GameState gameState = new GameState();
		gameState.setLastValueOfMovesInTable(movesOnClient);
		
		if (movesOnClient.equals("0")) {
			List<OnePlayer> players = DatabaseManagerFactory.getDatabaseManagerInstance()
					.getPlayers(gameId);
			for (OnePlayer player : players) {
				gameState.addPlayer(new PlayerState(player.getName(), player.getUID()));
			}
		}
		
		List<OneMove> moves = DatabaseManagerFactory.getDatabaseManagerInstance()
				.getMoves(movesOnClient, gameId);
		
		if (moves.size() > 0) {
			Routing routing = new Routing();
			routing.setGameId(gameId);
			String restoredGameStateId = decStringValue(movesOnClient);
			InformerModel informer = null;
			String currentPlayerId = null;
			String nextPlayerId = null;
			String currentMoveId = decStringValue(movesOnClient);
			
			Iterator<OneMove> itr = moves.iterator();
			while (itr.hasNext()) {
				OneMove move = itr.next();
				String playerAction = move.getPlayerAction();
				makeMove(move, routing, restoredGameStateId);
				currentPlayerId = move.getCurrentPlayerId();
				nextPlayerId = move.getNextPlayerId();
				informer = routing.getInformer();
				
				if (!playerAction.equals("start_game")) {
					MoveState moveState = new MoveState(informer, currentPlayerId, currentMoveId);
					gameState.addMove(moveState);
					routing.clearInformer();
				}
				currentMoveId = incStringValue(currentMoveId);
				routing.checkCellOfCurrentPlayerBeforeMove();
				restoredGameStateId = incStringValue(restoredGameStateId);
				if (itr.hasNext()) {
					routing.dropGameState();
				}
			}
			
			MoveState moveState = new MoveState(informer, nextPlayerId, currentMoveId);
			gameState.addMove(moveState);
			routing.dropGameState();
		}
		
		String json = new Gson().toJson(gameState);
		out.print(json);
		out.close();
	}
	
	private void makeMove(OneMove move, Routing routing, String moveId) {
		String currentPlayerId = move.getCurrentPlayerId();
		String playerAction = move.getPlayerAction();
		String actionDirection = move.getActionDirection();
		String treasureColor = move.getTreasureColor();
		System.out.println(currentPlayerId + " " + playerAction + " " + moveId);
		switch (playerAction) {
			case "start_game":
				routing.restoreBoard("0");
				break;
			case "go":
				routing.go(currentPlayerId, actionDirection, moveId);
				break;
			case "shoot":
				routing.shoot(currentPlayerId, actionDirection, moveId);
				break;
			case "take_treasure":
				routing.takeTreasure(currentPlayerId, treasureColor, moveId);
				break;
			case "drop_treasure":
				routing.dropTreasure(currentPlayerId, moveId);
				break;
			case "predict":
				routing.askPrediction(currentPlayerId, moveId);
				break;
			case "exit":
				routing.exit(currentPlayerId, moveId);
				break;
		}
	}
	
	String incStringValue(String someString) {
		return String.valueOf(Integer.parseInt(someString) + 1);
	}
	
	String decStringValue(String someString) {
		return String.valueOf(Integer.parseInt(someString) - 1);
	}

}
