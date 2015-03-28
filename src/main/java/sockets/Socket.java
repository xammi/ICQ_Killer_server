package sockets;

import Crypto.Asimmetric.AsimCrypto;
import Crypto.CryptoFactory;
import Crypto.NoSuchCryptoRealisationException;
import Crypto.Simmetric.SimCrypto;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by max 10.03.15
 */
@WebSocket
public class Socket implements Abonent {
    private static final Logger logger = LoggerManager.getFor("Socket");
    private AsimCrypto asimKey;
    private SimCrypto simKey;

    private String nickname;
    private Client client;
    private Session session;

    protected final String address = AddressService.getSocketAddress();
    private MessageSystem msys;

    public Socket(String nickname, Client client) {
        this.msys = MessageSystem.getInstance();
        this.msys.register(this);
        this.nickname = nickname;
        this.client = client;

        try {
            this.asimKey = CryptoFactory.getAsimInstance("RSA");
            this.simKey = CryptoFactory.getSimInstance("DES");
        }
        catch (NoSuchCryptoRealisationException e) {
            logger.log(e.toString());
            System.out.println(e.toString());
        }
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        JSONObject json = new JSONObject(msg);

        //TODO: decrypt here

        try {
            String action = json.getString("action");
            JSONObject data = json.getJSONObject("data");

            if (action.equals("handshake")) {
                String user = data.getString("nickname");
                registerNickname(user);
            } else if (action.equals("message")) {
                String whom = data.getString("whom");
                String message = data.getString("message");

                msys.sendMessage(new SendQuery(nickname, whom, message));
            }
        }
        catch (JSONException e) {
            logger.log("onMessage " + e.toString());
            System.out.println("Socket.onMessage" + e.toString());
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
            //TODO: encrypt here

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
