package org.example;

import org.example.dispacher.KafkaDispatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<Order>();

    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {

            var email = req.getParameter("email");
            var userId = UUID.randomUUID().toString();
            var orderId = UUID.randomUUID().toString();
            var amount = new BigDecimal(req.getParameter("amount"));

            var order = new Order(userId, orderId, amount, email);
            orderDispatcher.send("ECOMMERCE_NEW_ORDER", email,
                    new CorrelationId(NewOrderServlet.class.getSimpleName()),
                    order);

            System.out.println("Processo da nova compra terminado!!!");

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Novo pedido enviado");

        } catch (ExecutionException e) {
            throw new ServletException(e);

        } catch (InterruptedException e) {
            throw new ServletException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}