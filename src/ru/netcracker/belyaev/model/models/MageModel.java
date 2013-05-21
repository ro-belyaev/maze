package ru.netcracker.belyaev.model.models;

import ru.netcracker.belyaev.enums.GameCases;
import ru.netcracker.belyaev.enums.Notification;
import ru.netcracker.belyaev.enums.PlayerActions;
import ru.netcracker.belyaev.model.entities.Mage;
import ru.netcracker.belyaev.model.entities.Player;
import ru.netcracker.belyaev.model.entities.Treasure;

public class MageModel {
	private MessengerModel messenger;
	private InformerModel informer;
	private Game game;
	
	public void setMessenger(MessengerModel messenger) {
		this.messenger = messenger;
	}
	public void setInformerModel(InformerModel informer) {
		this.informer = informer;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
	public boolean askPrediction() {
		Player player = game.getCurrentPlayer();
		Treasure treasure = player.getTreasure();
		if(treasure == null) {
			messenger.notifyUser(Notification.HAVE_NO_TREASURE);
		}
		else {
			Mage mage = game.getBoard().getMage();
			if(!mage.isMageOnThisPoint(game.getCurrentPlayer().getPosition())) {
				messenger.notifyUser(Notification.NO_MAGE);
			}
			else {
				if(mage.makePrediction(treasure)) {
					informer.addResultInformer(GameCases.PREDICTION_REAL);
					messenger.informAboutAction(GameCases.PREDICTION_REAL, player.getPosition(), player);
				}
				else {
					informer.addResultInformer(GameCases.PREDICTION_FAKE);
					messenger.informAboutAction(GameCases.PREDICTION_FAKE, player.getPosition(), player);
				}
				game.setLastPlayerAction(PlayerActions.ASK_PREDICTION);
				game.setDirectionOfLastPlayerAction(null);
				game.newMove();
				return true;
			}
		}
		return false;
	}
}
