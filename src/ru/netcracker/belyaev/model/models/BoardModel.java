package ru.netcracker.belyaev.model.models;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

//import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import ru.netcracker.belyaev.controller.OneCellOnBoard;
import ru.netcracker.belyaev.customExceptions.ElementOnWallException;
import ru.netcracker.belyaev.customExceptions.NoBoardSizeException;
import ru.netcracker.belyaev.customExceptions.NoExitException;
import ru.netcracker.belyaev.customExceptions.OutOfBoardException;
import ru.netcracker.belyaev.customExceptions.RiverException;
import ru.netcracker.belyaev.customExceptions.TooLessPlayersException;
import ru.netcracker.belyaev.customExceptions.TooManyTreasuresException;
import ru.netcracker.belyaev.customExceptions.WrongBoardSizeException;
import ru.netcracker.belyaev.enums.Colors;
import ru.netcracker.belyaev.enums.Notification;

public class BoardModel {
	private MessengerModel messenger;
	private Game game;
//	static Logger log = Logger.getLogger(BoardModel.class.getName());
	
	public void setMessenger(MessengerModel messenger) {
		this.messenger = messenger;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
	public OneCellOnBoard[][] getBoardSnapshot() {
		OneCellOnBoard[][] snapshot = Board.getInstance().getBoardSnapshot();
		if(snapshot == null) {
			messenger.notifyUser(Notification.BOARD_IS_NOT_CREATED);
		}
		return snapshot;
	}
	
	public void generateBoard() {
		if(Board.getInstance().isBoardCreated()) {
			messenger.notifyUser(Notification.BOARD_IS_ALREADY_CREATED);
			return;
		}
		try {
			Board.getInstance().generateBoard();
			game.resetGame();
			messenger.notifyUser(Notification.BOARD_GENERATION);
			game.newMove();
		} catch (XPathExpressionException e) {
			System.out.println("malformed xpath request. developer is dumb");
			dropBoard();
//			log.info("failed!", e);
		} catch (ParserConfigurationException e) {
			System.out.println("parser configuration exception");
			dropBoard();
//			log.info("failed!", e);
		} catch (SAXException e) {
			System.out.println("configuration xml file is malformed");
			dropBoard();
//			log.info("failed!", e);
		} catch (FileNotFoundException e) {
			System.out.println("confuguration xml file not found");
			dropBoard();
//			log.info("failed!", e);
		} catch (IOException e) {
			System.out.println("io exception while creating the game");
			dropBoard();
//			log.info("failed!", e);
		} catch (ElementOnWallException e) {
			System.out.println("some element is on wall in initial game setup");
			dropBoard();
//			log.info("failed!", e);
		} catch (NoExitException e) {
			System.out.println("you should specify at least one exit point on map");
			dropBoard();
//			log.info("failed!", e);
		} catch (OutOfBoardException e) {
			System.out.println("some element in initial game setup is out of board");
			dropBoard();
//			log.info("failed!", e);
		} catch (RiverException e) {
			System.out.println("your river setup on map is wrong");
			dropBoard();
//			log.info("failed!", e);
		} catch (TooLessPlayersException e) {
			System.out.println("you shoud specify at least 2 players");
			dropBoard();
//			log.info("failed!", e);
		} catch (TooManyTreasuresException e) {
			System.out.println("you specify too many treasures. Maximum number is " + Colors.values().length);
			dropBoard();
//			log.info("failed!", e);
		} catch (WrongBoardSizeException e) {
			int minSizeX = Board.getInstance().getMinSizeX();
			int minSizeY = Board.getInstance().getMinSizeY();
			int maxSizeX = Board.getInstance().getMaxSizeX();
			int maxSizeY = Board.getInstance().getMaxSizeY();
			System.out.println("you specify wrong size of board. Mim X is " + minSizeX + ", max X is " +
					maxSizeX + ", min Y is " + minSizeY + ", max Y is " + maxSizeY);
			dropBoard();
//			log.info("failed!", e);
		} catch (NoBoardSizeException e) {
			System.out.println("you didn't specify board size");
			dropBoard();
//			log.info("failed!", e);
		} 
	}
	
	public void dropBoard() {
		Board.getInstance().dropBoard();
	}
}
