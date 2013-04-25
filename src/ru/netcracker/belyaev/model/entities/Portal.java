package ru.netcracker.belyaev.model.entities;

public class Portal {
	private OnePointOnMap startPoint;
	private OnePointOnMap destinationPoint;
	private int step;
	public Portal(OnePointOnMap startPoint, OnePointOnMap destinationPoint, int step) {
		this.startPoint = startPoint;
		this.destinationPoint = destinationPoint;
		this.step = step;
	}
	public OnePointOnMap getStartPoint() {
		return this.startPoint;
	}
	public OnePointOnMap getDestinationPoint() {
		return this.destinationPoint;
	}
	public int getStep() {
		return step;
	}
	public boolean equals(Portal anotherPortal) {
		if(this.startPoint.equals(anotherPortal.getStartPoint())
				&& this.destinationPoint.equals(anotherPortal.getDestinationPoint())
				&& this.step == anotherPortal.getStep()) {
			return true;
		} else {
			return false;
		}
	}
	public static Portal reversePortal(Portal somePortal) {
		OnePointOnMap startPoint = somePortal.getDestinationPoint();
		OnePointOnMap destinationPoint = somePortal.getStartPoint();
		int step = somePortal.getStep();
		return new Portal(startPoint, destinationPoint, step);
	}
}
