package sockets;

import loggers.Logger;
import managers.LoggerManager;
import msgsystem.Abonent;
import msgsystem.AddressService;
import msgsystem.MessageSystem;
import msgsystem.messages.LogoutQuery;
import msgsystem.messages.SendQuery;
import msgsystem.messages.SetSocket;
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
public class Socket implements Abonent {
    private static final Logger logger = LoggerManager.getFor("Socket");

    private String nickname;
    private Session session;

    protected final String address = AddressService.getSocketAddress();
    private MessageSystem msys;

    public Socket(String nickname) {
        this.msys = MessageSystem.getInstance();
        this.msys.register(this);
        this.nickname = nickname;
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        JSONObject json = new JSONObject(msg);

        String action = json.getString("action");
        JSONObject data = json.getJSONObject("data");

        if (action.equals("handshake")) {
            String user = data.getString("nickname");
            registerNickname(user);
        }
        else if (action.equals("message")) {
            String whom = data.getString("whom");
            String message = data.getString("message");

            msys.sendMessage(new SendQuery(nickname, whom, message));
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
        }
        catch (Exception e) {
            logger.log("sendToClient: " + e.toString());
            System.out.print("sendToClient: " + e.toString());
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        if (nickname != null) {
            msys.sendMessage(new LogoutQuery(getAddress(), nickname));
        }
    }

    public void registerNickname(String nickname) {
        this.nickname = nickname;
        msys.sendMessage(new SetSocket(getAddress(), nickname, this));
    }

    @Override
    public String getAddress() {
        return this.address;
    }
}
