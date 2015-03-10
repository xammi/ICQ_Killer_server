package msgsystem.messages;

import msgsystem.Abonent;
import msgsystem.AddressService;
import servlets.LoginServlet;

import java.util.Set;

/**
 * Created by max on 11.03.15.
 */
public class LoginAnswer extends AbstractMsg {

    private final boolean status;
    private final Set<String> others;

    public LoginAnswer(String addressTo, boolean status, Set<String> others) {
        super(AddressService.ACCOUNT_SERVICE, addressTo);
        this.status = status;
        this.others = others;
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

    public Set<String> getOthers() {
        return this.others;
    }
}
