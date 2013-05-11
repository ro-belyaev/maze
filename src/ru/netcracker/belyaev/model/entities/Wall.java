package ru.netcracker.belyaev.model.entities;

import java.util.ArrayList;
import java.util.List;

import ru.netcracker.belyaev.enums.Direction;

public class Wall implements PortalEntity {
	private List<Portal> wall;
	private int size;
	
	public Wall() {
		this.wall = new ArrayList<>();
	}

	public boolean suchWallIsAlreadyExists(Portal someWall) {
		for(Portal realWall : wall) {
			if(realWall.equals(someWall) || realWall.equals(Portal.reversePortal(someWall))) {
				return true;
			}
		}
		return false;
	}
	
	public List<Portal> getPortals() {
		return wall;
	}
	
	public boolean addPortal(OnePointOnMap firstPoint, OnePointOnMap secondPoint, int fakeStep) {
		if(firstPoint.adjoiningPoint(secondPoint)) {
			Portal newWall = new Portal(firstPoint, secondPoint, 0);
			if(!suchWallIsAlreadyExists(newWall)) {
				wall.add(newWall);
				size++;
			}
			return true;
		} else {
			return false;
		}
	}

	public int size() {
		return this.size;
	}
	
	public boolean isWallBetweenTwoPointsOnOneLine(OnePointOnMap firstPoint, OnePointOnMap lastPoint) {
		if(firstPoint.equals(lastPoint)) {
			return false;
		} else if(!firstPoint.isPointOnOneLine(lastPoint)) {
			return false;
		} else {
			Direction direction = ru.netcracker.belyaev.enums.Direction.recognizeDirection(firstPoint, lastPoint);
			OnePointOnMap somePoint = firstPoint;
			do {
				OnePointOnMap nextPoint = somePoint.nextPoint(direction);
				if(isWallBetweenTwoAdjoiningPoints(somePoint, nextPoint)) {
					return true;
				}
				somePoint = nextPoint;
			} while(!somePoint.equals(lastPoint));
			return false;
		}
	}
	
	private boolean isWallBetweenTwoAdjoiningPoints(OnePointOnMap firstPoint, OnePointOnMap secondPoint) {
		Portal someWall = new Portal(firstPoint, secondPoint, 0);
		return suchWallIsAlreadyExists(someWall);
	}
}
