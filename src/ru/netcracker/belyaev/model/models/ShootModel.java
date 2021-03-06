package ru.netcracker.belyaev.model.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.netcracker.belyaev.enums.Direction;
import ru.netcracker.belyaev.enums.GameCases;
import ru.netcracker.belyaev.enums.Notification;
import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.entities.Player;
import ru.netcracker.belyaev.model.entities.Wall;

public class ShootModel {
	private MessengerModel messenger;
	private Game game;
	
	public void setMessenger(MessengerModel messenger) {
		this.messenger = messenger;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void shoot(Direction direction) {
		Player currentPlayer = game.getCurrentPlayer();
		OnePointOnMap currentPlayerPosition = currentPlayer.getPosition();
		if(currentPlayer.canShoot()) {
			messenger.informAboutAction(GameCases.SHOT, currentPlayerPosition, currentPlayerPosition.nextPoint(direction), currentPlayer);
			List<OnePointOnMap> positionOfCompetitors = getCompetitorsPosition();
			List<OnePointOnMap> possibleVictimsPoints = currentPlayerPosition
					.getPointsOnOneLine(positionOfCompetitors, direction);
			OnePointOnMap nearestPossibleVictimsPosition = currentPlayerPosition
					.getNearestPointInLine(possibleVictimsPoints, direction);
			Wall wall = Board.getInstance().getWall();
			if(nearestPossibleVictimsPosition != null && 
					(wall == null || !wall.isWallBetweenTwoPoints(currentPlayerPosition, nearestPossibleVictimsPosition))) {
				List<Player> victims = getCompetitorsOnThisPoint(nearestPossibleVictimsPosition);
				int victimItem = new Random().nextInt(victims.size());
				Player victim = victims.get(victimItem);
				victim.injure();
				if(!victim.isAlive()) {
					messenger.informAboutAction(GameCases.KILLED, victim.getPosition(), victim);
				}
				else {
					messenger.informAboutAction(GameCases.INJURED, victim.getPosition(), victim);
				}
			}
			game.newMove();
		}
		else {
			messenger.notifyUser(Notification.NO_BULLET);
		}
	}
	
	public List<Player> getCompetitorsOnThisPoint(OnePointOnMap point) {
		List<Player> playersOnThisPoint = new ArrayList<>();
		Player currentPlayer = game.getCurrentPlayer();
		for(Player onePlayer : Board.getInstance().getPlayers()) {
			if(onePlayer.getPosition().equals(point) && onePlayer.isAlive() && !onePlayer.equals(currentPlayer)) {
				playersOnThisPoint.add(onePlayer);
			}
		}
		if(playersOnThisPoint.size() == 0) {
			return null;
		}
		else {
			return playersOnThisPoint;
		}
	}
	
	public List<OnePointOnMap> getCompetitorsPosition() {
		List<OnePointOnMap> positions = new ArrayList<>();
		Player currentPlayer = game.getCurrentPlayer();
		for(Player somePlayer : Board.getInstance().getPlayers()) {
			if(somePlayer.isAlive() && !somePlayer.equals(currentPlayer) && !positions.contains(somePlayer.getPosition())) {
				positions.add(somePlayer.getPosition());
			}
		}
		return positions;
	}
}
