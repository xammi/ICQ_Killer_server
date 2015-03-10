package msgsystem.messages;

import msgsystem.AddressService;
import services.AccountService;
import msgsystem.Abonent;

/**
 * Created by max on 11.03.15.
 */
public class LoginQuery extends AbstractMsg {

    private final String user;

    public LoginQuery(String addressFrom, String user) {
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
            service.login(this);
        }
    }
}
