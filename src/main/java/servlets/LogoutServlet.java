package servlets;

import msgsystem.messages.LoginQuery;
import msgsystem.messages.LogoutQuery;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by max on 12.03.15.
 */
public class LogoutServlet extends Servlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String user = request.getParameter("nickname");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("logout: Unknown user");
            return;
        }

        msys.sendMessage(new LogoutQuery(getAddress(), user));
    }
}
