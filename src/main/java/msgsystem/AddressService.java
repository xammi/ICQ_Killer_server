package msgsystem;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by max on 10.03.15.
 */

public class AddressService {
    private static final AtomicLong idCounter = new AtomicLong();

    public static final String ACCOUNT_SERVICE = "account_service";

    public static String getServletAddress() {
         return "servlet_" + idCounter.getAndIncrement();
    }
}
