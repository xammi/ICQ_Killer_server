package msgsystem.messages;

import accounts.AccountService;
import msgsystem.Abonent;
import msgsystem.AddressService;

/**
 * Created by max on 28.03.15.
 */
public class DownloadMsg extends AbstractMsg {
    private final String from;
    private final String whom;
    private final String url;

    public DownloadMsg(String from, String whom, String url) {
        super(null, AddressService.ACCOUNT_SERVICE);
        this.from = from;
        this.whom = whom;
        this.url = url;
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

    public String getUrl() {
        return url;
    }
}