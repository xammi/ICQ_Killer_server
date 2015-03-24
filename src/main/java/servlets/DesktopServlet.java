package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by max on 11.03.15.
 */
public class DesktopServlet extends Servlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter printout = response.getWriter();

        try (BufferedReader reader = new BufferedReader(new FileReader(TEMPLATES_DIR + "desktop.html"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                printout.println(line);
            }
        }
        logger.log("DesktopServlet: success");
    }
}
