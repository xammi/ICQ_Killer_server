package sockets;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by max 10.03.15
 */
@WebSocket
public class Socket {
    private Session session;

    @OnWebSocketMessage
    public void onMessage(String message) {
        JSONObject json = new JSONObject(message);
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        this.session = session;
    }

    public void sendToClient(String action, Map<String, Object> data) {
        JSONObject json = new JSONObject();
        json.put("action", action);
        json.put("status", "OK");
        json.put("data", data);

        try {
            session.getRemote().sendString(json.toString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {

    }
}
