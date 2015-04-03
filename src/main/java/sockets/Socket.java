package sockets;

import Crypto.Asimmetric.AsimCrypto;
import Crypto.CryptoFactory;
import Crypto.NoSuchCryptoRealisationException;
import Crypto.Simmetric.IncompatibleKeyException;
import Crypto.Simmetric.SimCrypto;
import confparser.Config;
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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by max 10.03.15
 */
@WebSocket
public class Socket implements Abonent {
    private static final Logger logger = LoggerManager.getFor("Socket");

    private static final Config config = Config.getInstance();
    private static boolean encrypt = config.getBoolean("encrypt", "enabled");
    private AsimCrypto asimCrypto;
    private SimCrypto simCrypto;

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
            this.asimCrypto = CryptoFactory.getAsimInstance(config.getString("encrypt", "asimmetric"));

            this.simCrypto = CryptoFactory.getSimInstance(config.getString("encrypt", "simmetric"));
            this.simCrypto.setWorkingDir(config.getString("encrypt", "working_dir"));
            this.simCrypto.setKey(simCrypto.generateKey());
        }
        catch (NoSuchCryptoRealisationException | IncompatibleKeyException e) {
            logger.log(e.toString());
            System.out.println(e.toString());
            encrypt = false;
        }

        if (client == Client.DESKTOP) {
            encrypt = false;
        }
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        JSONObject json;
        try {
            if (encrypt) {
                byte [] binaryData = msg.getBytes();
                byte [] decryptedData = simCrypto.decrypt(binaryData);
                String str = new String(decryptedData, "UTF-8");
                json = new JSONObject(str);
            }
            else {
                json = new JSONObject(msg);
            }

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
        catch (JSONException | UnsupportedEncodingException e) {
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
            if (encrypt) {
                byte [] binaryData = json.toString().getBytes();
                byte [] encryptedData = simCrypto.encrypt(binaryData);

                ByteBuffer buffer = ByteBuffer.wrap(encryptedData);
                session.getRemote().sendBytesByFuture(buffer);
            }
            else {
                session.getRemote().sendStringByFuture(json.toString());
            }
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

        if (encrypt) {
            Map<String, Object> data = new HashMap<>();
            byte [] key = asimCrypto.encrypt(simCrypto.getKey().toBytes());
            data.put("simKey", key);
            this.sendToClient("encrypt_start", data);
        }
    }

    @Override
    public String getAddress() {
        return this.address;
    }
}
