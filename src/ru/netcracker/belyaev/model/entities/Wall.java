package ru.netcracker.belyaev.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Wall {
	private List<OnePointOnMap> wall;
	private int size;
	
	public Wall() {
		this.wall = new ArrayList<>();
	}
	public boolean checkIfPointIsPartOfWall(OnePointOnMap somePoint) {
		for(OnePointOnMap point : wall) {
			if((point.getX() == somePoint.getX()) 
					&& (point.getY() == somePoint.getY())) {
				return true;
			}
		}
		return false;
	}
	public List<OnePointOnMap> getWall() {
		return wall;
	}
	public void addPoint(OnePointOnMap somePoint) {
		if(!checkIfPointIsPartOfWall(somePoint)) {
			wall.add(somePoint);
			size++;
		}
	}
	public void removePoint(OnePointOnMap somePoint) {
		wall.remove(somePoint);
	}
	public int size() {
		return this.size;
	}
	
//	TODO test this function
	public boolean isWallBetweenTwoPoints(OnePointOnMap firstPoint, OnePointOnMap secondPoint) {
		if(firstPoint.getX() != secondPoint.getX() && firstPoint.getY() != secondPoint.getY()) {
			return false;
		}
		else if(firstPoint.equals(secondPoint) || firstPoint.adjoiningPoint(secondPoint)) {
			return false;
		}
		else {
			for(OnePointOnMap wallPoint : wall) {
				if(firstPoint.getY() - secondPoint.getY() > 0) { //second point is below
					if(wallPoint.getX() == firstPoint.getX() && wallPoint.getY() > secondPoint.getY() && 
							wallPoint.getY() < firstPoint.getY()) {
						return true;
					}
				}
				else if(firstPoint.getY() - secondPoint.getY() < 0) { //second point is above
					if(wallPoint.getX() == firstPoint.getX() && wallPoint.getY() < secondPoint.getY() &&
							wallPoint.getY() > firstPoint.getY()) {
						return true;
					}
				}
				else if(firstPoint.getX() - secondPoint.getX() > 0) { //second point is left
					if(wallPoint.getY() == firstPoint.getY() && wallPoint.getX() > secondPoint.getX() &&
							wallPoint.getX() < firstPoint.getX()) {
						return true;
					}
				}
				else if(firstPoint.getX() - secondPoint.getX() < 0) { //second point is right
					if(wallPoint.getY() == firstPoint.getY() && wallPoint.getX() < secondPoint.getX() && 
							wallPoint.getX() > firstPoint.getX()) {
						return true;
					}
				}
			}
			return false;
		}
	}
}
