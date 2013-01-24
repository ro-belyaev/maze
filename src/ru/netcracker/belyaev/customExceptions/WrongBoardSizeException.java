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
	
	public static void check() throws WrongBoardSizeException {
		int sizeX = Board.getInstance().getSizeX();
		int sizeY = Board.getInstance().getSizeY();
		int minSizeX = Board.getInstance().getMinSizeX();
		int minSizeY = Board.getInstance().getMinSizeY();
		int maxSizeX = Board.getInstance().getMaxSizeX();
		int maxSizeY = Board.getInstance().getMaxSizeY();
		if(sizeX < minSizeX || sizeX > maxSizeX || sizeY < minSizeY || sizeY > maxSizeY) {
			throw new WrongBoardSizeException();
		}
	}
}
