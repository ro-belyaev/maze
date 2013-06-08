package ru.netcracker.belyaev.controller;

public class OneMove {
	private String currentPlayerId;
	private String nextPlayerId;
	private String moveId;
	private String playerAction;
	private String actionDirection;
	private String treasureColor;
	
	public OneMove(String id, String nextId, String moveId, String action,
			String dir, String color) {
		this.currentPlayerId = id;
		this.nextPlayerId = nextId;
		this.moveId = moveId;
		this.playerAction = action;
		this.actionDirection = dir;
		this.treasureColor = color;
	}
	
	public String getCurrentPlayerId() {
		return this.currentPlayerId;
	}
	public String getNextPlayerId() {
		return this.nextPlayerId;
	}
	public String getCurrentMoveId() {
		return this.moveId;
	}
	public String getPlayerAction() {
		return this.playerAction;
	}
	public String getActionDirection() {
		return this.actionDirection;
	}
	public String getTreasureColor() {
		return this.treasureColor;
	}
}
