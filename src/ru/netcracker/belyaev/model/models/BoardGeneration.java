package ru.netcracker.belyaev.model.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.*;
import javax.xml.xpath.*;

import ru.netcracker.belyaev.customExceptions.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
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
	
	public static int generateTreasureColorID() {
		int numOfColours = Colors.values().length;
		int colorID = 0;
		boolean notGenerated = true;
		while(notGenerated) {
			colorID = new Random().nextInt(numOfColours);
			if(TreasureModel.getTreasureByColorID(colorID) == null) {
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
	
	public static void generateWall(Document doc, XPath xpath) throws XPathExpressionException, OutOfBoardException {
		NodeList wallNodes = (NodeList) xpath.evaluate("//configuration/boardElement[@type='wall']", doc, XPathConstants.NODESET);
		Wall wall = new Wall();
		for(int i = 0; i < wallNodes.getLength(); i++) {
			NodeList wallElements = (NodeList) xpath.evaluate("./element", wallNodes.item(i), XPathConstants.NODESET);
			for(int j = 0; j < wallElements.getLength(); j++) {
				OnePointOnMap point = BoardGeneration.getPoint(wallElements.item(j), xpath);
				OutOfBoardException.check(point);
				wall.addPoint(point);
			}
		}
		Board.getInstance().setWall(wall);
	}
	
	public static void generateRiver(Document doc, XPath xpath) throws XPathExpressionException, RiverException, OutOfBoardException, ElementOnWallException {
		NodeList riverNodes = (NodeList) xpath.evaluate("/configuration/boardElement[@type='river']", doc, XPathConstants.NODESET);
		River river = new River();
		for(int i = 0; i < riverNodes.getLength(); i++) {
			List<OnePointOnMap> pointsOfRiver = new ArrayList<>();
			int step = 1;
			NodeList listOfRiverElements = (NodeList) xpath.evaluate("./element", riverNodes.item(i), XPathConstants.NODESET);
			for(int j = 0; j < listOfRiverElements.getLength(); j++) {
				Object attrStep = xpath.evaluate("./@step", listOfRiverElements.item(j), XPathConstants.NUMBER);				
				step = ((Double) attrStep).intValue();
				OnePointOnMap point = BoardGeneration.getPoint(listOfRiverElements.item(j), xpath);
				OutOfBoardException.check(point);
				ElementOnWallException.check(point);
				pointsOfRiver.add(point);
			}
			if(!river.addRiver(step, pointsOfRiver)) {
				throw new RiverException();
			}
		}
		Board.getInstance().setRiver(river);
	}
	
	public static void generatePlayers(Document doc, XPath xpath) throws XPathExpressionException, TooLessPlayersException, OutOfBoardException, ElementOnWallException {
		NodeList playerNodes = (NodeList) xpath.evaluate("/configuration/boardElement[@type='player']", doc, XPathConstants.NODESET);
		for(int i = 0; i < playerNodes.getLength(); i++) {
			NodeList listOfPlayerElements = (NodeList) xpath.evaluate("./element", playerNodes.item(i), XPathConstants.NODESET);
			for(int j = 0; j < listOfPlayerElements.getLength(); j++) {
				Object attrName = xpath.evaluate("./@name", listOfPlayerElements.item(j), XPathConstants.STRING);
				String name = (String) attrName;
				int uid = Board.getInstance().incNumOfPlayers();
				OnePointOnMap point = BoardGeneration.getPoint(listOfPlayerElements.item(j), xpath);
				Player player = new Player(name, point, uid);
				OutOfBoardException.check(point);
				ElementOnWallException.check(point);
				Board.getInstance().addPlayer(player);
			}
		}
		TooLessPlayersException.check();
	}
	
	public static void generateTreasures(Document doc, XPath xpath) throws XPathExpressionException, TooManyTreasuresException, ElementOnWallException, OutOfBoardException {
		NodeList treasureNodes = (NodeList) xpath.evaluate("/configuration/boardElement[@type='treasure']", doc, XPathConstants.NODESET);
		for(int i = 0; i < treasureNodes.getLength(); i++) {
			NodeList listOfTreasureElements = (NodeList) xpath.evaluate("./element", treasureNodes.item(i), XPathConstants.NODESET);
			for(int j = 0; j < listOfTreasureElements.getLength(); j++) {				
				Object attrReal = xpath.evaluate("./@real", listOfTreasureElements.item(j), XPathConstants.STRING);
				boolean real = Boolean.parseBoolean((String) attrReal);
				OnePointOnMap point = BoardGeneration.getPoint(listOfTreasureElements.item(j), xpath);
				Board.getInstance().incNumOfTreasures();
				TooManyTreasuresException.check();
				int colorID = generateTreasureColorID();
				Treasure treasure = new Treasure(colorID, point, real);
				OutOfBoardException.check(point);
				ElementOnWallException.check(point);
				Board.getInstance().addTreasure(treasure);
			}
		}
	}
	
	public static void generateArch(Document doc, XPath xpath) throws XPathExpressionException, OutOfBoardException, ElementOnWallException {
		NodeList archNodes = (NodeList) xpath.evaluate("./configuration/boardElement[@type='arch']", doc, XPathConstants.NODESET);
		Arch arch = new Arch();		
		for(int i = 0; i < archNodes.getLength(); i++) {
			NodeList archElements = (NodeList) xpath.evaluate("./element", archNodes.item(i), XPathConstants.NODESET);
			for(int j = 0; j < archElements.getLength(); j++) {
				OnePointOnMap point = BoardGeneration.getPoint(archElements.item(j), xpath);
				OutOfBoardException.check(point);
				ElementOnWallException.check(point);
				arch.addArch(point);
			}
		}
		Board.getInstance().setArch(arch);
	}
	
	public static void generateMage(Document doc, XPath xpath) throws XPathExpressionException, OutOfBoardException, ElementOnWallException {
		NodeList mageNodes = (NodeList) xpath.evaluate("./configuration/boardElement[@type='mage']", doc, XPathConstants.NODESET);
		Mage mage = new Mage();
		for(int i = 0; i < mageNodes.getLength(); i++) {
			NodeList mageElements = (NodeList) xpath.evaluate("./element", mageNodes.item(i), XPathConstants.NODESET);
			for(int j = 0; j < mageElements.getLength(); j++) {
				OnePointOnMap point = BoardGeneration.getPoint(mageElements.item(j), xpath);
				OutOfBoardException.check(point);
				ElementOnWallException.check(point);
				mage.addMage(point);
			}
		}
		Board.getInstance().setMage(mage);
	}
	
	public static void generateExit(Document doc, XPath xpath) throws XPathExpressionException, NoExitException, OutOfBoardException, ElementOnWallException {
		NodeList exitNodes = (NodeList) xpath.evaluate("/configuration/boardElement[@type='exit']", doc, XPathConstants.NODESET);
		Exit exit = new Exit();
		for(int i = 0; i < exitNodes.getLength(); i++) {
			NodeList exitElements = (NodeList) xpath.evaluate("./element", exitNodes.item(i), XPathConstants.NODESET);
			for(int j = 0; j < exitElements.getLength(); j++) {
				OnePointOnMap point = BoardGeneration.getPoint(exitElements.item(j), xpath);
				OutOfBoardException.check(point);
				ElementOnWallException.check(point);
				exit.addExit(point);
			}
		}
		Board.getInstance().setExit(exit);
		NoExitException.check();
	}
	
	public static void generateBoard(Document doc, XPath xpath) throws XPathExpressionException, NoBoardSizeException, WrongBoardSizeException {
		NodeList board = (NodeList) xpath.evaluate("/configuration/boardElement[@type='boardSize']/element", doc,
				XPathConstants.NODE);
		if(board == null) {
			throw new NoBoardSizeException();
		}
		OnePointOnMap boardSize = BoardGeneration.getPoint(board, xpath);
		Board.getInstance().setSizeX(boardSize.getX());
		Board.getInstance().setSizeY(boardSize.getY());
		WrongBoardSizeException.check();
	}
	
	public static void generate() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, TooManyTreasuresException, RiverException, TooLessPlayersException, NoExitException, OutOfBoardException, ElementOnWallException, NoBoardSizeException, WrongBoardSizeException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder.parse("C:/Users/Roman/study/NetCracker/projects/maze_web/src/configuration.xml");
		
//		InputStream xml = BoardGeneration.class.getResourceAsStream("C:/Users/Roman/study/NetCracker/projects/maze_web/src/configuration.xml");
//		Document doc = builder.parse(xml);
		
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
				
		BoardGeneration.generateBoard(doc, xpath);
		BoardGeneration.generateWall(doc, xpath);
		BoardGeneration.generateRiver(doc, xpath);
		BoardGeneration.generatePlayers(doc, xpath);
		BoardGeneration.generateTreasures(doc, xpath);
		BoardGeneration.generateArch(doc, xpath);
		BoardGeneration.generateMage(doc, xpath);
		BoardGeneration.generateExit(doc, xpath);		
	}
}
