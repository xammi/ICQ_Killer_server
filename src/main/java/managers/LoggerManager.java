package managers;

import confparser.Config;
import loggers.LazyLogger;
import loggers.Logger;

import loggers.ImmediateLogger;

/**
 * Created by max on 24.03.15.
 */
public class LoggerManager {
    private static final Config config = Config.getInstance();
    private static final String strategy = config.getString("logging", "strategy");

    public static Logger getFor(String tagName) {
        switch (strategy) {
            case "immediate":
                return ImmediateLogger.getInstance(tagName);
            case "lazy":
                return LazyLogger.getInstance(tagName);
            default:
                return ImmediateLogger.getInstance(tagName);
        }
    }
}
