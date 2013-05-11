package ru.netcracker.belyaev.customExceptions;

import ru.netcracker.belyaev.model.models.Board;

public class WrongBoardSizeException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6313566110837689546L;

	public WrongBoardSizeException() {
		super();
	}
	
	public static void check(Board board) throws WrongBoardSizeException {
		int sizeX = board.getSizeX();
		int sizeY = board.getSizeY();
		int minSizeX = board.getMinSizeX();
		int minSizeY = board.getMinSizeY();
		int maxSizeX = board.getMaxSizeX();
		int maxSizeY = board.getMaxSizeY();
		if(sizeX < minSizeX || sizeX > maxSizeX || sizeY < minSizeY || sizeY > maxSizeY) {
			throw new WrongBoardSizeException();
		}
	}
}
