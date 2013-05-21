package ru.netcracker.belyaev.model.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.netcracker.belyaev.enums.Direction;
import ru.netcracker.belyaev.enums.GameCases;
import ru.netcracker.belyaev.enums.Notification;
import ru.netcracker.belyaev.enums.PlayerActions;
import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.entities.Player;
import ru.netcracker.belyaev.model.entities.Wall;

public class ShootModel {
	private MessengerModel messenger;
	private InformerModel informer;
	private Game game;
	
	public void setMessenger(MessengerModel messenger) {
		this.messenger = messenger;
	}
	public void setInformer(InformerModel informer) {
		this.informer = informer;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
	public boolean shoot(Direction direction) {
		Player currentPlayer = game.getCurrentPlayer();
		OnePointOnMap currentPlayerPosition = currentPlayer.getPosition();
		if(currentPlayer.canShoot()) {
			informer.addActionInformer(PlayerActions.SHOOT, direction);
			messenger.informAboutAction(GameCases.SHOT, currentPlayerPosition, currentPlayerPosition.nextPoint(direction), currentPlayer);
			List<OnePointOnMap> positionOfCompetitors = getCompetitorsPosition();
			List<OnePointOnMap> possibleVictimsPoints = currentPlayerPosition
					.getPointsOnOneDirection(positionOfCompetitors, direction);
			OnePointOnMap nearestPossibleVictimsPosition = currentPlayerPosition
					.getNearestPointInDirection(possibleVictimsPoints, direction);
			Wall wall = game.getBoard().getWall();
			if(nearestPossibleVictimsPosition != null && 
					(wall == null || !wall.isWallBetweenTwoPointsOnOneLine(currentPlayerPosition, nearestPossibleVictimsPosition))) {
				List<Player> victims = getCompetitorsOnThisPoint(nearestPossibleVictimsPosition);
				int victimItem = new Random().nextInt(victims.size());
				Player victim = victims.get(victimItem);
				victim.injure();
				if(!victim.isAlive()) {
					informer.addResultInformer(GameCases.KILLED, victim.getName());
					messenger.informAboutAction(GameCases.KILLED, victim.getPosition(), victim);
				}
				else {
					informer.addResultInformer(GameCases.INJURED, victim.getName());
					messenger.informAboutAction(GameCases.INJURED, victim.getPosition(), victim);
				}
			}
			game.setLastPlayerAction(PlayerActions.SHOOT);
			game.setDirectionOfLastPlayerAction(direction);
			game.newMove();
			return true;
		}
		else {
			messenger.notifyUser(Notification.NO_BULLET);
		}
		return false;
	}
	
	public List<Player> getCompetitorsOnThisPoint(OnePointOnMap point) {
		List<Player> playersOnThisPoint = new ArrayList<>();
		Player currentPlayer = game.getCurrentPlayer();
		for(Player onePlayer : game.getBoard().getPlayers()) {
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
		for(Player somePlayer : game.getBoard().getPlayers()) {
			if(somePlayer.isAlive() && !somePlayer.equals(currentPlayer) && !positions.contains(somePlayer.getPosition())) {
				positions.add(somePlayer.getPosition());
			}
		}
		return positions;
	}
}
