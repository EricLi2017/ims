/**
 * 
 */
package websocket;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Eclipse on Aug 7, 2017 at 6:22:38 PM.
 *
 * @author Eric Li
 * @version 1.0
 * @since 1.0
 */
@ClientEndpoint
public class PushMessageClientEndpoint {
	private static final Log LOG = LogFactory.getLog(PushMessageClientEndpoint.class);
	private static final String SERVER_END_POINT_SCHEME = "ws://";
	private static final String SERVER_END_POINT_SERVLET_PATH = "/notifyall";

	@OnError
	public void error(Session session, Throwable error) {
		// NO-OP
	}

	@OnOpen
	public void open(Session session, EndpointConfig config) {
		// NO-OP
	}

	@OnMessage
	public void onMessage(Session session, String msg) {
		// NO-OP
	}

	@OnClose
	public void close(Session session, CloseReason reason) {
		// NO-OP
	}

	public static final void pushMessage(String url, String message) {
		LOG.info("url=" + url);
		LOG.info("message=" + message);
		if (url == null || url.trim().isEmpty() || message == null || message.trim().isEmpty())
			return;

		Session session = null;
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		try {
			session = container.connectToServer(PushMessageClientEndpoint.class, new URI(url));
		} catch (DeploymentException | IOException | URISyntaxException e) {
			LOG.error("Connection error occured!");
			e.printStackTrace();
		}
		if (session != null && session.isOpen()) {
			// nonblocking method
			session.getAsyncRemote().sendText(message);
			try {
				session.close();
			} catch (IOException e) {
				// NO-OP
			}
		}
	}

	public static final void pushMessage(HttpServletRequest request) {
		if (request == null)
			return;
		String url = SERVER_END_POINT_SCHEME + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + SERVER_END_POINT_SERVLET_PATH;
		String message = (String) request.getAttribute("message");

		pushMessage(url, message);
	}
}
