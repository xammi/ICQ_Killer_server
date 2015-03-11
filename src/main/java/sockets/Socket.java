package sockets;

import msgsystem.MessageSystem;
import msgsystem.messages.SendQuery;
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
    private String nickname;
    private Session session;
    private MessageSystem msys;

    public Socket(String nickname) {
        this.msys = MessageSystem.getInstance();
        this.nickname = nickname;
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        JSONObject json = new JSONObject(msg);

        String action = json.getString("action");
        JSONObject data = json.getJSONObject("data");

        if (action.equals("message")) {
            String whom = data.getString("whom");
            String message = data.getString("message");

            msys.sendMessage(new SendQuery(this.nickname, whom, message));
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        this.session = session;
    }

    public void sendToClient(String action, Map<String, Object> data) {
        JSONObject json = new JSONObject();
        json.put("action", action);
        json.put("data", data);

        try {
            session.getRemote().sendString(json.toString());
        } catch (Exception e) {
            System.out.print("sendToClient: " + e.toString());
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {

    }
}
