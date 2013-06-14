package com.zebra.devdemo.webservices;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.remote.comm.RemoteConnection;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import com.zebra.sdk.graphics.ZebraImageFactory;
import com.zebra.sdk.graphics.ZebraImageI;

@SuppressWarnings("serial")
public class PrintImage extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<FileItem> items = null;
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
                imageBytes = item.get();
                InputStream in = new ByteArrayInputStream(imageBytes);
                bufferedImage = ImageIO.read(in);
            }
        }

		// Create a RemoteConnection on port 11995
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
