package services;

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
            System.out.println("login: User already exists (" + user + ")");
        }
        else {
            Set<String> others = new HashSet<>(clients.keySet());
            others.remove(user);

            clients.put(user, null);
            msys.sendMessage(new LoginAnswer(addressBack, true, others));
            System.out.println("login: success (" + user + ")");
        }
    }

    public void logout(LogoutQuery query) {
        String user = query.getUser();

        if (clients.containsKey(user)) {
            clients.remove(user);

            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            for (Socket socket : clients.values()) {
                socket.sendToClient("user_went_out", data);
            }

            System.out.println("logout: success (" + user + ")");
        }
        else {
            System.out.println("logout: Unknown user (" + user + ")");
        }
    }

    public void setSocket(SetSocket msg) {
        String user = msg.getUser();
        Socket socket = msg.getSocket();

        if (clients.containsKey(user)) {
            clients.put(user, socket);
            System.out.println("setSocket: success (" + user + ")");
        }
        else {
            System.out.println("setSocket: Unknown user (" + user + ")");
        }
    }

    public void sendMessage(SendQuery msg) {
        String whom = msg.getWhom();
        Socket socketWhom = this.clients.get(whom);

        if (socketWhom != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("from", msg.getFrom());
            data.put("message", msg.getMessage());

            socketWhom.sendToClient("message", data);
        }
        else {
            System.out.println("AccountServer.sendMessage: " + "Unknown whom (" + whom + ")");
        }
    }
}
