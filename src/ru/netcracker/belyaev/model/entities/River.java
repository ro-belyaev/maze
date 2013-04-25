package ru.netcracker.belyaev.model.entities;

//import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.netcracker.belyaev.enums.Direction;

public class River {
	private List<Portal> trace;
	private int size;
	
	public River() {
		this.trace = new ArrayList<>();
	}
	public void addTrace(OnePointOnMap startPoint, OnePointOnMap destinationPoint, int step) {
		Portal tmpTrace = new Portal(startPoint, destinationPoint, step);
		trace.add(tmpTrace);
	}
	public List<Portal> getPortals() {
		return this.trace;
	}
	public boolean thisPointIsInTheRiver(OnePointOnMap somePoint) {
		for(Portal oneTrace : this.trace) {
			OnePointOnMap tmpStartPoint = oneTrace.getStartPoint();
			if((tmpStartPoint.getX() == somePoint.getX()) 
					&& (tmpStartPoint.getY() == somePoint.getY())) {
				return true;
			}
		}
		return false;
	}
	
	public OnePointOnMap raftOnTheRiver(OnePointOnMap startPoint) {
		OnePointOnMap currentPoint = startPoint;
		OnePointOnMap destinationPoint = null;
		int step = 0;
		for(Portal tmpTrace: this.trace) {
			if((tmpTrace.getStartPoint().getX() == currentPoint.getX()) && 
					(tmpTrace.getStartPoint().getY() == currentPoint.getY())) {
						step = tmpTrace.getStep();
			}
		}
		for(int i = step; i > 0; i--) {
			for(Portal tmpTrace: this.trace) {
				if((tmpTrace.getStartPoint().getX() == currentPoint.getX()) && 
					(tmpTrace.getStartPoint().getY() == currentPoint.getY())) {
						destinationPoint = tmpTrace.getDestinationPoint();
				}
			}
			currentPoint = destinationPoint;
		}
		return currentPoint;
	}
	
	public boolean addRiver(int step, OnePointOnMap ... points) {
		return this.addRiver(step, Arrays.asList(points));
	}
	
	public boolean addRiver(int step, List<OnePointOnMap> points) {
		Direction lastDirection = null;
		OnePointOnMap lastPoint = points.get(0);
		if(this.thisPointIsInTheRiver(lastPoint)) {
			return false;
		}
		for(int i = 1; i < points.size(); i++) {
			OnePointOnMap currentPoint = points.get(i);
			if(this.thisPointIsInTheRiver(currentPoint) ||
					!lastPoint.adjoiningPoint(currentPoint)) {
				return false;
			}
			Direction currentDirection = Direction.recognizeDirection(lastPoint, currentPoint);
			if((lastDirection != null) &&
					(lastDirection == currentDirection.reverseDirection())) {
				return false;
			}
			lastPoint = currentPoint;
			lastDirection = currentDirection;
		}
		for(int i = 1; i < points.size(); i++) {
			this.addTrace(points.get(i - 1), points.get(i), step);
			this.size++;
		}
		return true;
	}
	
	public int size() {
		return this.size;
	}
}
