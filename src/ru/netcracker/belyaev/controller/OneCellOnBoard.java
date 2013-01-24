package ru.netcracker.belyaev.controller;

import java.util.ArrayList;
import java.util.List;

import ru.netcracker.belyaev.enums.Direction;

public class OneCellOnBoard {
	private int x, y;
	private Direction riverDirection=null;
	private int riverStep=0;
	private boolean wall=false, arch=false, mage=false, exit=false;
	private int realTreasureCount=0, fakeTreasureCount=0;
	private List<OnePlayer> players = new ArrayList<>();
	
	public OneCellOnBoard(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public boolean isWall() {
		return wall;
	}
	public void setWall() {
		this.wall = true;
	}
	public boolean isArch() {
		return arch;
	}
	public void setArch() {
		this.arch = true;
	}
	public boolean isMage() {
		return mage;
	}
	public void setMage() {
		this.mage = true;
	}
	public boolean isExit() {
		return exit;
	}
	public void setExit() {
		this.exit = true;
	}
	public int getRealTreasureCount() {
		return realTreasureCount;
	}
	public void setRealTreasureCount() {
		this.realTreasureCount++;
	}
	public int getFakeTreasureCount() {
		return fakeTreasureCount;
	}
	public void setFakeTreasureCount() {
		this.fakeTreasureCount++;
	}
	public Direction getRiverDirection() {
		return riverDirection;
	}
	public void setRiverDirection(Direction direction) {
		this.riverDirection = direction;
	}
	public int getRiverStep() {
		return riverStep;
	}
	public void setRiverStep(int step) {
		this.riverStep = step;
	}
	public List<OnePlayer> getPlayers() {
		return this.players;
	}
}
