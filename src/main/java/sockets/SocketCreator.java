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

        Client client = Client.DESKTOP;
        HttpServletRequest request = req.getHttpServletRequest();

        if (request != null) {
            String clientName = request.getParameter("client");

            if (clientName == null) {
                clientName = "DESKTOP";
            }

            client = Client.valueOf(clientName);
            HttpSession session = request.getSession();

            if (session != null) {
                Object user = session.getAttribute("nickname");

                if (user != null) {
                    Socket socket = new Socket(user.toString(), client);
                    socket.registerNickname(user.toString());
                    return socket;
                }
            }

            String nickname = request.getParameter("nickname");

            if (nickname != null) {
                Socket socket = new Socket(nickname, client);
                socket.registerNickname(nickname);
                return socket;
            }
        }
        return new Socket(null, client);
    }
}
