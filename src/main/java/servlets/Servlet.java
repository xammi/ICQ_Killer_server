package servlets;

import msgsystem.Abonent;
import msgsystem.AddressService;
import msgsystem.MessageSystem;
import javax.servlet.http.HttpServlet;

/**
 * Created by max on 11.03.15.
 */
public abstract class Servlet extends HttpServlet implements Abonent {

    protected static final String TEMPLATES_DIR = "src/main/templates/";
    protected static final String OK = "OK";
    protected static final String FAILED = "FAIL";

    protected MessageSystem msys;
    protected final String address = AddressService.getServletAddress();
    protected boolean isWaiting;

    public Servlet() {
        this.msys = MessageSystem.getInstance();
        this.msys.register(this);
    }

    @Override
    public String getAddress() {
        return this.address;
    }

    protected void waitForAnswer() {
        this.isWaiting = true;

        while (this.isWaiting) {
            this.msys.executeFor(this);
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void resume() {
        this.isWaiting = false;
    }
}
