/**
 * 
 */
package websocket.server;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Eclipse on Aug 7, 2017 at 12:03:55 PM.
 *
 * @author Eric Li
 * @version 1.0
 * @since 1.0
 */
@ServerEndpoint("/notifyall")
public class NotifyAllEndpoint {
	private static Map<String, Session> sessionMap = new HashMap<>();

	private static final Log log = LogFactory.getLog(NotifyAllEndpoint.class);

	@OnClose
	public void close(Session session, CloseReason reason) {
		log.info("Websocket session closing");

		delSession(session);
		log.info("Session has been removed from map");
	}

	@OnError
	public void error(Session session, Throwable error) {
		log.info("Websocket session error");
		// error.printStackTrace();
	}

	@OnOpen
	public void open(Session session, EndpointConfig config) {
		log.info("Websocket session opening");
		addSession(session);
		log.info("Session has been added to map");

		if (session.isOpen()) {
			session.getAsyncRemote().sendText("Session opened");
		} else {
			log.info("Websocket session is closed in open()");
		}
	}

	@OnMessage
	public void onMessage(Session session, String msg) {
		log.info("Websocket session received message " + msg);
		Collection<Session> sessions = getSessions().values();
		log.info("sessions.size()=" + sessions.size());
		try {
			for (Session sess : sessions) {
				if (sess.isOpen()) {
					log.info("start to send message " + msg);
					sess.getBasicRemote().sendText(msg);

				}
			}
		} catch (IOException e) {
		}
	}

	private static synchronized void addSession(Session session) {
		if (session != null)
			sessionMap.put(session.getId(), session);
	}

	private static synchronized void delSession(Session session) {
		if (session != null && sessionMap.containsKey(session.getId()))
			sessionMap.remove(session.getId());
	}

	private static synchronized Map<String, Session> getSessions() {
		return sessionMap;
	}

}
