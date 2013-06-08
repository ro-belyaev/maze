package ru.netcracker.belyaev.servlets;

import java.io.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.netcracker.belyaev.routing.Routing;

public class MakeAction extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String uid = request.getParameter("uid");
		String action = request.getParameter("action");
		String gameId = request.getParameter("gameId");
		String direction = null;
		String treasureColor = null;
		if(action.equals("go") || action.equals("shoot")) {
			direction = request.getParameter("direction");
		} else if(action.equals("pick-up")) {
			treasureColor = request.getParameter("color");
		}
		String newGameId = null;
		System.out.println(action);

		Routing routing = new Routing();
		routing.setGameId(gameId);
		
		if (action.equals("generate")) {
			String gameXml = request.getParameter("gameXml");
			if (gameXml != null) {
				newGameId = routing.generate(gameXml);
			}
		} else {
			routing.setGameId(gameId);
			if(action.equals("go")) {
				if (routing.go(uid, direction, (String) null)) {
					routing.saveGameState();
				}
			} else if(action.equals("shoot")) {
				if (routing.shoot(uid, direction, (String) null)) {
					routing.saveGameState();
				}
			} else if(action.equals("pick-up")) {
				if (routing.takeTreasure(uid, treasureColor, (String) null)) {
					routing.saveGameState();
				}
			} else if(action.equals("drop")) {
				if (routing.dropTreasure(uid, (String) null)) {
					routing.saveGameState();
				}
			} else if(action.equals("predict")) {
				if (routing.askPrediction(uid, (String) null)) {
					routing.saveGameState();
				}
			} else if(action.equals("exit")) {
				if (routing.exit(uid, (String) null)) {
					routing.saveGameState();
				}
			} else if(action.equals("draw")) {
				routing.drawBoard();
			} else if(action.equals("terminate")) {
				routing.terminate();
			} else {
				System.out.println("!!! some wrong command from client !!!");
			}
		}
		
		routing.dropGameState();
		 
		response.getWriter().close();
		 
	}

}
