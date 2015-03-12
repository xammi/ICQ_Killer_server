package msgsystem.messages;

import msgsystem.Abonent;
import msgsystem.AddressService;
import services.AccountService;

/**
 * Created by max on 12.03.15.
 */
public class LogoutQuery extends AbstractMsg {

    private final String user;

    public LogoutQuery(String addressFrom, String user) {
        super(addressFrom, AddressService.ACCOUNT_SERVICE);
        this.user = user;
    }

    public String getUser() {
        return this.user;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof AccountService) {
            AccountService service = (AccountService) abonent;
            service.logout(this);
        }
    }
}
