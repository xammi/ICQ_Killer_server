package sockets;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by eugene on 10/19/14.
 */
public class SocketCreator implements WebSocketCreator {

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        HttpServletRequest request = req.getHttpServletRequest();

        if (request != null) {
            HttpSession session = request.getSession();

            if (session != null) {
                String user = session.getAttribute("nickname").toString();

                if (user != null) {
                    Socket socket = new Socket(user);
                    socket.registerNickname(user);
                    return socket;
                }
            }
        }
        return new Socket(null);
    }
}
