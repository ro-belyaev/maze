package ru.netcracker.belyaev.customExceptions;

import ru.netcracker.belyaev.model.models.Board;

public class TooLessPlayersException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4518255005026927697L;

	public TooLessPlayersException() {
		super();
	}
	
	public static void check() throws TooLessPlayersException {
		if(Board.getInstance().getNumOfPlayer() < 2) {
			throw new TooLessPlayersException();
		}
	}
}
