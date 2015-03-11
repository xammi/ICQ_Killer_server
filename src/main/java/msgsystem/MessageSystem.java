package msgsystem;

import msgsystem.messages.AbstractMsg;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by max on 10.03.15.
 * The Class for an asyncronous interaction between the system threads.
 * Singleton.
 */
public class MessageSystem {

    private final Map<String, ConcurrentLinkedQueue<AbstractMsg>> messages = new HashMap<>();

    private MessageSystem() {}

    private static class LazyHolder {
        private static final MessageSystem INSTANCE = new MessageSystem();
    }

    public static MessageSystem getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void register(Abonent abonent) {
        String address = abonent.getAddress();
        this.messages.put(address, new ConcurrentLinkedQueue<AbstractMsg>());
    }

    public boolean sendMessage(AbstractMsg message) {
        boolean result;
        String addressTo = message.getAddressTo();
        Queue<AbstractMsg> messageQueue = this.messages.get(addressTo);

        try {
            messageQueue.add(message);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public void executeFor(Abonent abonent) {
        String address = abonent.getAddress();
        Queue<AbstractMsg> messageQueue = this.messages.get(address);

        while (!messageQueue.isEmpty()) {
            AbstractMsg message = messageQueue.poll();
            message.exec(abonent);
        }
    }
}
