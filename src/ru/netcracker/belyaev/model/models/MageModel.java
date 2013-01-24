package ru.netcracker.belyaev.model.models;

import ru.netcracker.belyaev.enums.GameCases;
import ru.netcracker.belyaev.enums.Notification;
import ru.netcracker.belyaev.model.entities.Mage;
import ru.netcracker.belyaev.model.entities.Player;
import ru.netcracker.belyaev.model.entities.Treasure;

public class MageModel {
	private MessengerModel messenger;
	private Game game;
	
	public void setMessenger(MessengerModel messenger) {
		this.messenger = messenger;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void askPrediction() {
		Player player = game.getCurrentPlayer();
		Treasure treasure = player.getTreasure();
		if(treasure == null) {
			messenger.notifyUser(Notification.HAVE_NO_TREASURE);
		}
		else {
			Mage mage = Board.getInstance().getMage();
			if(!mage.isMageOnThisPoint(game.getCurrentPlayer().getPosition())) {
				messenger.notifyUser(Notification.NO_MAGE);
			}
			else {
				if(mage.makePrediction(treasure)) {
					messenger.informAboutAction(GameCases.PREDICTION_REAL, player.getPosition(), player);
				}
				else {
					messenger.informAboutAction(GameCases.PREDICTION_FAKE, player.getPosition(), player);
				}
				game.newMove();
			}
		}
	}
}
