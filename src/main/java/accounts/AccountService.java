package accounts;

import loggers.Logger;
import managers.LoggerManager;
import msgsystem.Abonent;
import msgsystem.AddressService;
import msgsystem.MessageSystem;
import msgsystem.messages.*;
import sockets.Socket;

import java.lang.Thread;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by max on 10.03.15.
 */
public class AccountService implements Abonent, Runnable {

    private static final Logger logger = LoggerManager.getFor("AccountService");

    private final Map<String, Socket> clients = new HashMap<>();
    private final MessageSystem msys;

    public AccountService() {
        this.msys = MessageSystem.getInstance();
        this.msys.register(this);
    }

    @Override
    public String getAddress() {
         return AddressService.ACCOUNT_SERVICE;
    }

    @Override
    public void run() {
        while (true) {
            this.msys.executeFor(this);
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void login(LoginQuery query) {
        String addressBack = query.getAddressFrom();
        String user = query.getUser();

        if (clients.containsKey(user)) {
            msys.sendMessage(new LoginAnswer(addressBack, false, null));

            logger.log("login: User already exists (" + user + ")");
            System.out.println("login: User already exists (" + user + ")");
        }
        else {
            Set<String> others = new HashSet<>(clients.keySet());
            others.remove(user);

            clients.put(user, null);
            msys.sendMessage(new LoginAnswer(addressBack, true, others));

            logger.log("login: success (" + user + ")");
            System.out.println("login: success (" + user + ")");
        }
    }

    public void logout(LogoutQuery query) {
        String user = query.getUser();

        if (clients.containsKey(user)) {
            clients.remove(user);

            Map<String, Object> data = new HashMap<>();
            data.put("nickname", user);
            for (Socket socket : clients.values()) {
                if (socket != null)
                    socket.sendToClient("user_went_out", data);
            }

            logger.log("logout: success (" + user + ")");
            System.out.println("logout: success (" + user + ")");
        }
        else {
            logger.log("logout: Unknown user (" + user + ")");
            System.out.println("logout: Unknown user (" + user + ")");
        }
    }

    public void setSocket(SetSocket msg) {
        String user = msg.getUser();
        Socket socket = msg.getSocket();

        if (clients.containsKey(user)) {
            clients.put(user, socket);

            Map<String, Object> data = new HashMap<>();
            data.put("nickname", user);

            for (Map.Entry<String, Socket> entry : clients.entrySet()) {
                Socket other = entry.getValue();

                if (other != null && ! entry.getKey().equals(user))
                    other.sendToClient("user_come_in", data);
            }

            logger.log("setSocket: success (" + user + ")");
            System.out.println("setSocket: success (" + user + ")");
        }
        else {
            logger.log("setSocket: Unknown user (" + user + ")");
            System.out.println("setSocket: Unknown user (" + user + ")");
        }
    }

    public void sendMessage(SendQuery query) {
        String whom = query.getWhom();
        Socket socketWhom = this.clients.get(whom);

        if (socketWhom != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("from", query.getFrom());
            data.put("message", query.getMessage());

            socketWhom.sendToClient("message", data);
        }
        else {
            logger.log("sendMessage: Unknown whom (" + whom + ")");
            System.out.println("AccountServer.sendMessage: Unknown whom (" + whom + ")");
        }
    }

    public void sendMessage(DownloadMsg query) {
        String whom = query.getWhom();
        Socket socketWhom = this.clients.get(whom);

        if (socketWhom != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("from", query.getFrom());
            data.put("url", query.getUrl());

            socketWhom.sendToClient("download", data);
        }
        else {
            logger.log("sendMessage: Unknown whom (" + whom + ")");
            System.out.println("AccountServer.sendMessage: Unknown whom (" + whom + ")");
        }
    }

    public void getOthers(ListQuery query) {
        String addressBack = query.getAddressFrom();
        String user = query.getUser();

        Set<String> others = new HashSet<>(clients.keySet());
        others.remove(user);

        msys.sendMessage(new ListAnswer(addressBack, others));

        logger.log("getOthers: (" + user + ")");
        System.out.println("getOthers: (" + user + ")");
    }
}
