package msgsystem.messages;

import msgsystem.Abonent;
import msgsystem.AddressService;
import servlets.ListServlet;

import java.util.Set;

/**
 * Created by max on 13.03.15.
 */
public class ListAnswer extends AbstractMsg {
    private final Set<String> others;

    public ListAnswer(String addressTo, Set<String> others) {
        super(AddressService.ACCOUNT_SERVICE, addressTo);
        this.others = others;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof ListServlet) {
            ListServlet servlet = (ListServlet) abonent;
            servlet.recieveAnswer(this);
        }
    }

    public Set<String> getOthers() {
        return this.others;
    }
}
