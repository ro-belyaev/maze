package ru.netcracker.belyaev.enums;

import ru.netcracker.belyaev.model.entities.OnePointOnMap;

public enum Direction {
	LEFT,
	RIGHT,
	UP,
	DOWN;
	
	public static Direction recognizeDirection(OnePointOnMap startPoint, OnePointOnMap endPoint) {
		if(endPoint.getX() < startPoint.getX() && endPoint.getY() == startPoint.getY()) {
			return Direction.LEFT;
		}
		else if(endPoint.getX() > startPoint.getX() && endPoint.getY() == startPoint.getY()) {
			return Direction.RIGHT;
		}
		else if(endPoint.getY() < startPoint.getY() && endPoint.getX() == startPoint.getX()) {
			return Direction.DOWN;
		}
		else if(endPoint.getY() > startPoint.getY() && endPoint.getX() == startPoint.getX()) {
			return Direction.UP;
		}
		else {
			return null;
		}
	}
	
	public static Direction recognizeDirection(int startX, int startY, int endX, int endY) {
		OnePointOnMap startPoint = new OnePointOnMap(startX, startY);
		OnePointOnMap endPoint = new OnePointOnMap(endX, endY);
		return Direction.recognizeDirection(startPoint, endPoint);
	}
	
	public static Direction recognizeDirection(String dir) {
		Direction direction = null;
		switch (dir.toLowerCase()) {
			case "up":
				direction = Direction.UP;
				break;
			case "down":
				direction = Direction.DOWN;
				break;
			case "left":
				direction = Direction.LEFT;
				break;
			case "right":
				direction = Direction.RIGHT;
				break;
		}
		return direction;
	}
	
	public Direction reverseDirection() {
		if(this == Direction.UP) {
			return Direction.DOWN;
		}
		else if(this == Direction.DOWN)  {
			return Direction.UP;
		}
		else if(this == Direction.RIGHT) {
			return Direction.LEFT;
		}
		else if(this == Direction.LEFT) {
			return Direction.RIGHT;
		}
		else {
			return null;
		}
	}
}
