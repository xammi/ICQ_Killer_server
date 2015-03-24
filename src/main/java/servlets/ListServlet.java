package servlets;

import msgsystem.messages.ListAnswer;
import msgsystem.messages.ListQuery;
import msgsystem.messages.LoginAnswer;
import msgsystem.messages.LoginQuery;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/**
 * Created by max on 13.03.15.
 */
public class ListServlet extends Servlet {
    private Set<String> others;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String user = request.getParameter("nickname");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            logger.log("ListServlet: Unknown user");
            System.out.println("ListServlet: Unknown user");
            return;
        }

        msys.sendMessage(new ListQuery(getAddress(), user));

        waitForAnswer();

        JSONObject json = new JSONObject();
        json.put("clients", this.others);

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printout = response.getWriter();
        printout.print(json);
        printout.flush();

        logger.log("ListServlet: success");
    }

    public void recieveAnswer(ListAnswer msg) {
        this.others = msg.getOthers();
        resume();
    }
}
