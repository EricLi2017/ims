<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/jsp/PermissionCheck.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Batch Add Product From AMZ</title>
<script type="text/javascript" language="javascript">
        function sub() {
            if (document.all['userfile'].value == "") {
                alert('Please select file');
                uploadForm.userfile.focus();
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
	<h1 align="center">Upload SKU From Amazon Inventory Report</h1>
	<table width="100%" border="1">
		<tr>
			<td>
				<table align="center">
					<tr align="center">
						<th>How to get the file to be uploaded:</th>
					</tr>
					<tr align="center">
						<td>1. Download the Inventory Report from
							amazon.(Inventory->Inventory Reports-> Inventory Report. It is a
							txt file)</td>
					</tr>
					<tr align="center">
						<td>2. Create a new excel (version 2007 /.xlsx)</td>
					</tr>
					<tr align="center">
						<td>3. Copy all the report content to the excel and save it.</td>
					</tr>
					<tr align="center">
						<th>The content of the report:</th>
					</tr>
					<tr align="center">
						<td>sku asin price quantity Business Price Quantity Price
							Type Quantity Lower Bound 1 Quantity Price 1 Quantity Lower Bound
							2 Quantity Price 2 Quantity Lower Bound 3 Quantity Price 3
							Quantity Lower Bound 4 Quantity Price 4 Quantity Lower Bound 5
							Quantity Price 5</td>
					</tr>
					<tr align="center">
						<td>ps: The value of quantity are all 0 or null</td>
					</tr>
					<tr align="center">
						<th>The main content of the report:</th>
					</tr>
					<tr align="center">
						<td>sku asin price</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

	<p></p>

	<!--上传-->
	<FORM ENCTYPE='multipart/form-data' method='POST'
		action="BatchAddProductFromAMZ_Upload.jsp" name="uploadForm">
		<table align="center">
			<tr>
				<td>File:</td>
				<td><input TYPE='file' name="userfile"></td>
				<td><input type="submit" onclick="return sub()" value="Upload"></td>
				<td><input type="reset" value="Reset"></td>
			</tr>

		</table>
	</form>

</body>
</html>