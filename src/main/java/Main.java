/**
 * Created by max on 10.03.15.
 */

//import loggers.ImmediateLogger;
//import loggers.Logger;
import Crypto.Asimmetric.AsimCrypto;
import Crypto.CryptoFactory;
import Crypto.NoSuchCryptoRealisationException;
import confparser.Config;
import loggers.Logger;
import managers.LoggerManager;
import managers.ServletManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import accounts.AccountService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main
{
    private static final Config config = Config.getInstance();
    private static final Logger logger = LoggerManager.getFor("Main");

    public static void main(String[] args) throws Exception {
        try {
            prepareEncrypt();
            launchServices();

            ServletManager servletManager = new ServletManager();
            ServletContextHandler context = servletManager.configure();
            HandlerList handlers = createHandlers(context);

            int serverPort = config.getInt("general", "port");

            Server server = new Server(serverPort);
            server.setHandler(handlers);

            logger.log("Server was started successfully");
            server.start();

            server.join();
        }
        catch (Exception e) {
            logger.log("Error" + e.toString());
            e.printStackTrace();
        }
    }

    private static void launchServices() {
        AccountService accountService = new AccountService();

        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        threadPool.submit(accountService);
    }

    private static HandlerList createHandlers(ServletContextHandler context) {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);

        final String STATIC_DIR = config.getString("static", "static_dir");
        resourceHandler.setResourceBase(STATIC_DIR);

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(context);

        return handlers;
    }

    private static void prepareEncrypt() throws NoSuchCryptoRealisationException {
        AsimCrypto asimCrypto = CryptoFactory.getAsimInstance(config.getString("encrypt", "asimmetric"));

        asimCrypto.setPublicPath(config.getString("encrypt", "public_path"));
        asimCrypto.setPrivatePath(config.getString("encrypt", "private_path"));
        asimCrypto.generateKeys();
    }
}