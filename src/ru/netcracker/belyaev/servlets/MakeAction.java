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

		Routing routing = new Routing();
		routing.setGameId(gameId);
		
		 if(action.equals("generate")) {
			 	String gameXml = request.getParameter("gameXml");
			 	if(gameXml != null) {
			 		newGameId = routing.generate(gameXml);
			 	}
		} else {
			routing.setGameId(gameId);
			if(action.equals("go")) {
				routing.go(uid, direction);
			} else if(action.equals("shoot")) {
				routing.shoot(uid, direction);
			} else if(action.equals("pick-up")) {
				routing.takeTreasure(uid, treasureColor);
			} else if(action.equals("drop")) {
				routing.dropTreasure(uid);
			} else if(action.equals("predict")) {
				routing.askPrediction(uid);
			} else if(action.equals("exit")) {
				routing.exit(uid);
			} else if(action.equals("draw")) {
				routing.drawBoard();
			} else if(action.equals("terminate")) {
				routing.terminate();
			} else {
				System.out.println("!!! some wrong command from client !!!");
			}
		}
		 
		response.getWriter().close();
		 
	}

}
