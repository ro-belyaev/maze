package ru.netcracker.belyaev.model.entities;

public class Treasure {
	private int colorID;
	private OnePointOnMap position;
	private boolean isRealTreasure;
	private boolean isFound = false;
	private int ownerID;
	public Treasure(int colorID, OnePointOnMap startPosition, boolean isRealTreasure) {
		this.colorID = colorID;
		this.position = startPosition;
		this.isRealTreasure = isRealTreasure;
	}
	public void setPosition(OnePointOnMap newPosition) {
		this.position = newPosition;
	}
	public boolean isReal() {
		return this.isRealTreasure;
	}
	public boolean isFound() {
		return this.isFound;
	}
	public OnePointOnMap getPosition() {
		return this.position;
	}
	public void getTreasure() {
		this.isFound = true;
	}
	public void dropTreasure() {
		this.isFound = false;
	}
	public int getOwnerID() {
		return this.ownerID;
	}
	public void setOwnerID(int id) {
		this.ownerID = id;
	}
	public int getColorID() {
		return this.colorID;
	}
}
