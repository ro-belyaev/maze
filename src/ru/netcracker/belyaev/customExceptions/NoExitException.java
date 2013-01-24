package ru.netcracker.belyaev.customExceptions;

import ru.netcracker.belyaev.model.entities.Exit;
import ru.netcracker.belyaev.model.models.Board;

public class NoExitException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8318524549023204763L;

	public NoExitException() {
		super();
	}
	
	public static void check() throws NoExitException {
		Exit exit = Board.getInstance().getExit();
		if(exit == null || exit.size() == 0) {
			throw new NoExitException();
		}
	}
}
