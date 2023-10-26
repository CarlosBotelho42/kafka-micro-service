package org.example;

import java.math.BigDecimal;

public class Order {

    private final String userId, OrderId;
    private final BigDecimal amount;

    public Order(String userId, String orderId, BigDecimal amount) {
        this.userId = userId;
        OrderId = orderId;
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "userId='" + userId + '\'' +
                ", OrderId='" + OrderId + '\'' +
                ", amount=" + amount +
                '}';
    }
}
