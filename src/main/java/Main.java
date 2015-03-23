/**
 * Created by max on 10.03.15.
 */

//import loggers.ImmediateLogger;
//import loggers.Logger;
import confparser.Config;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import accounts.AccountService;
import servlets.*;
import sockets.SocketServlet;
import javax.servlet.MultipartConfigElement;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main
{
    private static final Config config = Config.getInstance();
//    private static final Logger logger = ImmediateLogger.getInstance("Main");

    public static void main(String[] args) throws Exception{
        try {
            launchServices();

            ServletContextHandler context = createServlets();
            HandlerList handlers = createHandlers(context);

            int serverPort = config.getInt("general", "port");

            Server server = new Server(serverPort);
            server.setHandler(handlers);

//            logger.log("Server was started successfully");

            server.start();
            server.join();
        }
        catch (Exception e) {
//            logger.log("Error" + e.toString());

            System.out.println("Error" + e.toString());
            e.printStackTrace();
        }
    }

    private static void launchServices() {
        AccountService accountService = new AccountService();

        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        threadPool.submit(accountService);
    }

    private static ServletContextHandler createServlets() {
        Servlet loginServlet = new LoginServlet();
        Servlet listServlet = new ListServlet();
        Servlet logoutServlet = new LogoutServlet();
        Servlet desktopServlet = new DesktopServlet();
        Servlet uploadServlet = new UploadServlet();
        SocketServlet socketServlet = new SocketServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(socketServlet), "/send");
        context.addServlet(new ServletHolder(loginServlet), "/login");
        context.addServlet(new ServletHolder(logoutServlet), "/logout");
        context.addServlet(new ServletHolder(listServlet), "/list");
        context.addServlet(new ServletHolder(desktopServlet), "/desktop");

        ServletHolder uploadHolder = new ServletHolder(uploadServlet);
        uploadHolder.getRegistration().setMultipartConfig(new MultipartConfigElement("media"));
        context.addServlet(uploadHolder, "/upload");

        return context;
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
}