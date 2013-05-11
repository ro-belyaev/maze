package ru.netcracker.belyaev.controller;

import ru.netcracker.belyaev.enums.*;
import ru.netcracker.belyaev.model.models.MainModel;
import ru.netcracker.belyaev.view.*;

public class Controller {
	private MainModel model;
	private View view;
	public Controller(MainModel model, View view) {
		this.model = model;
		this.view = view;
	}
	
	public void go(int uid, Direction direction) {
		model.go(uid, direction);
	}
	public void upTreasure(int uid, int treasureColorID) {
		model.upTreasure(uid, treasureColorID);
	}
	public void dropTreasure(int uid) {
		model.dropTreasure(uid);
	}
	public void askPrediction(int uid) {
		model.askPrediction(uid);
	}
	public void shoot(int uid, Direction direction) {
		model.shoot(uid, direction);
	}
	public void exit(int uid) {
		model.exit(uid);
	}
	public void getBoardSnapshot() {
		OneCellOnBoard[][] board = model.getBoardSnapshot();
//		drop game state here
		this.model.dropGameState();
		if(board != null) {
			view.refreshBoard(board);
		}
	}
	public void generate() {
		model.generateBoard();
	}
	public void terminate() {
		model.terminate();
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
