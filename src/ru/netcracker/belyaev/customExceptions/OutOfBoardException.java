package ru.netcracker.belyaev.customExceptions;

import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.models.Board;

public class OutOfBoardException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1989626013573997499L;

	public OutOfBoardException() {
		super();
	}
	
	public static void check(OnePointOnMap point, Board board) throws OutOfBoardException {
		if(board.isOutside(point)) {
			throw new OutOfBoardException();
		}
	}
}
