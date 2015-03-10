/**
 * Created by max on 10.03.15.
 */

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import services.AccountService;
import servlets.LoginServlet;
import sockets.SocketServlet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main
{
    public static void main(String[] args) throws Exception{
        try {
            launchServices();

            ServletContextHandler context = createServlets();
            HandlerList handlers = new HandlerList();
            handlers.addHandler(context);

            int serverPort = 8080;

            Server server = new Server(serverPort);
            server.setHandler(handlers);
            server.start();
            server.join();
        }
        catch (Exception e) {
            System.out.println("Error" + e);
        }
    }

    private static void launchServices() {
        AccountService accountService = new AccountService();

        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        threadPool.submit(accountService);
    }

    private static ServletContextHandler createServlets() {
        LoginServlet loginServlet = new LoginServlet();
        SocketServlet socketServlet = new SocketServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(socketServlet), "/send");
        context.addServlet(new ServletHolder(loginServlet), "/");
        return context;
    }
}