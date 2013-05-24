package ru.netcracker.belyaev.model.models;

import ru.netcracker.belyaev.controller.*;
import ru.netcracker.belyaev.database.api.DatabaseManagerFactory;
import ru.netcracker.belyaev.enums.Direction;

public class MainModel {
	private Game game = new Game();
	private MessengerModel messenger = new MessengerModel();
	private InformerModel informer = new InformerModel(this.game.getCurrentPlayer().getName());
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
		this.game.setInformerModel(informer);
		this.game.setMovePrepare(move);
		this.weapon.setMessenger(messenger);
		this.weapon.setInformer(informer);
		this.weapon.setGame(game);
		this.movement.setMessenger(messenger);
		this.movement.setInformerModel(informer);
		this.movement.setGame(game);
		this.board.setMessenger(messenger);
		this.board.setGame(game);
		this.treasure.setMessenger(messenger);
		this.treasure.setInformerModel(informer);
		this.treasure.setGame(game);
		this.mage.setMessenger(messenger);
		this.mage.setInformerModel(informer);
		this.mage.setGame(game);
		this.exit.setMessenger(messenger);
		this.exit.setInformerModel(informer);
		this.exit.setGame(game);
		this.move.setMessenger(messenger);
		this.move.setInformerModel(informer);
		this.move.setGame(game);
	}
	
	
	public void dropGameState() {
		this.game.resetGame();
		this.game.getBoard().dropBoard();
	}
	
	public void saveGameState() {
		DatabaseManagerFactory.getDatabaseManagerInstance().saveGameState(this.game);
	}
	
	public boolean moveShouldBeContinued(int uid) {
//		load state here
		if(game.getBoard().restoreBoard()) {
			if(!game.checkCouldPlayerMakeMove(uid)) {
				return false;
			} else {
				return true;
			}
		} else {
//			game isn't started or is terminated
			return false;
		}
	}
	

	
	public void go(int uid, Direction direction) {
		if(moveShouldBeContinued(uid)) {
			movement.go(direction);
			saveGameState();
		}
		dropGameState();
	}
		
	public void upTreasure(int uid, int colorID) {
		if(moveShouldBeContinued(uid)) {
			if(treasure.upTreasure(colorID)) {
				saveGameState();
			}
		}
		dropGameState();
	}
	
	public void dropTreasure(int uid) {
		if(moveShouldBeContinued(uid)) {
			if(treasure.dropTreasure()) {
				saveGameState();
			}
		}
		dropGameState();
	}
	
	public void askPrediction(int uid) {
		if(moveShouldBeContinued(uid)) {
			if(mage.askPrediction()) {
				saveGameState();
			}
		}
		dropGameState();
	}
	
	public void exit(int uid) {
		if(moveShouldBeContinued(uid)) {
			if(exit.exit()) {
				saveGameState();
			}
		}
		dropGameState();
	}
	
	public void shoot(int uid, Direction direction) {
		if(moveShouldBeContinued(uid)) {
			if(weapon.shoot(direction)) {
				saveGameState();
			}
		}
		dropGameState();
	}
	
	public OneCellOnBoard[][] getBoardSnapshot() {
//		load state here
//		drop game state in controller
		if(game.getBoard().restoreBoard()) {
			return board.getBoardSnapshot();
		} else {
			return null;
		}
	}
	
	public String generateBoard(String gameXml) {
		board.generateBoard(gameXml);
		String gameId = DatabaseManagerFactory.getDatabaseManagerInstance().registerGame(game);
		dropGameState();
		return gameId;
	}
	
	public void terminate() {
//		load state here
		if(game.getBoard().restoreBoard()) {
//			terminate game here
			game.terminateGame();
			DatabaseManagerFactory.getDatabaseManagerInstance().terminateGame(game);
		}
		dropGameState();
	}
	
	public void setGameId(String gameId) {
		game.setGameId(gameId);
	}
	
	public InformerModel getInformer() {
		return this.informer;
	}
}
