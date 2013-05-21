package ru.netcracker.belyaev.model.models;

import ru.netcracker.belyaev.enums.GameCases;
import ru.netcracker.belyaev.enums.Notification;
import ru.netcracker.belyaev.enums.PlayerActions;
import ru.netcracker.belyaev.model.entities.Exit;
import ru.netcracker.belyaev.model.entities.Player;
import ru.netcracker.belyaev.model.entities.Treasure;

public class ExitModel {
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
	
	public boolean exit() {
		Exit exit = game.getBoard().getExit();
		if(!exit.isExitOnThisPoint(game.getCurrentPlayer().getPosition())) {
			messenger.notifyUser(Notification.NO_EXIT);
		}
		else {
			informer.addActionInformer(PlayerActions.EXIT);
			Player player = game.getCurrentPlayer();
			Treasure treasure = player.getTreasure();
			if(treasure == null || !treasure.isReal()) {
				player.ExitFromMazeWithoutTreasure();
				messenger.informAboutAction(GameCases.EXIT_FROM_MAZE, player.getPosition(), player);
			}
			else {
				player.ExitFromMazeWithRealTreasure();
				messenger.informAboutAction(GameCases.EXIT_WITH_REAL_TREASURE, player.getPosition(), player);
			}
			game.setLastPlayerAction(PlayerActions.EXIT);
			game.setDirectionOfLastPlayerAction(null);
			game.newMove();
			return true;
		}
		return false;
	}
}
