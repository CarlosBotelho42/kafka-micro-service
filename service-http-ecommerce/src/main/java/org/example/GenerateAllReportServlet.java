package org.example;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GenerateAllReportServlet extends HttpServlet {

    private final KafkaDispatcher<String> bathDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        super.destroy();
        bathDispatcher.close();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {

            bathDispatcher.send("SEND_MESSAGE_TO_ALL_USERS", "USER_GENERATE_READING_REPORT", "USER_GENERATE_READING_REPORT");

            System.out.println("Enviando relatorios gerados, para todos os usuarios");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Relatorios enviados");

        } catch (ExecutionException | IOException | InterruptedException e) {
            throw new ServletException(e);
        }
    }
}
