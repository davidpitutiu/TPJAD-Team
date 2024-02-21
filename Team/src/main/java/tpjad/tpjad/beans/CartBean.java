package tpjad.tpjad.beans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import tpjad.tpjad.entities.Cart;
import tpjad.tpjad.entities.Product;
import tpjad.tpjad.entities.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;


@Named
@RequestScoped
public class CartBean {
    private BigDecimal totalPrice;


    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    private Long cartItemId;

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getCartItemId() {
        return this.cartItemId;
    }

    @EJB
    private CartServiceEJB cartService;
    @EJB
    private ProductServiceEJB productService;
    @EJB
    private UserServiceEJB userService;

    public void addToCart(Long productId) {
        FacesContext context = FacesContext.getCurrentInstance();
        Long userId = (Long) context.getExternalContext().getSessionMap().get("userId");

        System.out.println("User" + userId);

        if (userId == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "User not logged in", null));
            return;
        }

        User user = userService.findUserById(userId);
        Product product = productService.findProductById(productId);

        if (user != null && product != null) {
            Cart cartItem = new Cart(user, product, 1);
            cartService.addCartItem(cartItem);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product added to cart successfully", null));
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to add product to cart", null));
        }
    }

    public List<Cart> getCartItems() {
        Long userId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
        if (userId != null) {
            return cartService.getCartItemsByUserId(userId);
        }
        return new ArrayList<>();
    }


    public void increaseQuantity() {
        if (cartItemId == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cart item ID is null", null));
            return;
        }

        try {
            Cart cartItem = cartService.findCartItemById(cartItemId);
            if (cartItem != null) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartService.updateCartItem(cartItem);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Quantity increased successfully", null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to increase quantity", null));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error increasing quantity", null));
        }
    }

    public void decreaseQuantity() {
        if (cartItemId == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cart item ID is null", null));
            return;
        }

        try {
            Cart cartItem = cartService.findCartItemById(cartItemId);
            if (cartItem != null) {
                if (cartItem.getQuantity() > 1) {

                    cartItem.setQuantity(cartItem.getQuantity() - 1);
                    cartService.updateCartItem(cartItem);
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Quantity decreased successfully", null));
                } else {

                    cartService.deleteCartItem(cartItem);
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Item removed from cart", null));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to decrease quantity", null));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error decreasing quantity", null));
        }
    }
    public void updateCart() {

    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<Cart> cartItems = getCartItems();
        for (Cart item : cartItems) {

            BigDecimal price = BigDecimal.valueOf(item.getProduct().getPrice());
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            BigDecimal itemTotal = price.multiply(quantity);
            totalPrice = totalPrice.add(itemTotal);
        }
        return totalPrice;
    }

    private List<CheckoutItem> checkoutItems = new ArrayList<>();

    public List<CheckoutItem> getCheckoutItems() {
        return checkoutItems;
    }

    public String prepareCheckout() {
        List<CheckoutItem> checkoutItems = new ArrayList<>();

        for (Cart item : getCartItems()) {
            checkoutItems.add(new CheckoutItem(item.getProduct().getId(), item.getQuantity()));
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("checkoutItems", checkoutItems);
        return "checkout?faces-redirect=true";
    }



    public static class CheckoutItem {
        private Long productId;
        private Integer quantity;

        public CheckoutItem(Long productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }


        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

}
