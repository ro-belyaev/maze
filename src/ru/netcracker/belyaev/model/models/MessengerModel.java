package ru.netcracker.belyaev.model.models;

import ru.netcracker.belyaev.controller.Controller;
import ru.netcracker.belyaev.enums.*;
import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.entities.Player;

public class MessengerModel {
	private Controller controller;
	
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	public void informAboutAction(GameCases action, OnePointOnMap startPoint, OnePointOnMap destinationPoint, Player player) {
		int startX = startPoint.getX();
		int startY = startPoint.getY();
		int destX = destinationPoint.getX();
		int destY = destinationPoint.getY();
		controller.informAboutAction(action, startX, startY, destX, destY, player.convertToOnePlayer());
	}
	
	public void informAboutAction(GameCases action, OnePointOnMap position, Player player, int... colourID) {
		int x = position.getX();
		int y = position.getY();
		controller.informAboutAction(action, x, y, player.convertToOnePlayer(), colourID);
	}
	
	public void informAboutAction(GameCases action, OnePointOnMap position, Player player) {
		int x = position.getX();
		int y = position.getY();
		controller.informAboutAction(action, x, y, player.convertToOnePlayer());
	}
	
	public void informAboutAction(GameCases action) {
		controller.informAboutAction(action);
	}
	
	public void notifyUser(Notification notification) {
		controller.notifyUser(notification);
	}
}
