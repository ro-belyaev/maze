package ru.netcracker.belyaev.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Exit implements SimpleEntity {
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

	@Override
	public List<OnePointOnMap> getEntity() {
		return this.getExit();
	}

	@Override
	public void setEntity(List<OnePointOnMap> points) {
		for(OnePointOnMap point : points) {
			this.addExit(point);
		}
	}
}
