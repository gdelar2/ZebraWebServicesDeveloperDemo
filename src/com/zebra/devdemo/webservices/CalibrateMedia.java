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
 * Servlet implementation class PrintConfig
 */
@SuppressWarnings("serial")
@WebServlet("/WebServicesDevDemo/CalibrateMedia")
public class CalibrateMedia extends HttpServlet {

	/**
	 * Custom POST implementation which takes in a SerialNumber and calibrates the media by sending the ~JC command over
	 * a remote connection.
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
			// Send the calibrate command, ~JC
			connection.write("~JC".getBytes());

		} catch (ConnectionException e) {
			System.err.println(e.getLocalizedMessage());
		} 
	}
}
