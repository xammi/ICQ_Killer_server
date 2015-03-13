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
                Object user = session.getAttribute("nickname");

                if (user != null) {
                    Socket socket = new Socket(user.toString());
                    socket.registerNickname(user.toString());
                    return socket;
                }
            }
        }
        return new Socket(null);
    }
}
