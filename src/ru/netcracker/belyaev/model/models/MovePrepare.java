package ru.netcracker.belyaev.model.models;

import java.util.ArrayList;
import java.util.List;

import ru.netcracker.belyaev.enums.GameCases;
import ru.netcracker.belyaev.model.entities.Arch;
import ru.netcracker.belyaev.model.entities.Exit;
import ru.netcracker.belyaev.model.entities.Mage;
import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.entities.Treasure;

public class MovePrepare {
	private MessengerModel messenger;
	private Game game;
	
	public void setMessenger(MessengerModel messenger) {
		this.messenger = messenger;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void checkMageOnThisPoint(OnePointOnMap point) {
		Mage mage = game.getBoard().getMage();
		if(mage != null && mage.isMageOnThisPoint(point)) {
			messenger.informAboutAction(GameCases.FIND_MAGE, point, game.getCurrentPlayer());
		}
	}
	
	public void checkArchOnThisPoint(OnePointOnMap point) {
		Arch arch = game.getBoard().getArch();
		if(arch != null && arch.isArchOnThisPoint(point)) {
			messenger.informAboutAction(GameCases.UNDER_ARCH, point, game.getCurrentPlayer());
		}
	}
	
	public void checkExitOnThisPoint(OnePointOnMap point) {
		Exit exit = game.getBoard().getExit();
		if(exit != null && exit.isExitOnThisPoint(point)) {
			messenger.informAboutAction(GameCases.EXIT_POINT, point, game.getCurrentPlayer());
		}
	}
	
	public void checkTreasureOnThisPoint(OnePointOnMap point) {
		List<Treasure> treasure = getTreasureOnThisPoint(point);
		if(treasure != null) {
			int[] colourID = new int[treasure.size()];
			for(int i = 0; i < treasure.size(); i++) {
				colourID[i] = treasure.get(i).getColorID();
			}
			messenger.informAboutAction(GameCases.FIND_TREASURE, point, game.getCurrentPlayer(), colourID);
		}
	}
	
	public List<Treasure> getTreasureOnThisPoint(OnePointOnMap point) {
		List<Treasure> treasureOnThisPoint = new ArrayList<>();
		List<Treasure> allTreasure = game.getBoard().getTreasure();
		if(allTreasure != null) {
			for(Treasure someTreasure : allTreasure) {
				if(someTreasure.getPosition().equals(point) && !someTreasure.isFound()) {
					treasureOnThisPoint.add(someTreasure);
				}
			}
			if(treasureOnThisPoint.size() == 0) {
				return null;
			}
			return treasureOnThisPoint;
		}
		else {
			return null;
		}
	}
	
}
