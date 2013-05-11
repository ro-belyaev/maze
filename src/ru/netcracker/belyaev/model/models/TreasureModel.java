package ru.netcracker.belyaev.model.models;

import java.util.List;

import ru.netcracker.belyaev.enums.GameCases;
import ru.netcracker.belyaev.enums.Notification;
import ru.netcracker.belyaev.enums.PlayerActions;
import ru.netcracker.belyaev.model.entities.Player;
import ru.netcracker.belyaev.model.entities.Treasure;

public class TreasureModel {
	private MessengerModel messenger;
	private Game game;
	
	public void setMessenger(MessengerModel messenger) {
		this.messenger = messenger;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void upTreasure(int colorID) {
		Player player = game.getCurrentPlayer();
		if(player.hasTreasure()) {
			messenger.notifyUser(Notification.ALREADY_HAS_TREASURE);
		}
		else {
			Treasure treasure = getTreasureByColorID(colorID, this.game);
			if(treasure == null) {
				messenger.notifyUser(Notification.NO_SUCH_TREASURE);
			}
			else if(!treasure.getPosition().equals(player.getPosition())) {
				messenger.notifyUser(Notification.NO_SUCH_TREASURE);
			}
			else {
				player.takeTreasure(treasure);
				messenger.informAboutAction(GameCases.UP_TREASURE, player.getPosition(), player, treasure.getColorID());
				game.setLastPlayerAction(PlayerActions.TAKE_TREASURE);
				game.setDirectionOfLastPlayerAction(null);
				game.newMove();
			}
		}
	}
		
	public void dropTreasure() {
		Player player = game.getCurrentPlayer();
		if(!player.hasTreasure()) {
			messenger.notifyUser(Notification.HAVE_NO_TREASURE);
		}
		else {
			Treasure treasure = player.getTreasure();
			player.dropTreasure();
			messenger.informAboutAction(GameCases.DROP_TREASURE, player.getPosition(), player, treasure.getColorID());
			game.setLastPlayerAction(PlayerActions.DROP_TREASURE);
			game.newMove();
		}
	}
	
	public static Treasure getTreasureByColorID(int colorID, Game game) {
		List<Treasure> treasure = game.getBoard().getTreasure();
		if(treasure == null) {
			return null;
		}
		else {
			for(Treasure someTreasure : treasure) {
				if(someTreasure.getColorID() == colorID) {
					return someTreasure;
				}
			}
			return null;
		}
	}
}
