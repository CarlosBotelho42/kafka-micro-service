package org.example;

import java.math.BigDecimal;

public class Order {

    private String userId, OrderId;
    private BigDecimal amount;
    private String email;

    public Order(String userId, String orderId, BigDecimal amount, String email) {
        this.userId = userId;
        this.OrderId = orderId;
        this.amount = amount;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}
