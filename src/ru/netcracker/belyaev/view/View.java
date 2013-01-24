package ru.netcracker.belyaev.view;

import ru.netcracker.belyaev.controller.*;
import ru.netcracker.belyaev.enums.GameCases;
import ru.netcracker.belyaev.enums.Notification;

public abstract class View {
	private Controller controller;
	public void setController(Controller controller) {
		this.controller = controller;
	}
	public abstract void refreshBoard(OneCellOnBoard[][] board);
	
	public abstract void informAboutAction(GameCases action, int startX, int startY, int destX, int destY, OnePlayer player);
	
	public abstract void informAboutAction(GameCases action, int startX, int startY, OnePlayer player, int... colourID);
	
	public abstract void informAboutAction(GameCases action, int x, int y, OnePlayer player);
	
	public abstract void informAboutAction(GameCases action);
	
	public abstract void notifyUser(Notification notification);
}
