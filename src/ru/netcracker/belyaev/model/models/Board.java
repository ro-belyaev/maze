package ru.netcracker.belyaev.model.models;

import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import ru.netcracker.belyaev.controller.OneCellOnBoard;
import ru.netcracker.belyaev.customExceptions.NoBoardSizeException;
import ru.netcracker.belyaev.customExceptions.NoExitException;
import ru.netcracker.belyaev.customExceptions.NotAdjoiningWallPoints;
import ru.netcracker.belyaev.customExceptions.OutOfBoardException;
import ru.netcracker.belyaev.customExceptions.RiverException;
import ru.netcracker.belyaev.customExceptions.RiverFlowsThroughWallException;
import ru.netcracker.belyaev.customExceptions.TooLessPlayersException;
import ru.netcracker.belyaev.customExceptions.TooManyTreasuresException;
import ru.netcracker.belyaev.customExceptions.WrongBoardSizeException;
import ru.netcracker.belyaev.database.api.DatabaseManagerFactory;
import ru.netcracker.belyaev.model.entities.Arch;
import ru.netcracker.belyaev.model.entities.Exit;
import ru.netcracker.belyaev.model.entities.Mage;
import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.entities.Player;
import ru.netcracker.belyaev.model.entities.River;
import ru.netcracker.belyaev.model.entities.Treasure;
import ru.netcracker.belyaev.model.entities.Wall;


public class Board {
//	private static volatile Board boardInstance;
	
	private boolean isBoardCreated = false;
	
	private int maxSizeX = 30, maxSizeY = 30;
	private int minSizeX = 4, minSizeY = 4;
	private int sizeX, sizeY;
	private Exit exit;
	private Wall wall;
	private River river;
	private Arch arch;
	private Mage mage;
	private List<Player> players;
	private List<Treasure> treasure;
	private int numOfPlayers = 0;
	private int numOfTreasures = 0;
	private Game game;
	
	BoardSnapshot boardSnapshot;
	
	public Board(Game game) {
		this.game = game;
	}
	
//	public static Board getInstance() {
//		if(boardInstance == null) {
//			synchronized(Board.class) {
//				if(boardInstance == null) {
//					boardInstance = new Board();
//				}
//			}
//		}
//		return boardInstance;
//	}
	public OneCellOnBoard[][] getBoardSnapshot() {
		if(boardSnapshot == null) {
			return null;
		}
		else {
			boardSnapshot.resetBoardSnapshot();
			boardSnapshot.generateBoardSnapshot();
			return boardSnapshot.getBoardSnapshot();
		}
	}
	
	public void generateBoard() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, TooManyTreasuresException, RiverException, TooLessPlayersException, NoExitException, OutOfBoardException, NoBoardSizeException, WrongBoardSizeException, NotAdjoiningWallPoints, RiverFlowsThroughWallException {
		BoardGeneration.generate(this);
		this.isBoardCreated = true;
		boardSnapshot = new BoardSnapshot(sizeX, sizeY, game);
	}
	public boolean restoreBoard() {
		if(DatabaseManagerFactory.getDatabaseManagerInstance().restoreGameState(game)) {
			this.isBoardCreated = true;
			boardSnapshot = new BoardSnapshot(sizeX, sizeY, game);
			return true;
		} else {
			return false;
		}
	}
	public boolean isBoardCreated() {
		return this.isBoardCreated;
	}

	public void dropBoard() {
		this.isBoardCreated = false;
		this.sizeX = this.sizeY = this.numOfPlayers = this.numOfTreasures = 0;
		players = null;
		treasure = null;
		exit = null;
		river = null;
		wall = null;
		arch = null;
		mage = null;
		boardSnapshot = null;
	}
	public void setSizeX(int x) {
		this.sizeX = x;
	}
	public void setSizeY(int y) {
		this.sizeY = y;
	}
	public int incNumOfPlayers() {
		return this.numOfPlayers++;
	}
	public int incNumOfTreasures() {
		return this.numOfTreasures++;
	}
	public void setRiver(River river) {
		this.river = river;
	}
	public void setWall(Wall wall) {
		this.wall = wall;
	}
	public void setMage(Mage mage) {
		this.mage = mage;
	}
	public void setExit(Exit exit) {
		this.exit = exit;
	}
	public void setArch(Arch arch) {
		this.arch = arch;
	}
	public void addPlayer(Player player) {
		if(this.players == null) {
			this.players = new ArrayList<>();
		}
		this.players.add(player);
	}
	public void addTreasure(Treasure treasure) {
		if(this.treasure == null) {
			this.treasure = new ArrayList<>();
		}
		this.treasure.add(treasure);
	}
	public boolean isOutside(OnePointOnMap point) {
		int x = point.getX();
		int y = point.getY();
		if(x >= 0 && x < this.sizeX && y >=0 && y < this.sizeY) {
			return false;
		}
		return true;
	}
	public List<Player> getPlayers() {
		return this.players;
	}
	public List<Treasure> getTreasure() {
		return this.treasure;
	}
	public Wall getWall() {
		return this.wall;
	}
	public River getRiver() {
		return this.river;
	}
	public Mage getMage() {
		return this.mage;
	}
	public Exit getExit() {
		return this.exit;
	}
	public Arch getArch() {
		return this.arch;
	}
	public int getNumOfPlayer() {
		return this.numOfPlayers;
	}
	public int getNumOfTreasures() {
		return this.numOfTreasures;
	}
	
	public int getSizeX() {
		return this.sizeX;
	}
	public int getSizeY() {
		return this.sizeY;
	}
	public int getMinSizeX() {
		return this.minSizeX;
	}
	public int getMinSizeY() {
		return this.minSizeY;
	}
	public int getMaxSizeX() {
		return this.maxSizeX;
	}
	public int getMaxSizeY() {
		return this.maxSizeY;
	}
	public Game getGame() {
		return this.game;
	}
}
