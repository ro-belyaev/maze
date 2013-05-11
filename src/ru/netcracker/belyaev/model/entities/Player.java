package ru.netcracker.belyaev.model.entities;

import ru.netcracker.belyaev.controller.OnePlayer;
import ru.netcracker.belyaev.enums.PlayerState;

public class Player {
	private OnePointOnMap position;
	private int uid;
	private boolean myMove;
	private int numOfShots = 2, health = 2;
	private String name;
	private PlayerState state = PlayerState.ALIVE;
	private Treasure treasure;
	
	public Player(String name, OnePointOnMap position, int uid) {
		this.position = position;
		this.uid = uid;
		this.name = name;
	}
	
	public void injure() {
		if(--health <= 0) {
			state = PlayerState.DEAD;
			if(treasure != null) {
				dropTreasure();
			}
		}
	}
	public int getHealth() {
		return this.health;
	}
	public OnePointOnMap getPosition() {
		return this.position;
	}
	public OnePlayer convertToOnePlayer() {
		OnePlayer onePlayer = new OnePlayer();
		onePlayer.setUID(this.uid);
		onePlayer.setName(this.name);
		onePlayer.setNumOfShots(this.numOfShots);
		onePlayer.setState(this.state);
		if(this.myMove) {
			onePlayer.setMyMove();
		}
		return onePlayer;
	}
	public void setPosition(OnePointOnMap point) {
		this.position = point;
	}
	public String getName() {
		return this.name;
	}
	public PlayerState getState() {
		return this.state;
	}
	public void setState(PlayerState state) {
		this.state = state;
	}
	public int getNumOfShots() {
		return this.numOfShots;
	}
	public boolean canShoot() {
		return this.numOfShots-- > 0;
	}
	public int getUID() {
		return this.uid;
	}
	public boolean isMyMove() {
		return this.myMove;
	}
	public void takeTreasure(Treasure treasure) {
		this.treasure = treasure;
		treasure.getTreasure();
		treasure.setOwnerID(uid);
	}
	public void dropTreasure() {
		this.treasure.dropTreasure();
		this.treasure.setPosition(position);
		this.treasure = null;
	}
	public boolean hasTreasure() {
		if(this.treasure == null) {
			return false;
		}
		else {
			return true;
		}
	}
	public Treasure getTreasure() {
		return this.treasure;
	}
	public boolean isAlive() {
		return this.state == PlayerState.ALIVE;
	}
	public void ExitFromMazeWithoutTreasure() {
		this.state = PlayerState.EXIT_FROM_MAZE;
	}
	public void ExitFromMazeWithRealTreasure() {
		this.state = PlayerState.WINNER;
	}
	public boolean equals(Player anotherPlayer) {
		if(this.uid == anotherPlayer.getUID()) {
			return true;
		}
		else {
			return false;
		}
	}
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append(System.getProperty("line.separator"));
//		info.append("player " + this.getName() + " is on position (" + this.getX() + "," + this.getY() + ")");
		info.append(System.getProperty("line.separator"));
		info.append(System.getProperty("line.separator"));
		info.append("has " + this.getNumOfShots() + " shots");
		info.append(System.getProperty("line.separator"));
		return info.toString();
	}
	public void setNumOfShots(int num) {
		this.numOfShots = num;
	}
	public void setNumOfHealt(int num) {
		this.health = num;
	}
}
