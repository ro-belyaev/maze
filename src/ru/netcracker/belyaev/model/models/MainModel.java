package ru.netcracker.belyaev.model.models;

import ru.netcracker.belyaev.controller.*;
import ru.netcracker.belyaev.database.api.DatabaseManagerFactory;
import ru.netcracker.belyaev.enums.Direction;

public class MainModel {
	private Game game = new Game();
	private MessengerModel messenger = new MessengerModel();
	private InformerModel informer = new InformerModel();
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
	
	public boolean moveShouldBeContinued(int uid, String ... moveId) {
//		load state here
		if(game.getBoard().restoreBoard(moveId)) {
			if(game.checkCouldPlayerMakeMove(uid)) {
				this.informer.setCurrentPlayerName(this.game.getCurrentPlayer().getName());
				return true;
			} else {
				return false;
			}
		} else {
//			game isn't started or is terminated
			return false;
		}
	}
	
	public void checkCellOfCurrentPlayerBeforeMove() {
		move.checkCellOfCurrentPlayerBeforeMove();
	}
	
	public boolean go(int uid, Direction direction, String ... moveId) {
		if(moveShouldBeContinued(uid, moveId)) {
			movement.go(direction);
			return true;
		}
		return false;
	}
		
	public boolean upTreasure(int uid, int colorID, String ... moveId) {
		if(moveShouldBeContinued(uid, moveId)) {
			return treasure.upTreasure(colorID);
		}
		return false;
	}
	
	public boolean dropTreasure(int uid, String ... moveId) {
		if(moveShouldBeContinued(uid, moveId)) {
			return treasure.dropTreasure();
		}
		return false;
	}
	
	public boolean askPrediction(int uid, String ... moveId) {
		if(moveShouldBeContinued(uid, moveId)) {
			return mage.askPrediction();
		}
		return false;
	}
	
	public boolean exit(int uid, String ... moveId) {
		if(moveShouldBeContinued(uid, moveId)) {
			return exit.exit();
		}
		return false;
	}
	
	public boolean shoot(int uid, Direction direction, String ... moveId) {
		if(moveShouldBeContinued(uid, moveId)) {
			return weapon.shoot(direction);
		}
		return false;
	}
	
	public OneCellOnBoard[][] getBoardSnapshot() {
//		load state here
//		drop game state in controller ???
		if(game.getBoard().restoreBoard()) {
			return board.getBoardSnapshot();
		} else {
			return null;
		}
	}
	
	public String generateBoard(String gameXml) {
		board.generateBoard(gameXml);
		String gameId = DatabaseManagerFactory.getDatabaseManagerInstance().registerGame(game);
//		dropGameState();
		return gameId;
	}
	
	public void terminate() {
//		load state here
		if(game.getBoard().restoreBoard()) {
//			terminate game here
			game.terminateGame();
			DatabaseManagerFactory.getDatabaseManagerInstance().terminateGame(game);
		}
//		dropGameState();
	}
	
	public void restoreBoard(String moveId) {
		game.getBoard().restoreBoard(moveId);
	}
	
	public void setGameId(String gameId) {
		game.setGameId(gameId);
	}
	
	public InformerModel getInformer() {
		return this.informer;
	}
	
	public void clearInformer() {
		this.informer.clear();
	}
}
