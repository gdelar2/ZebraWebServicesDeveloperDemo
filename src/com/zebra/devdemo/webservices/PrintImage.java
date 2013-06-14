package com.zebra.devdemo.webservices;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.discovery.DiscoveredPrinter;
import com.zebra.sdk.remote.comm.RemoteConnection;

import javax.servlet.annotation.WebServlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.graphics.ZebraImageFactory;
import com.zebra.sdk.graphics.ZebraImageI;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.discovery.DiscoveredPrinter;
import com.zebra.sdk.remote.discovery.RemoteDiscoverer;

public class PrintImage extends HttpServlet {

	/*protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//Set Response Type
		response.setContentType("text/html;charset=UTF-8");

		//Use out to send content to the user's browser
		PrintWriter out = response.getWriter();
		try {
			//Get the name of the file from the URL string
			String name = (String)request.getParameter("filename");
			if (name == null || name.length() < 3) {
				out.println("Failure: No filename given");
			} 
			else {

				//Create an input stream from our request.
				//This input stream contains the image itself.
				DataInputStream din = new DataInputStream(request.getInputStream());
				byte[] data = new byte[0];
				byte[] buffer = new byte[512];
				int bytesRead;
				while ((bytesRead = din.read(buffer)) > 0) {
					// construct an array large enough to hold the data we currently have
					byte[] newData = new byte[data.length + bytesRead];
					// copy data that was previously read into newData
					System.arraycopy(data, 0, newData, 0, data.length);
					// append new data from buffer into newData
					System.arraycopy(buffer, 0, newData, data.length, bytesRead);
					// set data equal to newData in prep for next block of data
					data = newData;
				}

				//define the path to save the file using the file name from the URL.
				String path = "C:/" + name + ".png";

				InputStream in = new ByteArrayInputStream(data);
				BufferedImage bImageFromConvert = ImageIO.read(in);

				ImageIO.write(bImageFromConvert, "png", new File(path));
				out.println("Success");

				//Get the serial number off the request.
				String serialNumber = request.getParameter("SerialNumber");
				//Create a RemoteConnection on port 11995
				RemoteConnection connection = new RemoteConnection(serialNumber, 11995);
				connection.open();
				ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
				printer.printImage(path, 0, 0);
				connection.close();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			out.println("Failure");
		} 
		finally {
			out.close();
		}
	}*/

	/**
	 * Handles the HTTP GET method.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	//protected void doGet(HttpServletRequest request, HttpServletResponse response)
			//throws ServletException, IOException {
		//processRequest(request, response);
	//}

	/**
	 * Handles the HTTP POST method.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	//protected void doPost(HttpServletRequest request, HttpServletResponse response)
			//throws ServletException, IOException {
		//processRequest(request, response);
	//}

	/**
	 * Returns a short description of the servlet.
	 * @return a String containing servlet description
	 */
	//public String getServletInfo() {
		//return "PNG Image upload servlet";
	//}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<FileItem> items = null;
        File file = null;
        byte[] imageBytes = null;
        BufferedImage bufferedImage = null;
        String serialNumber = null;
        try {
            items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
        } catch (FileUploadException e) {
            throw new ServletException("Cannot parse multipart request.", e);
        }
        for (FileItem item : items) {
            if (item.isFormField()) {
                // Process regular form fields here the same way as request.getParameter().
                // You can get parameter name by item.getFieldName();
                // You can get parameter value by item.getString();
            	serialNumber = item.getString();
            } else {
                // Process uploaded fields here.
                String filename = FilenameUtils.getName(item.getName()); // Get filename.
                if (!filename.endsWith(".jpg") && !filename.endsWith(".jpeg") && !filename.endsWith(".bmp") && !filename.endsWith(".png") 
                		&& !filename.endsWith(".gif")) {
                	System.out.println("Invalid image extension.");
                	return;
                }
                //file = new File("D:/", filename); // Define destination file.
                imageBytes = item.get();
                InputStream in = new ByteArrayInputStream(imageBytes);
                bufferedImage = ImageIO.read(in);
                try {
					//item.write(file);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // Write to destination file.
            }
        }

        //String serialNumber = "XXQLJ123100255";//request.getParameter("SerialNumber");
		//Create a RemoteConnection on port 11995
		RemoteConnection connection = new RemoteConnection(serialNumber, 11995);
		try {
			connection.open();
			ZebraImageI image = ZebraImageFactory.getImage(bufferedImage);//file.getAbsolutePath());
			ZebraPrinterFactory.getInstance(connection).printImage(image, 0, 0, 550, 412, false);
			connection.close();
		} catch (ConnectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ZebraPrinterLanguageUnknownException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Show result page.
        response.sendRedirect("/ZebraWebServicesDeveloperDemo/WebServicesDevDemo");
		//getServletContext().getRequestDispatcher("/WEB-INF/devdemo.jsp").forward(request, response);
    }
	
}
