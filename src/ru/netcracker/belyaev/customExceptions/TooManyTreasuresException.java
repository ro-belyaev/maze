package ru.netcracker.belyaev.customExceptions;

import ru.netcracker.belyaev.enums.Colors;
import ru.netcracker.belyaev.model.models.Board;

public class TooManyTreasuresException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2362744820403300691L;
	public TooManyTreasuresException() {
		super();
	}
	
	public static void check(Board board) throws TooManyTreasuresException {
		if(board.getNumOfTreasures() > Colors.values().length) {
			throw new TooManyTreasuresException();
		}
	}
}
