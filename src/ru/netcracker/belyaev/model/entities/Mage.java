package ru.netcracker.belyaev.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Mage {
	private List<OnePointOnMap> position;
	public Mage() {
		this.position = new ArrayList<>();
	}
	
	public void addMage(OnePointOnMap point) {
		this.position.add(point);
	}
	public List<OnePointOnMap> getMage() {
		return this.position;
	}
	public boolean isMageOnThisPoint(OnePointOnMap point) {
		return point.isPointInList(position);
	}
	public boolean makePrediction(Treasure treasure) {
		return treasure.isReal();
	}
}
