package ru.netcracker.belyaev.customExceptions;

import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.entities.Wall;
import ru.netcracker.belyaev.model.models.Board;

public class ElementOnWallException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4291552965723778299L;

	public ElementOnWallException() {
		super();
	}
	
	public static void check(OnePointOnMap point) throws ElementOnWallException {
		Wall wall = Board.getInstance().getWall();
		if(wall != null && wall.checkIfPointIsPartOfWall(point)) {
			throw new ElementOnWallException();
		}
	}
}
