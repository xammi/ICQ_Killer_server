/**
 * Created by max on 10.03.15.
 */

import msgsystem.MessageSystem;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
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
        MessageSystem msys = new MessageSystem();
        AccountService accountService = new AccountService(msys);

        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        threadPool.submit(accountService);
    }

    private static ServletContextHandler createServlets() {
        Servlet servlet = new Servlet();
        SocketServlet socketServlet = new SocketServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(socketServlet), "/send");
        context.addServlet(new ServletHolder(servlet), "/");
        return context;
    }
}