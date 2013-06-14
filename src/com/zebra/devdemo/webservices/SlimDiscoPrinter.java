package com.zebra.devdemo.webservices;

import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.discovery.DiscoveredPrinter;
import com.zebra.sdk.remote.comm.RemoteConnection;

public class SlimDiscoPrinter {
	public String address;
	public String dnsName;
	public String firmwareVer;
	public String productName;
	public String serialNumber;
	public String status;

	public SlimDiscoPrinter(DiscoveredPrinter printer) throws ConnectionException, ZebraPrinterLanguageUnknownException {
		this.setAddress(printer.getDiscoveryDataMap().get("ADDRESS"));
		this.setProductName(printer.getDiscoveryDataMap().get("PRODUCT_NAME"));
		this.setDnsName(printer.getDiscoveryDataMap().get("DNS_NAME"));
		this.setFirmwareVer(printer.getDiscoveryDataMap().get("FIRMWARE_VER"));
		this.setSerialNumber(printer.getDiscoveryDataMap().get("SERIAL_NUMBER"));
		
		RemoteConnection connection = new RemoteConnection(serialNumber, 11995);
		try {
			connection.open();
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ZebraPrinter printerTest = ZebraPrinterFactory.getInstance(connection);
		PrinterStatus printerStatus = printerTest.getCurrentStatus();
		if (printerStatus.isReadyToPrint)
            this.setStatus("Ready to Print");
		else if (printerStatus.isPaused) 
			this.setStatus("Cannot Print because the printer is paused.");
        else if (printerStatus.isHeadOpen)
        	this.setStatus("Cannot Print because the printer head is open.");
        else if (printerStatus.isPaperOut)
        	this.setStatus("Cannot Print because the paper is out.");
        else 
        	this.setStatus("Cannot Print.");
        
	}

	public String getDnsName() {
		return this.dnsName;
	}

	public void setDnsName(String dnsName) {
		this.dnsName = dnsName;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getFirmwareVer() {
		return this.firmwareVer;
	}

	public void setFirmwareVer(String firmwareVer) {
		this.firmwareVer = firmwareVer;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}