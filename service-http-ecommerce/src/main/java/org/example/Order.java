package org.example;

import java.math.BigDecimal;

public class Order {

    private String  OrderId;
    private BigDecimal amount;
    private String email;

    public Order(String orderId, BigDecimal amount, String email) {
        this.OrderId = orderId;
        this.amount = amount;
        this.email = email;
    }

    public String getOrderId() {
        return OrderId;
    }

    public String getEmail() {
        return email;
    }
}
