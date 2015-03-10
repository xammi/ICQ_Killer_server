package msgsystem.messages;

import msgsystem.Abonent;
import msgsystem.AddressService;
import servlets.LoginServlet;

/**
 * Created by max on 11.03.15.
 */
public class LoginAnswer extends AbstractMsg {

    private final boolean status;

    public LoginAnswer(String addressTo, boolean status) {
        super(AddressService.ACCOUNT_SERVICE, addressTo);
        this.status = status;
    }

    public boolean getStatus() {
        return this.status;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof LoginServlet) {
            LoginServlet servlet = (LoginServlet) abonent;
            servlet.recieveAnswer(this);
        }
    }
}
