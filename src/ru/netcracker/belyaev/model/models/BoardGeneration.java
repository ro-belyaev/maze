package ru.netcracker.belyaev.model.models;

import java.io.IOException;
import java.io.StringReader;
//import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.*;
import javax.xml.xpath.*;

import ru.netcracker.belyaev.customExceptions.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ru.netcracker.belyaev.enums.Colors;
import ru.netcracker.belyaev.model.entities.Arch;
import ru.netcracker.belyaev.model.entities.Exit;
import ru.netcracker.belyaev.model.entities.Mage;
import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.entities.Player;
import ru.netcracker.belyaev.model.entities.River;
import ru.netcracker.belyaev.model.entities.Treasure;
import ru.netcracker.belyaev.model.entities.Wall;

public class BoardGeneration {
	
	public static int generateTreasureColorID(Game game) {
		int numOfColours = Colors.values().length;
		int colorID = 0;
		boolean notGenerated = true;
		while(notGenerated) {
			colorID = new Random().nextInt(numOfColours);
			if(TreasureModel.getTreasureByColorID(colorID, game) == null) {
				notGenerated = false;
			}
		}
		return colorID;
	}
	
	public static OnePointOnMap getPoint(Object context, XPath xpath) throws XPathExpressionException {
		Object pointX = xpath.evaluate("./@pointX", context, XPathConstants.NUMBER);
		Object pointY = xpath.evaluate("./@pointY", context, XPathConstants.NUMBER);
		int x = ((Double) pointX).intValue();
		int y = ((Double) pointY).intValue();
		return new OnePointOnMap(x, y);
	}
	
	public static void generateWall(Document doc, XPath xpath, Board board) throws XPathExpressionException, OutOfBoardException, NotAdjoiningWallPoints {
		NodeList wallNodes = (NodeList) xpath.evaluate("//configuration/boardElement[@type='wall']", doc, XPathConstants.NODESET);
		Wall wall = new Wall();
		for(int i = 0; i < wallNodes.getLength(); i++) {
			NodeList wallElements = (NodeList) xpath.evaluate("./element", wallNodes.item(i), XPathConstants.NODESET);
			OnePointOnMap firstPoint = BoardGeneration.getPoint(wallElements.item(0), xpath);
			OnePointOnMap secondPoint = BoardGeneration.getPoint(wallElements.item(1), xpath);
			OutOfBoardException.check(firstPoint, board);
			OutOfBoardException.check(secondPoint, board);
			if(!wall.addPortal(firstPoint, secondPoint, 0)) {
				throw new NotAdjoiningWallPoints();
			}
		}
		board.setWall(wall);
	}
	
	public static void generateRiver(Document doc, XPath xpath, Board board) throws XPathExpressionException, RiverException, OutOfBoardException, RiverFlowsThroughWallException {
		NodeList riverNodes = (NodeList) xpath.evaluate("/configuration/boardElement[@type='river']", doc, XPathConstants.NODESET);
		River river = new River();
		for(int i = 0; i < riverNodes.getLength(); i++) {
			List<OnePointOnMap> pointsOfRiver = new ArrayList<>();
			int step = 1;
			NodeList listOfRiverElements = (NodeList) xpath.evaluate("./element", riverNodes.item(i), XPathConstants.NODESET);
			OnePointOnMap lastPoint = null;
			for(int j = 0; j < listOfRiverElements.getLength(); j++) {
				Object attrStep = xpath.evaluate("./@step", listOfRiverElements.item(j), XPathConstants.NUMBER);				
				step = ((Double) attrStep).intValue();
				OnePointOnMap point = BoardGeneration.getPoint(listOfRiverElements.item(j), xpath);
				OutOfBoardException.check(point, board);
				if(lastPoint != null) {
					RiverFlowsThroughWallException.check(lastPoint, point, board);
				}
				pointsOfRiver.add(point);
				lastPoint = point;
			}
			if(!river.addRiver(step, pointsOfRiver)) {
				throw new RiverException();
			}
		}
		board.setRiver(river);
	}
	
	public static void generatePlayers(Document doc, XPath xpath, Board board) throws XPathExpressionException, TooLessPlayersException, OutOfBoardException {
		NodeList playerNodes = (NodeList) xpath.evaluate("/configuration/boardElement[@type='player']", doc, XPathConstants.NODESET);
		for(int i = 0; i < playerNodes.getLength(); i++) {
			NodeList listOfPlayerElements = (NodeList) xpath.evaluate("./element", playerNodes.item(i), XPathConstants.NODESET);
			for(int j = 0; j < listOfPlayerElements.getLength(); j++) {
				Object attrName = xpath.evaluate("./@name", listOfPlayerElements.item(j), XPathConstants.STRING);
				String name = (String) attrName;
				int uid = board.incNumOfPlayers();
				OnePointOnMap point = BoardGeneration.getPoint(listOfPlayerElements.item(j), xpath);
				Player player = new Player(name, point, uid);
				OutOfBoardException.check(point, board);
				board.addPlayer(player);
			}
		}
		TooLessPlayersException.check(board);
	}
	
