package msgsystem.messages;

import msgsystem.Abonent;
import msgsystem.AddressService;
import accounts.AccountService;

/**
 * Created by max on 11.03.15.
 */
public class SendQuery extends AbstractMsg {
    private final String from;
    private final String whom;
    private final String message;

    public SendQuery(String from, String whom, String message) {
        super(null, AddressService.ACCOUNT_SERVICE);
        this.from = from;
        this.whom = whom;
        this.message = message;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof AccountService) {
            AccountService service = (AccountService) abonent;
            service.sendMessage(this);
        }
    }

    public String getFrom() {
        return from;
    }

    public String getWhom() {
        return whom;
    }

    public String getMessage() {
        return message;
    }
}
