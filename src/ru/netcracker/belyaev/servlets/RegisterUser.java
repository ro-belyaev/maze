package ru.netcracker.belyaev.servlets;

import java.io.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.netcracker.belyaev.database.api.DatabaseManagerFactory;

public class RegisterUser extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		PrintWriter out = response.getWriter();
		
		if(name != null && password != null) {
			DatabaseManagerFactory.getDatabaseManagerInstance().registerUser(name, password);
		}
		out.close();
	}

}
