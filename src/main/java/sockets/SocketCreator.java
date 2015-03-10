package sockets;

import msgsystem.MessageSystem;
import msgsystem.messages.SetSocket;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import javax.servlet.http.HttpSession;

/**
 * Created by eugene on 10/19/14.
 */
public class SocketCreator implements WebSocketCreator {

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        HttpSession session = req.getHttpServletRequest().getSession();

        if (session != null) {
            String user = session.getAttribute("nickname").toString();

            if (user != null) {
                MessageSystem msys = MessageSystem.getInstance();
                Socket socket = new Socket();
                msys.sendMessage(new SetSocket(user, socket));
                return socket;
            }
        }
        return null;
    }
}
