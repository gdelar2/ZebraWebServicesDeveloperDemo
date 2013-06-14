<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions'%>

<html>
<head>
<script type="text/javascript" src="resources/scripts/jquery-1.8.0.js"></script>
</head>
<body>
	<%
		response.setIntHeader("Refresh", 5);
	%>
	<table id="connectedPrintersTable" border="1" >
		<thead>
			<tr>
				<th>Name (${fn:length(connectedPrinters)} printers connected)</th>
				<th>IP Address</th>
				<th>Firmware Ver</th>
				<th>DNS Name</th>
				<th>Serial Number</th>
				<th>Status</th>
				<th>Quick Commands</th>
				<th> ZPL </th>
				<th> Print Image </th>
			</tr>
		</thead>

		<c:forEach items="${connectedPrinters}" var="printer" varStatus="status">
			<tr class="${status.index % 2 == 0 ? '' : 'alt'}">
				<td>
					<table>
						<tr>
							<td>${printer.productName}</td>
							
						</tr>
					</table>
				</td>
				<td>${printer.address}</td>
				<td>${printer.firmwareVer}</td>
				<td>${printer.dnsName}</td>
				<td>${printer.serialNumber}</td>
				<td>${printer.status}</td>
				
				<td>
				
				<%---Print Config label --%>
								<form id="printConfigForm${printer.serialNumber}" action="/ZebraWebServicesDeveloperDemo/WebServicesDevDemo/PrintConfig">
									<input type="hidden" name="SerialNumber" value="${printer.serialNumber}">
									<input type="submit" value="Print Config" />
								</form>
								
								<script type="text/javascript">                                         
								 $("#printConfigForm${printer.serialNumber}").submit(function() {
										event.preventDefault();
								
										var $form = $(this);
										term = $form.find( 'input[name="SerialNumber"]' ).val(),
								     	url = $form.attr( 'action' );
										
										$.post(url, { SerialNumber : term });
									});                                 
								</script> 
								
								
								
							
				<%---calibrate media button --%>
					<form id="calibrate${printer.serialNumber}" action="/ZebraWebServicesDeveloperDemo/WebServicesDevDemo/CalibrateMedia">
									<input type="hidden" name="SerialNumber" value="${printer.serialNumber}">
									<input type="submit" value="Calibrate Media" />
								</form>
								<script type="text/javascript">                                         
								 $("#calibrate${printer.serialNumber}").submit(function() {
										event.preventDefault();
								
										var $form = $(this);
										term = $form.find( 'input[name="SerialNumber"]' ).val(),
								     	url = $form.attr( 'action' );
										
										$.post(url, { SerialNumber : term });
									});                                 
								</script> 
								
								
								
								
			<%---restart printer button --%>
								<form id="restart${printer.serialNumber}" action="/ZebraWebServicesDeveloperDemo/WebServicesDevDemo/Restart">
									<input type="hidden" name="SerialNumber" value="${printer.serialNumber}">
									<input type="submit" value="Restart" />
								</form>
								<script type="text/javascript">                                         
								 $("#restart${printer.serialNumber}").submit(function() {
										event.preventDefault();
								
										var $form = $(this);
										term = $form.find( 'input[name="SerialNumber"]' ).val(),
								     	url = $form.attr( 'action' );
										
										$.post(url, { SerialNumber : term });
									});                                 
								</script> 
								
				</td>
				
				<%---ZPL column --%>
				<td>
				
				
					<form id="zplCommands${printer.serialNumber}" action="/ZebraWebServicesDeveloperDemo/WebServicesDevDemo/ZPLCommands">
									<input type="hidden" name="SerialNumber" value="${printer.serialNumber}">
									<input type="text" name="inCommand">
									<input type="submit" value="Send to Printer" />
								</form>
								<script type="text/javascript">                                         
								 $("#zplCommands${printer.serialNumber})").submit(function() {
										event.preventDefault();
								
										var $form = $(this);
										term = $form.find( 'input[name="SerialNumber"]' ).val(),
								     	url = $form.attr( 'action' );
										
										$.post(url, { SerialNumber : term });
									});                                 
								</script> 
				
				</td>
				
				<td><form action="upload" method="post" enctype="multipart/form-data">
					<input type="hidden" name="SerialNumber" value="${printer.serialNumber}">
    				<input type="file" name="file" accept="image/*">
    				<input type="submit" value="Print Image">
				</form>
				
				<%---- 
					<form id="imagePrint${printer.serialNumber}" action="upload" enctype="multipart/form-data" method="post">
									<input type="hidden" name="SerialNumber" value="${printer.serialNumber}">
									<input type="text" name="description" />
    								<input type="file" name="file" />
									<input type="submit" value="print" />
								</form>
								<script type="text/javascript">                                         
								 $("#imagePrint${printer.serialNumber})").submit(function() {
										event.preventDefault();
								
										var $form = $(this);
										term = $form.find( 'input[name="SerialNumber"]' ).val(),
								     	url = $form.attr( 'action' );
										
										$.post(url, { SerialNumber : term });
									});                                 
								</script> 
				----%>
				</td>
			</tr>

		</c:forEach>

		<tfoot>
			<tr>
				<td colspan="9"><div id="no-paging" align="right">&#169;
						ZIH Corp. All Rights Reserved.</div>
			</tr>
		</tfoot>


		<tbody id="connectedPrintersTableBody">
		</tbody>
	</table>
</body>
</html>