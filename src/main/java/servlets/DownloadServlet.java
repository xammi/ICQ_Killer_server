package servlets;

import org.eclipse.jetty.http.MimeTypes;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by max on 24.03.15.
 */
public class DownloadServlet extends Servlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String file = request.getParameter("file");
        String path = "/tmp/media/" + file;

        MimeTypes mimeTypes = new MimeTypes();
        String mimeType = mimeTypes.getMimeByExtension(file);

        response.setHeader("Content-Length", String.valueOf(new File(path).length()));
        response.setContentType(mimeType);
        response.setStatus(HttpServletResponse.SC_OK);

        ServletOutputStream outputStream = response.getOutputStream();

        try (FileInputStream inputStream = new FileInputStream(path)) {
            byte[] buffer = new byte[1024];
            int bytesRead = 0;

            do {
                bytesRead = inputStream.read(buffer, 0, buffer.length);
                outputStream.write(buffer, 0, bytesRead);
            }
            while (bytesRead > 0);

            outputStream.flush();
        }
        logger.log("DownloadServlet: success");
    }
}
