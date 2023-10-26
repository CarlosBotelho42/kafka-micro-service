package org.example;

import java.math.BigDecimal;

public class Order {

    private String userId, OrderId;
    private BigDecimal amount;

    public Order(String userId, String orderId, BigDecimal amount) {
        this.userId = userId;
        OrderId = orderId;
        this.amount = amount;
    }
}
