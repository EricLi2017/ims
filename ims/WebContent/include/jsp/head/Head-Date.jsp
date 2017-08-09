<%--datepicker depends on jQuery--%>
<%--jquery ui for datepicker function--%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/include/js/jquery-ui-1.12.1/jquery-ui.js"></script>
<%--jquery ui for datepicker style--%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/include/js/jquery-ui-1.12.1/themes/redmond/jquery-ui.css">
<%--define two id to use datepicker--%>
<script type="text/javascript">
	$(document).ready(function() {
		/*set date*/
		$("#dateAfter").datepicker({
			dateFormat : "yy-mm-dd"
		});
		$("#dateBefore").datepicker({
			dateFormat : "yy-mm-dd"
		});

	});//end of $(document).ready()
</script>