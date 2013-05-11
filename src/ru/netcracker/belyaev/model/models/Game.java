package ru.netcracker.belyaev.model.models;

import ru.netcracker.belyaev.database.api.DatabaseManagerFactory;
import ru.netcracker.belyaev.enums.Direction;
import ru.netcracker.belyaev.enums.GameCases;
import ru.netcracker.belyaev.enums.Notification;
import ru.netcracker.belyaev.enums.PlayerActions;
import ru.netcracker.belyaev.enums.PlayerState;
import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.entities.Player;

public class Game {
	private MessengerModel messenger;
	private MovePrepare move;
	private Board board;
	private int currentPlayerID;
	private PlayerActions lastPlayerAction;
	private Direction directionOfLastPlayerAction;
	private boolean gameIsTerminated = false;
	
	public Game() {
		board = new Board(this);
	}
	
	public void setMessenger(MessengerModel messenger) {
		this.messenger = messenger;
	}
	public MessengerModel getMessenger() {
		return this.messenger;
	}
	public void setMovePrepare(MovePrepare move) {
		this.move = move;
	}
	
	public boolean isGameTerminated() {
		return this.gameIsTerminated;
	}
	public void terminateGame() {
		this.gameIsTerminated = true;
	}
	
	public boolean isGameOver() {
		int numOfAlivePlayers = 0;
		for(Player player : this.board.getPlayers()) {
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
			this.board.dropBoard();
		}
		else {
			boolean firstMove = false;
			if(this.getCurrentPlayerID() == -1) {
				firstMove = true;
			}
			nextPlayer();
//			save game state here
			if(!firstMove) {
				DatabaseManagerFactory.getDatabaseManagerInstance().saveGameState(this);
			}
			// maybe it's better to register game here (if firstMove)

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
		this.currentPlayerID = -1;
		this.lastPlayerAction = null;
		this.directionOfLastPlayerAction = null;
		this.gameIsTerminated = false;
	}
	
	public void nextPlayer() {
		if(isGameOver()) {
			return;
		}
		boolean nextPlayerNotFound = true;
		while(nextPlayerNotFound) {
			this.currentPlayerID++;
			if(this.currentPlayerID == this.board.getNumOfPlayer()) {
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
		return this.board.getPlayers().get(currentPlayerID);
	}
	
	public void setCurrentPlayerId(int id) {
		this.currentPlayerID = id;
	}

	
	public boolean checkCurrentPlayerID(int uid) {
		if(uid < 0 || uid >= this.board.getNumOfPlayer()) { 
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
	public boolean checkIsGameCouldBeProceeded() {
		if(!this.board.isBoardCreated()) {
			messenger.notifyUser(Notification.BOARD_IS_NOT_CREATED);
			return false;
		} else if(this.gameIsTerminated) {
			messenger.notifyUser(Notification.GAME_IS_TERMINATED);
			return false;
		} else if(isGameOver()) {
			messenger.notifyUser(Notification.GAME_IS_OVER);
			return false;
		}
		return true;
	}
	public boolean checkCouldPlayerMakeMove(int uid) {
		return checkIsGameCouldBeProceeded() && checkCurrentPlayerID(uid);
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public void setLastPlayerAction(PlayerActions action) {
		this.lastPlayerAction = action;
	}
	public PlayerActions getLastPlayerAction() {
		return this.lastPlayerAction;
	}
	public void setDirectionOfLastPlayerAction(Direction dir) {
		this.directionOfLastPlayerAction = dir;
	}
	public Direction getDirectionOfLastPlayerAction() {
		return this.directionOfLastPlayerAction;
	}
	
}
