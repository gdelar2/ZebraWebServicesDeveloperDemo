package com.zebra.devdemo.webservices;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.remote.comm.RemoteConnection;

/**
 * Servlet implementation class ZPLCommands
 */
@SuppressWarnings("serial")
@WebServlet("/WebServicesDevDemo/ZPLCommands")
public class ZPLCommands extends HttpServlet {
	
	/**
	 * Custom POST implementation which takes in a SerialNumber and sends ZPL commands via a remote connection.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// Get the serial number off the request.
			String serialNumber = request.getParameter("SerialNumber");
			// Create a RemoteConnection on port 11995
			RemoteConnection connection = new RemoteConnection(serialNumber, 11995);
			// Open the connection
			connection.open();
			// Get the ZPL commands from the inCommand parameter
			String zplCommands = request.getParameter("inCommand");
			// Send the ZPL commands
			connection.write(zplCommands.getBytes());
			
			//getServletContext().getRequestDispatcher("/WEB-INF/devdemo.jsp").forward(request, response);
			// Redirect the webpage back to the app's home
			response.sendRedirect("/ZebraWebServicesDeveloperDemo/WebServicesDevDemo");
			
		} catch (ConnectionException e) {
			System.err.println(e.getLocalizedMessage());
		} 
	}
}
