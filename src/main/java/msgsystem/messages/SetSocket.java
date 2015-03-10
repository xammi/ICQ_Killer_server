package msgsystem.messages;

import msgsystem.Abonent;
import msgsystem.AddressService;
import services.AccountService;
import sockets.Socket;

/**
 * Created by max on 11.03.15.
 */
public class SetSocket extends AbstractMsg {

    private final Socket socket;
    private final String user;

    public SetSocket(String user, Socket socket) {
        super(null, AddressService.ACCOUNT_SERVICE);
        this.socket = socket;
        this.user = user;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof AccountService) {
            AccountService service = (AccountService) abonent;
            service.setSocket(this);
        }
    }

    public Socket getSocket() {
        return this.socket;
    }

    public String getUser() {
        return this.user;
    }
}
