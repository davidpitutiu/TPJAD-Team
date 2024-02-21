package tpjad.tpjad.beans;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tpjad.tpjad.entities.Cart;
import tpjad.tpjad.entities.User;

import java.util.List;

@Stateless
public class CartServiceEJB{

    @PersistenceContext(unitName = "Store")
    private EntityManager entityManager;

    public List<Cart> getCartItemsByUserId(Long userId) {
        return entityManager.createQuery("SELECT c FROM Cart c WHERE c.user.id = :userId", Cart.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public void addCartItem(Cart cartItem) {
        try {
            entityManager.persist(cartItem);
            System.out.println("Cart item added successfully: " + cartItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateCartItem(Cart cartItem) {
        entityManager.merge(cartItem);
        System.out.println("Cart item updated successfully: " + cartItem);
    }
    public void deleteCartItem(Cart cartItem) {
        if (cartItem != null) {
            entityManager.remove(entityManager.contains(cartItem) ? cartItem : entityManager.merge(cartItem));
            System.out.println("Cart item deleted successfully: " + cartItem);
        }
    }
    public void clearCartByUserId(Long userId) {
        List<Cart> cartItems = entityManager.createQuery("SELECT c FROM Cart c WHERE c.user.id = :userId", Cart.class)
                .setParameter("userId", userId)
                .getResultList();
        for (Cart item : cartItems) {
            entityManager.remove(item);
        }
    }
    public Cart findCartItemById(Long cartItemId) {
        return entityManager.find(Cart.class, cartItemId);
    }
}
