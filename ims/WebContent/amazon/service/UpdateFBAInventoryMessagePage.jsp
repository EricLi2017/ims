<%--
    View Message at intervals

    Note:
    1.This page should be opened by other pages;
    2.Opener page should be sure to close this page in correct time;

    Self closing interval:
    1.Click the Close button on the page manually to clear interval.
    2.Page auto clear interval if the opener is null.

  Created by IntelliJ IDEA.
  User: Eric Li
  Date: 3/1/2017
  Time: 2:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/include/PermissionCheck.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%--jquery lib--%>
<script type="text/javascript"
	src="../../include/js/jquery/jquery-3.1.1.min.js"></script>
<%--special only for this page--%>
<script type="text/javascript">
        $(document).ready(function () {
            /*  partial update at intervals    */
            var interval = setInterval(function () {//set a interval load
                $("#messageDiv").load("UpdateFBAInventoryMessageService.jsp");
            }, 1000);

            //Clear the interval when click the closeBTN button
            $("#closeBTN").click(function () {
                clearInterval(interval);//clear the interval by manual
                window.opener = null;
                window.open('', '_self');
                window.close();//only available when this was opened by javascript
            });

            //Clear the interval automatically only when this page was not opened by parent.
            //Note: The parent page who open this page should be sure to close this page at the right time.
            if (window.opener == null) {
                clearInterval(interval);
                $("#messageDiv").remove();
            }
        });//end of $(document).ready()
    </script>
<title></title>
</head>
<body>
	<button id="closeBTN">Close</button>
	<div id="messageDiv"></div>

</body>
</html>