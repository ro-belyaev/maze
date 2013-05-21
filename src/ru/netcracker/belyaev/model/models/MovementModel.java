package ru.netcracker.belyaev.model.models;

import ru.netcracker.belyaev.enums.Direction;
import ru.netcracker.belyaev.enums.GameCases;
import ru.netcracker.belyaev.enums.PlayerActions;
import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.entities.Player;
import ru.netcracker.belyaev.model.entities.River;
import ru.netcracker.belyaev.model.entities.Wall;

public class MovementModel {
	private MessengerModel messenger;
	private InformerModel informer;
	private Game game;
	
	public void setMessenger(MessengerModel messenger) {
		this.messenger = messenger;
	}
	public void setInformerModel(InformerModel informer) {
		this.informer = informer;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void go(Direction direction) {
		Player player = game.getCurrentPlayer();
		OnePointOnMap currentPosition = player.getPosition();
		OnePointOnMap destination = currentPosition.nextPoint(direction);
		informer.addActionInformer(PlayerActions.GO, direction);
		messenger.informAboutAction(GameCases.GO, currentPosition, destination, player);
		game.setLastPlayerAction(PlayerActions.GO);
		game.setDirectionOfLastPlayerAction(direction);
		Wall wall = game.getBoard().getWall();
		if(wall != null && wall.isWallBetweenTwoPointsOnOneLine(currentPosition, destination)) {
			informer.addResultInformer(GameCases.INSIDE_WALL);
			messenger.informAboutAction(GameCases.INSIDE_WALL, currentPosition, destination, player);
		}
		else if(game.getBoard().isOutside(destination)) {
			informer.addResultInformer(GameCases.OUTSIDE_WALL);
			messenger.informAboutAction(GameCases.OUTSIDE_WALL, currentPosition, destination, player);
		}
		else {
			River river = game.getBoard().getRiver();
			if(river != null && river.thisPointIsInTheRiver(destination)) {
				OnePointOnMap lastDestination = river.raftOnTheRiver(destination);
				informer.addResultInformer(GameCases.RIVER, destination, lastDestination);
				messenger.informAboutAction(GameCases.RIVER, destination, lastDestination, player);
				destination = lastDestination;
			}
			player.setPosition(destination);
		}
		game.newMove();
	}
}
