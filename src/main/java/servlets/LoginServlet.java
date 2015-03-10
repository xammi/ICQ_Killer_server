package servlets;

import msgsystem.Abonent;
import msgsystem.AddressService;
import msgsystem.MessageSystem;
import msgsystem.messages.LoginAnswer;
import msgsystem.messages.LoginQuery;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by max on 10.03.15.
 */
public class LoginServlet extends Servlet {

    private boolean successLogin;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String user = request.getParameter("nickname");
        this.msys.sendMessage(new LoginQuery(getAddress(), AddressService.ACCOUNT_SERVICE, user));

        waitForAnswer();

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printout = response.getWriter();
        JSONObject json = new JSONObject();

        if (this.successLogin) {
            json.put("status", OK);
        }
        else {
            json.put("status", FAILED);
        }
        printout.print(json);
        printout.flush();
    }

    public void recieveAnswer(LoginAnswer msg) {
        this.successLogin = msg.getStatus();
        resume();
    }
}
