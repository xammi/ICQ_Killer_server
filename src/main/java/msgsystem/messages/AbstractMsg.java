package msgsystem.messages;

import msgsystem.Abonent;

/**
 * Created by max on 10.03.15.
 * Abstract message.
 * It will be used like a callback.
 */
public abstract class AbstractMsg {
    private final String addressFrom;
    private final String addressTo;

    public AbstractMsg(String addressFrom, String addressTo) {
        this.addressFrom = addressFrom;
        this.addressTo = addressTo;
    }

    public String getAddressFrom() {
        return this.addressFrom;
    }

    public String getAddressTo() {
        return this.addressTo;
    }

    public abstract void exec(Abonent abonent);
}
