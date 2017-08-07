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
	private static final Log log = LogFactory.getLog(PushMessageClientEndpoint.class);

	@OnClose
	public void close(Session session, CloseReason reason) {
		log.info("Websocket session closing");
	}

	@OnError
	public void error(Session session, Throwable error) {
		log.info("Websocket session error");
		// error.printStackTrace();
	}

	@OnOpen
	public void open(Session session, EndpointConfig config) {
		log.info("Websocket session opening");

		if (session.isOpen()) {

		} else {
			log.info("Websocket session is closed in open()");
		}
	}

	@OnMessage
	public void onMessage(Session session, String msg) {
		log.info("Websocket session received message " + msg);
		if (session.isOpen()) {
			// blocking method
			// session.getBasicRemote().sendText(msg);
			// nonblocking method
			// session.getAsyncRemote().sendText(msg);
		} else {
			log.info("Websocket session is closed in onMessage()");
		}
	}

	public static void pushMessage(String message) {
		if (message == null || message.trim().isEmpty())
			return;

		Session session = null;
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		try {
			session = container.connectToServer(PushMessageClientEndpoint.class,
					new URI("ws://127.0.0.1:8088/ims/notifyall"));// TODO
		} catch (DeploymentException | IOException | URISyntaxException e) {
			log.error("Connection error occured!");
			e.printStackTrace();
		}

		if (session != null && session.isOpen()) {
			// nonblocking method
			session.getAsyncRemote().sendText(message);

			try {
				session.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		Session session = null;
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		try {
			session = container.connectToServer(PushMessageClientEndpoint.class,
					new URI("ws://127.0.0.1:8088/ims/echoall"));
		} catch (DeploymentException | IOException | URISyntaxException e) {
			log.error("Connection error occured!");
			e.printStackTrace();
		}

		String msg = "The system will be shut down in 5 minutes!";
		if (session != null && session.isOpen()) {
			// nonblocking method
			session.getAsyncRemote().sendText(msg);

			// Thread.sleep(1000);
		}

	}
}
