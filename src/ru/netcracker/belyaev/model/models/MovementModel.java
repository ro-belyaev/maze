package ru.netcracker.belyaev.model.models;

import ru.netcracker.belyaev.enums.Direction;
import ru.netcracker.belyaev.enums.GameCases;
import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.entities.Player;
import ru.netcracker.belyaev.model.entities.River;
import ru.netcracker.belyaev.model.entities.Wall;

public class MovementModel {
	private MessengerModel messenger;
	private Game game;
	
	public void setMessenger(MessengerModel messenger) {
		this.messenger = messenger;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void go(Direction direction) {
		Player player = game.getCurrentPlayer();
		OnePointOnMap currentPosition = player.getPosition();
		OnePointOnMap destination = currentPosition.nextPoint(direction);
		messenger.informAboutAction(GameCases.GO, currentPosition, destination, player);
		Wall wall = Board.getInstance().getWall();
		if(wall != null && wall.isWallBetweenTwoPointsOnOneLine(currentPosition, destination)) {
			messenger.informAboutAction(GameCases.INSIDE_WALL, currentPosition, destination, player);
		}
		else if(Board.getInstance().isOutside(destination)) {
			messenger.informAboutAction(GameCases.OUTSIDE_WALL, currentPosition, destination, player);
		}
		else {
			River river = Board.getInstance().getRiver();
			if(river != null && river.thisPointIsInTheRiver(destination)) {
				OnePointOnMap lastDestination = river.raftOnTheRiver(destination);
				messenger.informAboutAction(GameCases.RIVER, destination, lastDestination, player);
				destination = lastDestination;
			}
			player.setPosition(destination);
		}
		game.newMove();
	}
}
