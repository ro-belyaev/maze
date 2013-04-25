package ru.netcracker.belyaev.model.models;

import java.util.List;

import ru.netcracker.belyaev.controller.OneCellOnBoard;
import ru.netcracker.belyaev.controller.OnePlayer;
import ru.netcracker.belyaev.enums.Direction;
import ru.netcracker.belyaev.model.entities.Arch;
import ru.netcracker.belyaev.model.entities.Exit;
import ru.netcracker.belyaev.model.entities.Mage;
import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.entities.Player;
import ru.netcracker.belyaev.model.entities.River;
import ru.netcracker.belyaev.model.entities.Treasure;
import ru.netcracker.belyaev.model.entities.Wall;
//import ru.netcracker.belyaev.model.entities.River;
import ru.netcracker.belyaev.model.entities.Portal;

public class BoardSnapshot {
	int sizeX;
	int sizeY;
	OneCellOnBoard[][] snapshot;
	
	public BoardSnapshot(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.snapshot = new OneCellOnBoard[sizeY][sizeX];
		resetBoardSnapshot();
	}
	
	public OneCellOnBoard[][] getBoardSnapshot() {
		return this.snapshot;
	}
	
	public void resetBoardSnapshot() {
		for(int i=0; i < this.sizeY; i++) {
			for(int j=0; j < this.sizeX; j++) {
				snapshot[i][j] = new OneCellOnBoard(j, i);
			}
		}
	}
	
	public void generateBoardSnapshot() {
		if(!Board.getInstance().isBoardCreated()) {
			return;
		}
		
		River river = Board.getInstance().getRiver();
		Wall wall = Board.getInstance().getWall();
		Arch arch = Board.getInstance().getArch();
		Mage mage = Board.getInstance().getMage();
		Exit exit = Board.getInstance().getExit();
		List<Treasure> treasure = Board.getInstance().getTreasure();
		List<Player> players = Board.getInstance().getPlayers();
		
		if(river != null) {
			generateRiverSnapshot(river);
		}
		if(wall != null) {
			generateWallSnapshot(wall);
		}
		if(arch != null) {
			generateArchSnapshot(arch);
		}
		if(exit != null) {
			generateExitSnapshot(exit);
		}
		if(mage != null) {
			generateMageSnapshot(mage);
		}
		if(treasure != null) {
			generateTreasureSnapshot(treasure);
		}
		if(players != null) {
			generatePlayersSnapshot(players);
		}
	}
	
	public void generateRiverSnapshot(River river) {
		for(Portal portal : river.getPortals()) {
			int x = portal.getStartPoint().getX();
			int y = portal.getStartPoint().getY();
			Direction direction = Direction
					.recognizeDirection(portal.getStartPoint(), portal.getDestinationPoint());
			snapshot[y][x].setRiverDirection(direction);
			snapshot[y][x].setRiverStep(portal.getStep());
		}
	}
	public void generateWallSnapshot(Wall wall) {
		for(Portal wallPart: wall.getWall()) {
			OnePointOnMap firstPoint = wallPart.getStartPoint();
			OnePointOnMap secondPoint = wallPart.getDestinationPoint();
			int x1 = firstPoint.getX();
			int y1 = firstPoint.getY();
			int x2 = secondPoint.getX();
			int y2 = secondPoint.getY();
			snapshot[y1][x1].setWall();
			snapshot[y2][x2].setWall();
		}
	}
	public void generateArchSnapshot(Arch arch) {
		for(OnePointOnMap point : arch.getArch()) {
			int x = point.getX();
			int y = point.getY();
			snapshot[y][x].setArch();
		}
	}
	public void generateExitSnapshot(Exit exit) {
		for(OnePointOnMap point : exit.getExit()) {
			int x = point.getX();
			int y = point.getY();
			snapshot[y][x].setExit();
		}
	}
	public void generateMageSnapshot(Mage mage) {
		for(OnePointOnMap point : mage.getMage()) {
			int x = point.getX();
			int y = point.getY();
			snapshot[y][x].setMage();
		}
	}
	public void generateTreasureSnapshot(List<Treasure> treasure) {
		for(Treasure oneTreasure : treasure) {
			if(!oneTreasure.isFound()) {
				int x = oneTreasure.getPosition().getX();
				int y = oneTreasure.getPosition().getY();
				if(oneTreasure.isReal()) {
					snapshot[y][x].setRealTreasureCount();
				}
				else {
					snapshot[y][x].setFakeTreasureCount();
				}
			}
		}
	}
	public void generatePlayersSnapshot(List<Player> players) {
		for(Player player : players) {
			OnePlayer onePlayer = player.convertToOnePlayer();
			int x = player.getPosition().getX();
			int y = player.getPosition().getY();
			snapshot[y][x].getPlayers().add(onePlayer);
		}
	}
}
