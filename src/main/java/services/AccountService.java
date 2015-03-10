package services;

import msgsystem.Abonent;
import msgsystem.AddressService;
import msgsystem.MessageSystem;
import msgsystem.messages.LoginAnswer;
import msgsystem.messages.LoginQuery;
import msgsystem.messages.SetSocket;
import sockets.Socket;

import java.lang.Thread;
import java.util.HashMap;
import java.util.Map;

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
            msys.sendMessage(new LoginAnswer(addressBack, false));
            System.out.println("login: User already exists (" + user + ")");
        }
        else {
            clients.put(user, null);
            msys.sendMessage(new LoginAnswer(addressBack, true));
            System.out.println("login: success(" + user + ")");
        }
    }

    public void setSocket(SetSocket msg) {
        String user = msg.getUser();
        Socket socket = msg.getSocket();

        if (clients.containsKey(user)) {
            clients.put(user, socket);
            System.out.println("setSocket: success(" + user + ")");
        }
        else {
            System.out.println("setSocket: Unknown user(" + user + ")");
        }
    }
}
