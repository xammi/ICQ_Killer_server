/**
 * Created by max on 28.03.15.
 */

import msgsystem.messages.LoginAnswer;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import servlets.LoginServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

public class LoginServletTest {

    class LoginServletWrapper extends LoginServlet {
        private LoginAnswer answer;

        public LoginServletWrapper(LoginAnswer answer) {
            this.answer = answer;
        }

        @Override
        public void doPost(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException
        {
            super.doPost(request, response);
        }

        @Override
        public void waitForAnswer() {
             this.recieveAnswer(answer);
        }
    }

    @Test
    public void testDoPostWithoutNickname() throws Exception {
        LoginAnswer answer = new LoginAnswer(null, false, null);

        LoginServletWrapper servlet = new LoginServletWrapper(answer);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        servlet.doPost(request, response);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void testDoPostOK() throws Exception {
        Set<String> clients = new HashSet<String>();
        LoginAnswer answer = new LoginAnswer(null, true, clients);

        LoginServletWrapper servlet = new LoginServletWrapper(answer);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        PrintWriter printer = mock(PrintWriter.class);

        when(request.getSession(true)).thenReturn(session);
        when(response.getWriter()).thenReturn(printer);
        when(request.getParameter("nickname")).thenReturn("max");

        servlet.doPost(request, response);

        verify(response, times(1)).setContentType("application/json; charset=UTF-8");
        verify(session, times(1)).setAttribute("nickname", "max");

        String testJson = "{\"clients\":[],\"status\":\"OK\"}";

        ArgumentCaptor<JSONObject> argument = ArgumentCaptor.forClass(JSONObject.class);
        verify(printer, times(1)).print(argument.capture());
        Assert.assertEquals(testJson, argument.getValue().toString());
    }

    @Test
    public void testDoPostFAIL() throws Exception {
        Set<String> clients = new HashSet<String>();
        LoginAnswer answer = new LoginAnswer(null, false, clients);

        LoginServletWrapper servlet = new LoginServletWrapper(answer);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter printer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(printer);
        when(request.getParameter("nickname")).thenReturn("max");

        servlet.doPost(request, response);

        String testJson = "{\"clients\":[],\"status\":\"FAIL\"}";

        ArgumentCaptor<JSONObject> argument = ArgumentCaptor.forClass(JSONObject.class);
        verify(printer, times(1)).print(argument.capture());
        Assert.assertEquals(testJson, argument.getValue().toString());
    }
}
