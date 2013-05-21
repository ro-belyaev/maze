package ru.netcracker.belyaev.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GenerateGame extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void goGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String gameXml = request.getParameter("gameXml");
	}
}
