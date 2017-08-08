/**
 * 
 */
package websocket.server;

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

	@OnError
	public void error(Session session, Throwable error) {
		log.info("Erring websocket session " + (session == null ? "" : session.getId()) + ", " + error);
	}

	@OnOpen
	public void open(Session session, EndpointConfig config) {
		log.info("Opening websocket session " + (session == null ? "" : session.getId()));
		addSession(session);
		log.info("Websocket session " + (session == null ? "" : session.getId()) + " was added into map");
	}

	@OnMessage
	public void onMessage(Session session, String msg) {
		log.info("Received a notice that needs to be pushed immediately：" + msg);
		Collection<Session> sessions = getSessions().values();
		log.info(sessions.size() + " websocket session(s) need to be pushed");
		for (Session sess : sessions) {
			if (sess.isOpen()) {
				log.info("Pushing to websocket session " + sess.getId());
				sess.getAsyncRemote().sendText(msg);
			}
		}
	}

	@OnClose
	public void close(Session session, CloseReason reason) {
		log.info("Closing websocket session " + (session == null ? "" : session.getId()) + ", " + reason);
		delSession(session);
		log.info("Websocket session " + (session == null ? "" : session.getId()) + " was removed from map");
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
