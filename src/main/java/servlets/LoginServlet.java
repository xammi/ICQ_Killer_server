package servlets;

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
 * Created by max on 10.03.15.
 */
public class LoginServlet extends Servlet {

    private boolean successLogin;
    private Set<String> others;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String user = request.getParameter("nickname");
        if (user == null) {
            return;
        }

        msys.sendMessage(new LoginQuery(getAddress(), user));

        waitForAnswer();

        JSONObject json = new JSONObject();
        if (this.successLogin) {

            HttpSession session = request.getSession(true);
            session.setAttribute("nickname", user);

            json.put("status", OK);
        }
        else {
            json.put("status", FAILED);
        }
        json.put("clients", this.others);

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printout = response.getWriter();
        printout.print(json);
        printout.flush();
    }

    public void recieveAnswer(LoginAnswer msg) {
        this.successLogin = msg.getStatus();
        this.others = msg.getOthers();
        resume();
    }
}
