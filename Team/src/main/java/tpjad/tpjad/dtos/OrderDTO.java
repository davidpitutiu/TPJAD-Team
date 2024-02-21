package tpjad.tpjad.dtos;

import java.sql.Timestamp;
import java.math.BigDecimal;

public class OrderDTO {
    private Integer userId;
    private Timestamp orderDate;
    private String status;
    private String paymentMethod;
    private String deliveryAddress;
    private BigDecimal total; // New field for the total amount

    // Constructors
    public OrderDTO() {}

    public OrderDTO(Integer userId, Timestamp orderDate, String status, String paymentMethod, String deliveryAddress, BigDecimal total) {
        this.userId = userId;
        this.orderDate = orderDate;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.deliveryAddress = deliveryAddress;
        this.total = total; // Initialize the total field
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
