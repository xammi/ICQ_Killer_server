package servlets;

import msgsystem.messages.SendQuery;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by max on 21.03.15.
 */
public class UploadServlet extends Servlet {

    private static final String protocol = config.getString("general", "protocol");

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String from = request.getParameter("nickname");
        String whom = request.getParameter("whom");

        JSONObject json = new JSONObject();

        if (from == null) {
            json.put("status", "fail");
            json.put("error", "Unknown user (from)");
            logger.log("UploadServlet: Unknown user (from)");
        }
        else if (whom == null) {
            json.put("status", "fail");
            json.put("error", "Unknown user (whom)");
            logger.log("UploadServlet: Unknown user (whom)");
        }
        else {

            Part filePart = request.getPart("file-0");

            if (filePart == null) {
                json.put("status", "fail");
                json.put("error", "File does not exist");
                logger.log("UploadServlet: File does not exist");
            } else {
                String fileName = filePart.getSubmittedFileName();
                filePart.write(fileName);

                String link = "http://" + request.getHeader("Host");
                if (protocol.equals("SOAP")) {
                    link += "/download?file=" + fileName;
                }
                else if (protocol.equals("REST")) {
                    link += "/file/read?file=" + fileName;
                }

                String message = "File was uploaded: <a href=\"" + link + "\">" + fileName + "</a>";
                msys.sendMessage(new SendQuery(from, whom, message));

                json.put("status", "OK");
                json.put("file", fileName);
                logger.log("UploadServlet: success");
            }
        }

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printout = response.getWriter();
        printout.print(json);
        printout.flush();
    }
}
