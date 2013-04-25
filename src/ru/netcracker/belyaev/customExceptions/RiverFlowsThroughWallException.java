package ru.netcracker.belyaev.customExceptions;

import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.entities.Wall;
import ru.netcracker.belyaev.model.models.Board;

public class RiverFlowsThroughWallException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5699565794196643678L;

	public RiverFlowsThroughWallException() {
		super();
	}
	
	public static void check(OnePointOnMap firstPoint, OnePointOnMap secondPoint) throws RiverFlowsThroughWallException {
		Wall wall = Board.getInstance().getWall();
		if(wall.isWallBetweenTwoPointsOnOneLine(firstPoint, secondPoint)) {
			throw new RiverFlowsThroughWallException();
		}
	}
}
