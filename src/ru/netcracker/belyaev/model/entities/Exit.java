package ru.netcracker.belyaev.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Exit {
	private List<OnePointOnMap> position;
	private int numOfExit;
	public Exit() {
		position = new ArrayList<>();
	}
	
	public void addExit(OnePointOnMap point) {
		if(!isExitOnThisPoint(point)) {
			this.position.add(point);
			this.numOfExit++;
		}
	}
	public List<OnePointOnMap> getExit() {
		return this.position;
	}
	public boolean isExitOnThisPoint(OnePointOnMap point) {
		return point.isPointInList(position);
	}
	public int size() {
		return this.numOfExit;
	}
}