	public static void generateTreasures(Document doc, XPath xpath, Board board) throws XPathExpressionException, TooManyTreasuresException, OutOfBoardException {
		NodeList treasureNodes = (NodeList) xpath.evaluate("/configuration/boardElement[@type='treasure']", doc, XPathConstants.NODESET);
		for(int i = 0; i < treasureNodes.getLength(); i++) {
			NodeList listOfTreasureElements = (NodeList) xpath.evaluate("./element", treasureNodes.item(i), XPathConstants.NODESET);
			for(int j = 0; j < listOfTreasureElements.getLength(); j++) {				
				Object attrReal = xpath.evaluate("./@real", listOfTreasureElements.item(j), XPathConstants.STRING);
				boolean real = Boolean.parseBoolean((String) attrReal);
				OnePointOnMap point = BoardGeneration.getPoint(listOfTreasureElements.item(j), xpath);
				board.incNumOfTreasures();
				TooManyTreasuresException.check(board);
				int colorID = generateTreasureColorID(board.getGame());
				Treasure treasure = new Treasure(colorID, point, real);
				OutOfBoardException.check(point, board);
				board.addTreasure(treasure);
			}
		}
	}
	
	public static void generateArch(Document doc, XPath xpath, Board board) throws XPathExpressionException, OutOfBoardException {
		NodeList archNodes = (NodeList) xpath.evaluate("./configuration/boardElement[@type='arch']", doc, XPathConstants.NODESET);
		Arch arch = new Arch();		
		for(int i = 0; i < archNodes.getLength(); i++) {
			NodeList archElements = (NodeList) xpath.evaluate("./element", archNodes.item(i), XPathConstants.NODESET);
			for(int j = 0; j < archElements.getLength(); j++) {
				OnePointOnMap point = BoardGeneration.getPoint(archElements.item(j), xpath);
				OutOfBoardException.check(point, board);
				arch.addArch(point);
			}
		}
		board.setArch(arch);
	}
	
	public static void generateMage(Document doc, XPath xpath, Board board) throws XPathExpressionException, OutOfBoardException {
		NodeList mageNodes = (NodeList) xpath.evaluate("./configuration/boardElement[@type='mage']", doc, XPathConstants.NODESET);
		Mage mage = new Mage();
		for(int i = 0; i < mageNodes.getLength(); i++) {
			NodeList mageElements = (NodeList) xpath.evaluate("./element", mageNodes.item(i), XPathConstants.NODESET);
			for(int j = 0; j < mageElements.getLength(); j++) {
				OnePointOnMap point = BoardGeneration.getPoint(mageElements.item(j), xpath);
				OutOfBoardException.check(point, board);
				mage.addMage(point);
			}
		}
		board.setMage(mage);
	}
	
	public static void generateExit(Document doc, XPath xpath, Board board) throws XPathExpressionException, NoExitException, OutOfBoardException {
		NodeList exitNodes = (NodeList) xpath.evaluate("/configuration/boardElement[@type='exit']", doc, XPathConstants.NODESET);
		Exit exit = new Exit();
		for(int i = 0; i < exitNodes.getLength(); i++) {
			NodeList exitElements = (NodeList) xpath.evaluate("./element", exitNodes.item(i), XPathConstants.NODESET);
			for(int j = 0; j < exitElements.getLength(); j++) {
				OnePointOnMap point = BoardGeneration.getPoint(exitElements.item(j), xpath);
				OutOfBoardException.check(point, board);
				exit.addExit(point);
			}
		}
		board.setExit(exit);
		NoExitException.check(board);
	}
	
	public static void generateBoard(Document doc, XPath xpath, Board board) throws XPathExpressionException, NoBoardSizeException, WrongBoardSizeException {
		NodeList newBoardSize = (NodeList) xpath.evaluate("/configuration/boardElement[@type='boardSize']/element", doc,
				XPathConstants.NODE);
		if(newBoardSize == null) {
			throw new NoBoardSizeException();
		}
		OnePointOnMap boardSize = BoardGeneration.getPoint(newBoardSize, xpath);
		board.setSizeX(boardSize.getX());
		board.setSizeY(boardSize.getY());
		WrongBoardSizeException.check(board);
	}
	
	public static void generate(Board board, String gameXml) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, TooManyTreasuresException, RiverException, TooLessPlayersException, NoExitException, OutOfBoardException, NoBoardSizeException, WrongBoardSizeException, NotAdjoiningWallPoints, RiverFlowsThroughWallException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
//		Document doc = builder.parse("C:/Users/Roman/study/NetCracker/projects/maze_web/src/configuration.xml");
		Document doc = builder.parse(new InputSource(new StringReader(gameXml)));
		
//		InputStream xml = BoardGeneration.class.getResourceAsStream("C:/Users/Roman/study/NetCracker/projects/maze_web/src/configuration.xml");
//		Document doc = builder.parse(xml);
		
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		
		BoardGeneration.generateBoard(doc, xpath, board);
		BoardGeneration.generateWall(doc, xpath, board);
		BoardGeneration.generateRiver(doc, xpath, board);
		BoardGeneration.generatePlayers(doc, xpath, board);
		BoardGeneration.generateTreasures(doc, xpath, board);
		BoardGeneration.generateArch(doc, xpath, board);
		BoardGeneration.generateMage(doc, xpath, board);
		BoardGeneration.generateExit(doc, xpath, board);		
	}
}
