package ru.netcracker.belyaev.servlets.service;

import java.util.ArrayList;
import java.util.List;

import ru.netcracker.belyaev.enums.Colors;
import ru.netcracker.belyaev.enums.Direction;
import ru.netcracker.belyaev.enums.GameCases;
import ru.netcracker.belyaev.enums.PlayerActions;
import ru.netcracker.belyaev.model.models.InformerModel;
import ru.netcracker.belyaev.model.models.InformerModel.ActionInformer;
import ru.netcracker.belyaev.model.models.InformerModel.PrepareInformer;
import ru.netcracker.belyaev.model.models.InformerModel.ResultInformer;


public class MoveInGame {
	private int playerId;
	private MoveInformation moveInformation;
	
	public MoveInGame(InformerModel informer, int playerId) {
		this.playerId = playerId;
		moveInformation = new MoveInformation();
		gatherInfoAboutPrepareCases(informer, moveInformation);
		gatherInfoAboutPlayerActions(informer, moveInformation);
		gatherInfoAboutPlayerActionResults(informer, moveInformation);
	}
	
	
	public class MoveInformation {
		private List<String> someMoveInformation;
		public MoveInformation() {
			someMoveInformation = new ArrayList<>();
		}
		public List<String> getSomeMoveInformation() {
			return this.someMoveInformation;
		}
		public void addSomeMoveInformation(String info) {
			someMoveInformation.add(info);
		}
	}
	
	private void gatherInfoAboutPrepareCases(InformerModel informer, MoveInformation moveInfo) {
		String currentPlayerName = informer.getCurrentPlayerName();
		for (PrepareInformer prepareInfo : informer.getPrepareInformer()) {
			GameCases gameCase = prepareInfo.getGameCase();
			String prepareString = null;
			switch (gameCase) {
				case UNDER_ARCH:
					prepareString = currentPlayerName + " is under the arch";
					break;
				case FIND_MAGE:
					prepareString = currentPlayerName + " on one cell with mage";
					break;
				case FIND_TREASURE:
					String treasureColor = Colors.values()[prepareInfo.getTreasureColorId()].toString();
					prepareString = currentPlayerName + " is on one cell with " + treasureColor + " treasure";
					break;
				case EXIT_POINT:
					prepareString = currentPlayerName + " is on exit";
					break;
				default:
//					log about error
					break;
			}
			moveInfo.addSomeMoveInformation(prepareString);
		}
	}
	
	private void gatherInfoAboutPlayerActions(InformerModel informer, MoveInformation moveInfo) {
		String currentPlayerName = informer.getCurrentPlayerName();
		for (ActionInformer actionInfo : informer.getActionInformer()) {
			PlayerActions action = actionInfo.getPlayerAction();
			String actionString = null;
			Direction actionDirection = actionInfo.getActionDirection();
			String treasureColor = null;
			switch (action) {
				case GO:
					actionString = currentPlayerName + " goes " + actionDirection.toString();
					break;
				case SHOOT:
					actionString = currentPlayerName + " shoots " + actionDirection.toString();
					break;
				case TAKE_TREASURE:
					treasureColor = Colors.values()[actionInfo.getTreasureColorId()].toString();
					actionString = currentPlayerName + " takes " + treasureColor + " treasure";
					break;
				case DROP_TREASURE:
					treasureColor = Colors.values()[actionInfo.getTreasureColorId()].toString();
					actionString = currentPlayerName + " drops " + treasureColor + " treasure";
					break;
				case EXIT:
					actionString = currentPlayerName + " exits from treasure";
					break;
				case ASK_PREDICTION:
					actionString = currentPlayerName + " asks prediction from mage";
					break;
			}
			moveInfo.addSomeMoveInformation(actionString);
		}
	}
	
	private void gatherInfoAboutPlayerActionResults(InformerModel informer, MoveInformation moveInfo) {
		String currentPlayerName = informer.getCurrentPlayerName();
		for (ResultInformer resultInfo : informer.getResultInformer()) {
			GameCases result = resultInfo.getGameCase();
			String victim = resultInfo.getVictimName();
			String resultString = null;
			switch (result) {
				case INSIDE_WALL:
					resultString = currentPlayerName + " goes to the inside wall";
					break;
				case OUTSIDE_WALL:
					resultString = currentPlayerName + " goes to the ouside wall";
					break;
				case INJURED:
					resultString = victim + " is injured";
					break;
				case KILLED:
					resultString = victim + " is killed";
					break;
				case PREDICTION_REAL:
					resultString = victim + " has real treasure";
					break;
				case PREDICTION_FAKE:
					resultString = victim + " has fake treasure";
					break;
				case RIVER:
					int[] coordinates = resultInfo.getCoordinates();
					resultString = recogniseShiftOnTheRiver(coordinates[0], coordinates[1],
							coordinates[2], coordinates[3], currentPlayerName);
					break;
				case GAME_OVER:
					resultString = "Game is over!";
					break;
				default:
					break;
			}
			moveInfo.addSomeMoveInformation(resultString);
		}
	}
	
	private String recogniseShiftOnTheRiver(int startX, int startY, int destX, int destY, String playerName) {
		int distinctionX = destX - startX;
		int distinctionY = destY - startY;
		StringBuffer message = new StringBuffer("Player " + playerName + " rafts");
		if(distinctionX != 0) {
			message.append(" on " + Math.abs(distinctionX) + " cells " + (distinctionX > 0 ?
					"right" : "left"));
		}
		if(distinctionY != 0) {
			if(distinctionX != 0) {
				message.append(" and");
			}
			message.append(" on " + Math.abs(distinctionY) + " cells " + (distinctionY > 0 ?
					"up" : "down"));
		}
		return message.toString();
	}
}
