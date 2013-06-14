package com.zebra.devdemo.webservices;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.remote.comm.RemoteConnection;

/**
 * Servlet implementation class PrintConfig
 */
@SuppressWarnings("serial")
public class GetPrinterStatus extends HttpServlet {

	/**
	 * Custom POST implementation which takes in a SerialNumber as the parameter.
	 * A <code>ZebraPrinter</code> object is created and used to get the printer's status.
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
			// Create printer instance
			ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
			// Get printer's status
			PrinterStatus printerStatus = printer.getCurrentStatus();
			
			if (printerStatus.isReadyToPrint) {
                request.setAttribute("printStatus", "Ready To Print");
            } else if (printerStatus.isPaused) {
            	request.setAttribute("printStatus", "Cannot Print because the printer is paused.");
            } else if (printerStatus.isHeadOpen) {
            	request.setAttribute("printStatus", "Cannot Print because the printer head is open.");
            } else if (printerStatus.isPaperOut) {
            	request.setAttribute("printStatus", "Cannot Print because the paper is out.");
            } else {
            	request.setAttribute("printStatus", "Cannot Print.");
            }
			
			getServletContext().getRequestDispatcher("/WEB-INF/devdemo.jsp").forward(request, response);
			
		} catch (ConnectionException e) {
			System.err.println(e.getLocalizedMessage());
		} catch (ZebraPrinterLanguageUnknownException e) {
			System.err.println(e.getLocalizedMessage());
		}
	}
}
