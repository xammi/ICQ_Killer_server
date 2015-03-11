/**
 * Created by max on 10.03.15.
 */

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import services.AccountService;
import servlets.DesktopServlet;
import servlets.LoginServlet;
import servlets.Servlet;
import sockets.SocketServlet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main
{
    private static final String STATIC_DIR = "src/main/static/";

    public static void main(String[] args) throws Exception{
        try {
            launchServices();

            ServletContextHandler context = createServlets();
            HandlerList handlers = createHandlers(context);

            int serverPort = 8082;

            Server server = new Server(serverPort);
            server.setHandler(handlers);
            server.start();
            server.join();
        }
        catch (Exception e) {
            System.out.println("Error" + e.toString());
        }
    }

    private static void launchServices() {
        AccountService accountService = new AccountService();

        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        threadPool.submit(accountService);
    }

    private static ServletContextHandler createServlets() {
        Servlet loginServlet = new LoginServlet();
        Servlet desktopServlet = new DesktopServlet();
        SocketServlet socketServlet = new SocketServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(socketServlet), "/send");
        context.addServlet(new ServletHolder(loginServlet), "/login");
        context.addServlet(new ServletHolder(desktopServlet), "/desktop");
        return context;
    }

    private static HandlerList createHandlers(ServletContextHandler context) {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setResourceBase(STATIC_DIR);

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(context);

        return handlers;
    }
}