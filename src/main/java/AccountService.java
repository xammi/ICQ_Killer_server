import msgsystem.Abonent;
import msgsystem.MessageSystem;
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

    public AccountService(MessageSystem msys) {
        this.msys = msys;
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
}
