package ru.netcracker.belyaev.controller;

import ru.netcracker.belyaev.enums.*;
import ru.netcracker.belyaev.model.models.InformerModel;
import ru.netcracker.belyaev.model.models.MainModel;
import ru.netcracker.belyaev.view.*;

public class Controller {
	private MainModel model;
	private View view;
	public Controller(MainModel model, View view) {
		this.model = model;
		this.view = view;
	}
	
	public void checkCellOfCurrentPlayerBeforeMove() {
		model.checkCellOfCurrentPlayerBeforeMove();
	}
	public boolean go(int uid, Direction direction, String ... moveId) {
		return model.go(uid, direction, moveId);
	}
	public boolean upTreasure(int uid, int treasureColorID, String ... moveId) {
		return model.upTreasure(uid, treasureColorID, moveId);
	}
	public boolean dropTreasure(int uid, String ... moveId) {
		return model.dropTreasure(uid, moveId);
	}
	public boolean askPrediction(int uid, String ... moveId) {
		return model.askPrediction(uid, moveId);
	}
	public boolean shoot(int uid, Direction direction, String ... moveId) {
		return model.shoot(uid, direction, moveId);
	}
	public boolean exit(int uid, String ... moveId) {
		return model.exit(uid, moveId);
	}
	public void getBoardSnapshot() {
		OneCellOnBoard[][] board = model.getBoardSnapshot();
//		drop game state here
//		this.model.dropGameState();
		if(board != null) {
			view.refreshBoard(board);
		}
	}
	public String generate(String gameXml) {
		return model.generateBoard(gameXml);
	}
	public void terminate() {
		model.terminate();
	}
	public void restoreBoard(String moveId) {
		model.restoreBoard(moveId);
	}
	public InformerModel getInformer() {
		return model.getInformer();
	}
	public void clearInformer() {
		model.clearInformer();
	}
	public void setGameId(String gameId) {
		model.setGameId(gameId);
	}
	public void saveGameState() {
		model.saveGameState();
	}
	public void dropGameState() {
		model.dropGameState();
	}
	
	public void informAboutAction(GameCases action, int startX, int startY, int destX, int destY, OnePlayer player) {
		view.informAboutAction(action, startX, startY, destX, destY, player);
	}	
	public void informAboutAction(GameCases action, int startX, int startY, OnePlayer player, int... colourID) {
		view.informAboutAction(action, startX, startY, player, colourID);
	}
	public void informAboutAction(GameCases action, int x, int y, OnePlayer player) {
		view.informAboutAction(action, x, y, player);
	}
	public void informAboutAction(GameCases action) {
		view.informAboutAction(action);
	}
	public void notifyUser(Notification notification) {
		view.notifyUser(notification);
	}

}
