package ru.netcracker.belyaev.routing;

import java.util.Arrays;
import java.util.Scanner;

//import org.apache.log4j.Logger;

import ru.netcracker.belyaev.controller.Controller;
import ru.netcracker.belyaev.enums.Colors;
import ru.netcracker.belyaev.enums.Direction;
import ru.netcracker.belyaev.model.models.MainModel;
import ru.netcracker.belyaev.view.ConsoleView;
import ru.netcracker.belyaev.view.View;

public class Routing {
//	private static Logger log = Logger.getLogger(Routing.class.getName());
	private Controller controller;
	
	private MainModel model;
	private View view;
	public Routing() {
		model = new MainModel();
		view = new ConsoleView();
		controller = new Controller(model, view);
		model.setController(controller);
		view.setController(controller);		
		this.setController(controller);
	}
	
	public void setController(Controller controller) {
		this.controller = controller;
	}
	public void setGameId(String gameId) {
		this.controller.setGameId(gameId);
	}
	public boolean checkArgumentsNumber(String[] args, int shoudBeNum) {
		if(args.length != shoudBeNum) {
			System.out.println("Wrong amount of arguments! Should be " + (shoudBeNum - 1));
			return false;
		}
		return true;
	}
	public void go(String uid, String dir) {
		Direction direction = Direction.recognizeDirection(dir);
		if(direction == null) {
			System.out.println("wrong second parameter");
		}
		else {
			try {
				int id = Integer.parseInt(uid);
				controller.go(id, direction);
			} catch(NumberFormatException e) {
				System.out.println("Argument " + uid + " must be an integer!");
			}
		}
	}
	public void takeTreasure(String uid, String treasureColor) {
		try {
			int userID = Integer.parseInt(uid);
			int treasureColorID = Colors.recognizeColorID(treasureColor);
			if(treasureColorID == -1) {
				System.out.println(treasureColor + " is wrong colour");
				System.out.println("Should be one of: " + Arrays.toString(Colors.values()));
			}
			else {
				controller.upTreasure(userID, treasureColorID);
			}
		} catch(NumberFormatException e) {
			System.out.println("Arguments " + uid + " must be an integer!");
		}
	}
	public void dropTreasure(String uid) {
		try {
			int userID = Integer.parseInt(uid);
			controller.dropTreasure(userID);
		} catch(NumberFormatException e) {
			System.out.println("Argument " + uid + " must be an integer!");
		}
	}
	public void askPrediction(String uid) {
		try {
			int userID = Integer.parseInt(uid);
			controller.askPrediction(userID);
		} catch(NumberFormatException e) {
			System.out.println("Argument " + uid + " must be an integer!");
		}
	}
	public void shoot(String uid, String dir) {
		Direction direction = Direction.recognizeDirection(dir);
		if(direction == null) {
			System.out.println("wrong second parameter");
		}
		else {
			try {
				int id = Integer.parseInt(uid);
				controller.shoot(id, direction);
			} catch(NumberFormatException e) {
				System.out.println("Argument " + uid + " must be an integer!");
			}
		}
	}
	public void exit(String uid) {
		try {
			int userID = Integer.parseInt(uid);
			controller.exit(userID);
		} catch(NumberFormatException e) {
			System.out.println("Argument " + uid + " must be an integer!");
		}
	}
	public void drawBoard() {
		controller.getBoardSnapshot();
	}
	public String generate(String gameXml) {
		String gameId = controller.generate(gameXml);
		return gameId;
	}
	public void terminate() {
		controller.terminate();
	}
	
	public void route(String gameId) {
		Scanner input = new Scanner(System.in);
		while(true) {
			String[] args = input.nextLine().trim().split("\\s+");
			setGameId(gameId);
			if(args.length == 0) {
				continue;
			}
			else if("go".equals(args[0])) {
				if(checkArgumentsNumber(args, 3)) {
					go(args[1], args[2]);
//					log.info("go " + args[2]);
				}
			}
			else if("take_treasure".equals(args[0])) {
				if(checkArgumentsNumber(args, 3)) {
					takeTreasure(args[1], args[2]);
//					log.info("take treasure " + args[2]);
				}
			}
			else if("drop_treasure".equals(args[0])) {
				if(checkArgumentsNumber(args, 2)) {
					dropTreasure(args[1]);
//					log.info("drop treasure");
				}
			}
			else if("predict".equals(args[0])) {
				if(checkArgumentsNumber(args, 2)){
					askPrediction(args[1]);
//					log.info("predict");
				}
			}
			else if("shoot".equals(args[0])) {
				if(checkArgumentsNumber(args, 3)) {
					shoot(args[1], args[2]);
//					log.info("shoot " + args[2]);
				}
			}
			else if("draw".equals(args[0])) {
				if(checkArgumentsNumber(args, 1)) {
					drawBoard();
//					log.info("draw");
				}
			}
			else if("exit".equals(args[0])) {
				if(checkArgumentsNumber(args, 2)) {
					exit(args[1]);
//					log.info("exit");
				}
			}
			else if("terminate".equals(args[0])) {
				if(checkArgumentsNumber(args, 1)) {
					terminate();
//					log.info("stop");
				}
			}
			else {
				System.out.println("wrong command");
			}
		}
	}
	
	public static void main(String[] argv) {
		Routing routing = new Routing();
//		String gameId = routing.generate();
//		routing.route(gameId);
	}
}
