package ru.netcracker.belyaev.servlets;

import com.google.gson.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.netcracker.belyaev.controller.OneMove;
import ru.netcracker.belyaev.database.api.DatabaseManagerFactory;
import ru.netcracker.belyaev.enums.Direction;
import ru.netcracker.belyaev.routing.Routing;

public class GetGameState extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String movesOnClient = request.getParameter("movesInTable");
		String gameId = request.getParameter("gameId");
		List<OneMove> moves = DatabaseManagerFactory.getDatabaseManagerInstance()
				.getMoves(movesOnClient, gameId);
		Routing routing = new Routing();
		GameState gameState = new GameState();
		
		for (OneMove move : moves) {
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
//					routing.takeTreasure(currentPlayerId, treasureColor)
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

}

class GameState {
	private List<PlayerState> players;
	private int movesCount;
	private int playersCount;
	public GameState() {
		players = new ArrayList<>();
	}
	public void addPlayer(PlayerState somePlayer) {
		players.add(somePlayer);
	}
	public void setMovesCount(int count) {
		this.movesCount = count;
	}
	public void setPlayersCount(int count) {
		this.playersCount = count;
	}
}

class PlayerState {
	private String name;
	private int turn;
	private List<Move> moves;
	public PlayerState() {
		this.moves = new ArrayList<>();
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public void addMove(Move someMove) {
		moves.add(someMove);
	}
}

class Move {
	private List<String> someMoveInformation;
	public Move() {
		someMoveInformation = new ArrayList<>();
	}
	public List<String> getSomeMoveInformation() {
		return this.someMoveInformation;
	}
	public void addSomeMoveInformation(String info) {
		someMoveInformation.add(info);
	}
}
