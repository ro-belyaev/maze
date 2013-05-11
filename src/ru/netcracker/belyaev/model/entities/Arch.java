package ru.netcracker.belyaev.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Arch implements SimpleEntity {
	private List<OnePointOnMap> position;
	public Arch() {
		this.position = new ArrayList<>();
	}
	
	public void addArch(OnePointOnMap point) {
		position.add(point);
	}
	public List<OnePointOnMap> getArch() {
		return this.position;
	}
	public boolean isArchOnThisPoint(OnePointOnMap point) {
		return point.isPointInList(position);
	}

	@Override
	public List<OnePointOnMap> getEntity() {
		return this.getArch();
	}

	@Override
	public void setEntity(List<OnePointOnMap> points) {
		for(OnePointOnMap point : points) {
			this.addArch(point);
		}
	}
}
