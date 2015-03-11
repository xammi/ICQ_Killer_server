package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by eugene on 11.03.15.
 */
// TODO Just to test how server works
public class AdminServlet extends Servlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        PrintWriter printout = response.getWriter();
        printout.print("I am the Admin Page");
        printout.flush();
    }
}
