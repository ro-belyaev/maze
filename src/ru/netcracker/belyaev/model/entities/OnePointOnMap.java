package ru.netcracker.belyaev.model.entities;

import java.util.ArrayList;
import java.util.List;

import ru.netcracker.belyaev.enums.Direction;

public class OnePointOnMap {
	private int x, y;
	public OnePointOnMap(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public boolean adjoiningPoint(OnePointOnMap secondPoint) {
		if((this.x == secondPoint.getX()) && (Math.abs(this.y - secondPoint.getY()) == 1)) {
			return true;
		}
		else if((this.y == secondPoint.getY()) && (Math.abs(this.x - secondPoint.getX()) == 1)) {
			return true;
		}
		return false;
	}
	
	public OnePointOnMap nextPoint(Direction direction) {
		int destX = this.x;
		int destY = this.y;
		switch (direction) {
			case LEFT:
				destX--;
				break;
			case RIGHT:
				destX++;
				break;
			case UP:
				destY++;
				break;
			case DOWN:
				destY--;
				break;
		}
		return new OnePointOnMap(destX, destY);
	}
	
	public boolean equals(OnePointOnMap anotherPoint) {
		if(x == anotherPoint.getX() && y == anotherPoint.getY()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isPointInList(List<OnePointOnMap> list) {
		for(OnePointOnMap somePoint : list) {
			if(this.x == somePoint.getX() && this.y == somePoint.getY()) {
				return true;
			}
		}
		return false;
	}
	
	public List<OnePointOnMap> getPointsOnOneDirection(List<OnePointOnMap> allPoints, Direction direction) {
		List<OnePointOnMap> returnList = new ArrayList<>();
		for(OnePointOnMap somePoint : allPoints) {
			switch (direction) {
				case UP:
					if(somePoint.getX() == this.x && somePoint.getY() >= this.y) {
						returnList.add(somePoint);
					}
					break;
				case DOWN:
					if(somePoint.getX() == this.x && somePoint.getY() <= this.y) {
						returnList.add(somePoint);
					}
					break;
				case RIGHT:
					if(somePoint.getY() == this.y && somePoint.getX() >= this.x) {
						returnList.add(somePoint);
					}
					break;
				case LEFT:
					if(somePoint.getY() == this.y && somePoint.getX() <= this.x) {
						returnList.add(somePoint);
					}
					break;
			}
		}
		return returnList;
	}
	
	public OnePointOnMap getNearestPointInDirection(List<OnePointOnMap> line, Direction direction) {
		OnePointOnMap closest = null;
		for(OnePointOnMap somePoint : line) {
			if(closest == null) {
				closest = somePoint;
			}
			else {
				boolean pointIsCloser = false;
				switch (direction) {
					case UP:
						if(somePoint.getY() < closest.getY()) {
							pointIsCloser = true;
						}
						break;
					case DOWN:
						if(somePoint.getY() > closest.getY()) {
							pointIsCloser = true;
						}
						break;
					case RIGHT:
						if(somePoint.getX() < closest.getX()) {
							pointIsCloser = true;
						}
						break;
					case LEFT:
						if(somePoint.getX() > closest.getX()) {
							pointIsCloser = true;
						}
						break;
				}
				if(pointIsCloser) {
					closest = somePoint;
				}
			}
		}
		return closest;
	}
	
	public boolean isPointOnOneLine(OnePointOnMap somePoint) {
		if(this.x == somePoint.getX() || this.y == somePoint.getY()) {
			return true;
		} else {
			return false;
		}
	}
}
