<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/PermissionCheck.jsp"%>
<%@ page import="com.jspsmart.upload.SmartUpload"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.io.File"%>
<%@ page import="java.util.Date"%>

<%--执行完后生成的数据--%>
<%
	String realFullFileName = null;
%>
<%
	String IMGTYPE = "xlsx";
	long IMGMAXSIZE = 1 * 1024 * 1024;

	int count = 0;//上传文件数
	try {
		SmartUpload su = new SmartUpload();// 新建一个SmartUpload对象
		su.initialize(pageContext);// 上传初始化
		su.setAllowedFilesList(IMGTYPE);//3.设定允许上传的文件（通过扩展名限制）
		su.setMaxFileSize(IMGMAXSIZE);// 1.限制每个上传文件的最大长度。
		su.upload();// 上传文件

		// 逐一提取上传文件信息
		com.jspsmart.upload.File file = su.getFiles().getFile(0);

		// 将上传文件全部保存到指定目录
		String realPath = application.getRealPath("/");
		String realDir = realPath + "WEB-INF\\temp";
		String realFileName = "InventoryReport_" + new SimpleDateFormat("yyyyMMddHHmmssFFF").format(new Date())
				+ "." + file.getFileExt();
		realFullFileName = realDir + "\\" + realFileName;
		File f = new java.io.File(realDir);
		if (!f.exists()) {
			f.mkdirs();
			System.out.println("Create Dir:" + realDir);
		}
		file.saveAs(realFullFileName);
		System.out.println("realFullFileName = " + realFullFileName);

		count++;
	} catch (Exception e) {
		e.printStackTrace();
	}

	//###############################################//
	// redirect
	//###############################################//
	if (count > 0) {
		request.setAttribute("fileName", realFullFileName);
		request.getRequestDispatcher("BatchAddProductFromAMZ_Excute.jsp").forward(request, response);
		return;
	} else {
		out.print("<script>alert('参数错误!');history.go(-1);</script>");
	}
%>
