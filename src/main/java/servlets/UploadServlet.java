package servlets;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by max on 21.03.15.
 */
public class UploadServlet extends Servlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        JSONObject json = new JSONObject();
        File file = (File) request.getAttribute("file");

        if (file == null || !file.exists()) {
            json.put("status", "fail");
            json.put("error", "File does not exist");
        }
        else if (file.isDirectory()) {
            json.put("status", "fail");
            json.put("error", "File is a directory");
        }
        else {
            File outputFile = new File(request.getParameter( "file"));
            file.renameTo(outputFile);

            json.put("status", "OK");
            json.put("file", file.getName());
        }

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printout = response.getWriter();
        printout.print(json);
        printout.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        doGet(request, response);
    }
}
