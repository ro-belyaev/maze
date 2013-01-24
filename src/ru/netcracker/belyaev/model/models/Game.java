package ru.netcracker.belyaev.model.models;

import ru.netcracker.belyaev.enums.GameCases;
import ru.netcracker.belyaev.enums.Notification;
import ru.netcracker.belyaev.enums.PlayerState;
import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.entities.Player;

public class Game {
	private MessengerModel messenger;
	private MovePrepare move;
	private int currentPlayerID;
	private int playerNumber;
	
	public void setMessenger(MessengerModel messenger) {
		this.messenger = messenger;
	}
	public void setMovePrepare(MovePrepare move) {
		this.move = move;
	}
	
	public boolean isGameOver() {
		int numOfAlivePlayers = 0;
		for(Player player : Board.getInstance().getPlayers()) {
			if(player.getState() == PlayerState.ALIVE) {
				numOfAlivePlayers++;
			}
		}
		if(numOfAlivePlayers > 1) {
			return false;
		}
		return true;
	}
	
	public void newMove() {
		if(isGameOver()) {
			messenger.informAboutAction(GameCases.GAME_OVER);
			Board.getInstance().dropBoard();
		}
		else {
			nextPlayer();
			Player player = getCurrentPlayer();
			OnePointOnMap playerPosition = player.getPosition();
			messenger.informAboutAction(GameCases.CURRENT_PLAYER_INFORMATION, playerPosition, player);
			move.checkTreasureOnThisPoint(playerPosition);
			move.checkMageOnThisPoint(playerPosition);
			move.checkArchOnThisPoint(playerPosition);
			move.checkExitOnThisPoint(playerPosition);
		}
	}
	
	public void resetGame() {
		this.playerNumber = Board.getInstance().getNumOfPlayer();
		this.currentPlayerID = -1;
	}
	
	public void nextPlayer() {
		if(isGameOver()) {
			return;
		}
		boolean nextPlayerNotFound = true;
		while(nextPlayerNotFound) {
			this.currentPlayerID++;
			if(this.currentPlayerID == this.playerNumber) {
				this.currentPlayerID = 0;
			}
			if(getCurrentPlayer().getState() == PlayerState.ALIVE) {
				nextPlayerNotFound = false;
			}
		}
	}
	
	public int getCurrentPlayerID() {
		return this.currentPlayerID;
	}
	
	public Player getCurrentPlayer() {
		return Board.getInstance().getPlayers().get(currentPlayerID);
	}

	
	public boolean checkCurrentPlayerID(int uid) {
		if(uid < 0 || uid >= Board.getInstance().getNumOfPlayer()) {
			return false;
		}
		else {
			if(uid != getCurrentPlayerID()) {
				messenger.notifyUser(Notification.NOT_YOUR_MOVE);
				return false;
			}
			return true;
		}
	}
	public boolean checkIsGameStarted() {
		boolean isStarted = Board.getInstance().isBoardCreated();
		if(!isStarted) {
			messenger.notifyUser(Notification.BOARD_IS_NOT_CREATED);
		}
		return isStarted;
	}
	public boolean checkCouldPlayerMakeMove(int uid) {
		return checkIsGameStarted() && checkCurrentPlayerID(uid);
	}
	
}
