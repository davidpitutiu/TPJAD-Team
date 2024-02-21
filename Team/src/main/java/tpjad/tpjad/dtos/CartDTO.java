package tpjad.tpjad.dtos;

public class CartDTO {
    private Integer productId;
    private Integer quantity;

    // Default constructor
    public CartDTO() {
    }

    // Constructor with fields
    public CartDTO(Integer productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
