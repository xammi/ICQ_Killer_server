package managers;

import confparser.Config;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.*;
import sockets.SocketServlet;

import javax.servlet.MultipartConfigElement;

/**
 * Created by max on 24.03.15.
 */
public class ServletManager {

    private static final Config config = Config.getInstance();

    private final Servlet loginServlet = new LoginServlet();
    private final Servlet listServlet = new ListServlet();
    private final Servlet logoutServlet = new LogoutServlet();
    private final Servlet desktopServlet = new DesktopServlet();
    private final Servlet uploadServlet = new UploadServlet();
    private final Servlet downloadServlet = new DownloadServlet();
    private final SocketServlet socketServlet = new SocketServlet();

    public ServletContextHandler configure() {
        String protocol = config.getString("general", "protocol");

        if (protocol.equals("SOAP")) {
            return configureBySOAP();
        }
        if (protocol.equals("REST")) {
            return configureByREST();
        }
        return configureBySOAP();
    }

    private ServletContextHandler configureBySOAP() {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(socketServlet), "/send");
        context.addServlet(new ServletHolder(loginServlet), "/login");
        context.addServlet(new ServletHolder(logoutServlet), "/logout");
        context.addServlet(new ServletHolder(listServlet), "/list");
        context.addServlet(new ServletHolder(desktopServlet), "/desktop");

        ServletHolder uploadHolder = new ServletHolder(uploadServlet);
        uploadHolder.getRegistration().setMultipartConfig(new MultipartConfigElement("media"));
        context.addServlet(uploadHolder, "/upload");

        context.addServlet(new ServletHolder(downloadServlet), "/download");

        return context;
    }

    private ServletContextHandler configureByREST() {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(socketServlet), "/message/create");
        context.addServlet(new ServletHolder(loginServlet), "/user/create");
        context.addServlet(new ServletHolder(logoutServlet), "/user/delete");
        context.addServlet(new ServletHolder(listServlet), "/user/list");
        context.addServlet(new ServletHolder(desktopServlet), "/desktop");

        ServletHolder uploadHolder = new ServletHolder(uploadServlet);
        uploadHolder.getRegistration().setMultipartConfig(new MultipartConfigElement("media"));
        context.addServlet(uploadHolder, "/file/create");

        context.addServlet(new ServletHolder(downloadServlet), "/file/read");

        return context;
    }
}