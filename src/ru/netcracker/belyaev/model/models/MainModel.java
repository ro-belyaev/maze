package ru.netcracker.belyaev.model.models;

import ru.netcracker.belyaev.controller.*;
import ru.netcracker.belyaev.enums.Direction;

public class MainModel {
	private Game game = new Game();
	private MessengerModel messenger = new MessengerModel();
	private ShootModel weapon = new ShootModel();
	private MovementModel movement = new MovementModel();
	private BoardModel board = new BoardModel();
	private TreasureModel treasure = new TreasureModel();
	private MageModel mage = new MageModel();
	private ExitModel exit = new ExitModel();
	private MovePrepare move = new MovePrepare();
	
	public void setController(Controller controller) {
		this.messenger.setController(controller);
	}
	{
		this.game.setMessenger(messenger);
		this.game.setMovePrepare(move);
		this.weapon.setMessenger(messenger);
		this.weapon.setGame(game);
		this.movement.setMessenger(messenger);
		this.movement.setGame(game);
		this.board.setMessenger(messenger);
		this.board.setGame(game);
		this.treasure.setMessenger(messenger);
		this.treasure.setGame(game);
		this.mage.setMessenger(messenger);
		this.mage.setGame(game);
		this.exit.setMessenger(messenger);
		this.exit.setGame(game);
		this.move.setMessenger(messenger);
		this.move.setGame(game);
	}
	

	
	public void go(int uid, Direction direction) {
		if(!game.checkCouldPlayerMakeMove(uid)) {
			return;
		}
		else {
			movement.go(direction);
		}
	}
		
	public void upTreasure(int uid, int colorID) {
		if(!game.checkCouldPlayerMakeMove(uid)) {
			return;
		}
		else {
			treasure.upTreasure(colorID);
		}
	}
	
	public void dropTreasure(int uid) {
		if(!game.checkCouldPlayerMakeMove(uid)) {
			return;
		}
		else {
			treasure.dropTreasure();
		}
	}
	
	public void askPrediction(int uid) {
		if(!game.checkCouldPlayerMakeMove(uid)) {
			return;
		}
		else {
			mage.askPrediction();
		}
	}
	
	public void exit(int uid) {
		if(!game.checkCouldPlayerMakeMove(uid)) {
			return;
		}
		else {
			exit.exit();
		}
	}
	
	public void shoot(int uid, Direction direction) {
		if(!game.checkCouldPlayerMakeMove(uid)) {
			return;
		}
		else {
			weapon.shoot(direction);
		}
	}
	
	public OneCellOnBoard[][] getBoardSnapshot() {
		return board.getBoardSnapshot();
	}
	
	public void generateBoard() {
		board.generateBoard();
	}
	
	public void dropBoard() {
		board.dropBoard();
	}
}
