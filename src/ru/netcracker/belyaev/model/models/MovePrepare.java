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
	
	public void checkMageOnThisPoint(OnePointOnMap point) {
		Mage mage = game.getBoard().getMage();
		if(mage != null && mage.isMageOnThisPoint(point)) {
			informer.addPrepareInformer(GameCases.FIND_MAGE);
			messenger.informAboutAction(GameCases.FIND_MAGE, point, game.getCurrentPlayer());
		}
	}
	
	public void checkArchOnThisPoint(OnePointOnMap point) {
		Arch arch = game.getBoard().getArch();
		if(arch != null && arch.isArchOnThisPoint(point)) {
			informer.addPrepareInformer(GameCases.UNDER_ARCH);
			messenger.informAboutAction(GameCases.UNDER_ARCH, point, game.getCurrentPlayer());
		}
	}
	
	public void checkExitOnThisPoint(OnePointOnMap point) {
		Exit exit = game.getBoard().getExit();
		if(exit != null && exit.isExitOnThisPoint(point)) {
			informer.addPrepareInformer(GameCases.EXIT_POINT);
			messenger.informAboutAction(GameCases.EXIT_POINT, point, game.getCurrentPlayer());
		}
	}
	
	public void checkTreasureOnThisPoint(OnePointOnMap point) {
		List<Treasure> treasure = getTreasureOnThisPoint(point);
		if(treasure != null) {
			int[] colourID = new int[treasure.size()];
			for(int i = 0; i < treasure.size(); i++) {
				informer.addPrepareInformer(GameCases.FIND_TREASURE, treasure.get(i).getColorID());
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
