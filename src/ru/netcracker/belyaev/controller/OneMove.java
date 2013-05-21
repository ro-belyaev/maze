package ru.netcracker.belyaev.controller;

import ru.netcracker.belyaev.enums.Direction;

public class OneMove {
	private String currentPlayerId;
	private String playerAction;
	private String actionDirection;
	
	public OneMove(String id, String action, String dir) {
		this.currentPlayerId = id;
		this.playerAction = action;
		this.actionDirection = dir;
	}
	
	public String getCurrentPlayerId() {
		return this.currentPlayerId;
	}
	public String getPlayerAction() {
		return this.playerAction;
	}
	public String getActionDirection() {
		return this.actionDirection;
	}
}
