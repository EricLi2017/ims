package common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * IP tool Created by Eric Li on 3/2/2017.
 */
public class IP {

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		System.out.println("x-forwarded-for:" + ip);// TODO for debug
		if (ip == null || ip.trim().isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			System.out.println("Proxy-Client-IP:" + ip);// TODO for debug
		}
		if (ip == null || ip.trim().isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			System.out.println("WL-Proxy-Client-IP:" + ip);// TODO for debug
		}
		if (ip == null || ip.trim().isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			System.out.println("remoteAddr:" + ip);// TODO for debug
		}
		return ip;
	}

}
