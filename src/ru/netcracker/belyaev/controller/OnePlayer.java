package ru.netcracker.belyaev.controller;

import ru.netcracker.belyaev.enums.PlayerState;

public class OnePlayer {
	private int uid;
	private boolean myMove=false;
	private int numOfShots;
	private PlayerState state;
	private String name;
	

	public int getUID() {
		return this.uid;
	}
	public void setUID(int uid) {
		this.uid = uid;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumOfShots() {
		return this.numOfShots;
	}
	public void setNumOfShots(int count) {
		this.numOfShots = count;
	}
	public boolean isMyMove() {
		return this.myMove;
	}
	public void setMyMove() {
		this.myMove = true;
	}
	public void setState(PlayerState state) {
		this.state = state;
	}
	public PlayerState getState() {
		return this.state;
	}
	public boolean isAlive() {
		return this.state == PlayerState.ALIVE;
	}
}
