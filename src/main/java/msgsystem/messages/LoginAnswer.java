package msgsystem.messages;

import msgsystem.Abonent;
import servlets.LoginServlet;

/**
 * Created by max on 11.03.15.
 */
public class LoginAnswer extends AbstractMsg {

    private final boolean status;

    public LoginAnswer(String addressFrom, String addressTo, boolean status) {
        super(addressFrom, addressTo);
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
