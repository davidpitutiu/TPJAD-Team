package tpjad.tpjad.beans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.transaction.Transactional;
import tpjad.tpjad.entities.Cart;
import tpjad.tpjad.entities.Order;
import tpjad.tpjad.entities.Product;
import tpjad.tpjad.entities.User;

import java.sql.Timestamp;
import java.util.List;
import java.math.BigDecimal;

@Named
@RequestScoped
@Transactional
public class CheckoutBean {
    @PersistenceContext(unitName = "Store")
    private EntityManager entityManager;
    @EJB
    private CartServiceEJB cartService;
    @EJB
    private ProductServiceEJB productService;

    @EJB
    private OrderServiceEJB orderService;
    @Inject
    private CartBean cartBean;

    private String paymentMethod;
    private String deliveryAddress;

    // Getters and Setters
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
    private BigDecimal totalPrice; // Field to hold the total price

    // Getter method for totalPrice
    public BigDecimal getTotalPrice() {
        // Logic to calculate the total price if not already done
        // This calculation logic could be moved here if you want to ensure it's always up-to-date when accessed
        return totalPrice;
    }
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    public String processCheckout() {
        FacesContext context = FacesContext.getCurrentInstance();
        Long userId = (Long) context.getExternalContext().getSessionMap().get("userId");
        if (userId == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "User not logged in", null));
            return "login";
        }

        User user = entityManager.find(User.class, userId);
        BigDecimal total = cartBean.getTotalPrice();
        @SuppressWarnings("unchecked")
        List<CartBean.CheckoutItem> checkoutItems = (List<CartBean.CheckoutItem>) context.getExternalContext().getSessionMap().get("checkoutItems");

        if (checkoutItems == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Checkout items are missing.", null));
            return null;
        }

        if (user != null && total.compareTo(BigDecimal.ZERO) > 0) {
            Order order = new Order();
            order.setUser(user);
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setStatus("Pending");
            order.setPaymentMethod(paymentMethod);
            order.setDeliveryAddress(deliveryAddress);
            order.setTotal(total);

            for (CartBean.CheckoutItem item : checkoutItems) {
                Product product = entityManager.find(Product.class, item.getProductId());
                if (product != null && product.getStock() >= item.getQuantity()) {
                    product.setStock(product.getStock() - item.getQuantity());
                    entityManager.merge(product); // Update product with new stock
                } else {
                    // Handle stock issue or product not found
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Stock issue for product ID " + item.getProductId(), null));
                    return null; // Optionally redirect to a different page or handle the error differently
                }
            }

            orderService.createOrder(order); // Persist the order
            cartService.clearCartByUserId(userId); // Clear the cart

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Order processed successfully. Cart is now empty.", null));
            return "orderConfirmation?faces-redirect=true";
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error processing order.", null));
            return null;
        }
    }


    private void clearCart(Long userId) {
        // Implementation to clear the user's cart
    }
    public String checkout() {
        FacesContext context = FacesContext.getCurrentInstance();
        Long userId = (Long) context.getExternalContext().getSessionMap().get("userId");
        if (userId == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "User not logged in", null));
            return null; // Or navigate to login page.
        }

        List<Cart> cartItems = cartService.getCartItemsByUserId(userId);
        boolean stockIssue = false;

        for (Cart item : cartItems) {
            Product product = productService.findProductById(item.getProduct().getId());
            if (product.getStock() < item.getQuantity()) {
                item.setQuantity(product.getStock());
                cartService.updateCartItem(item);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Adjusted quantity for product " + product.getName() + " to the available stock.", null));
                stockIssue = true;
            }
        }

        if (stockIssue) {
            return null; // Stay on the cart page to let the user review changes.
        }

        // Proceed with the checkout process if no stock issues.
        // Perform operations like creating an order, clearing the cart, sending confirmation, etc.

        return "checkoutSuccess"; // Navigate to the checkout success page.
    }
}

