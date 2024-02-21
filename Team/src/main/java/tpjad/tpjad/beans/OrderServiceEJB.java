package tpjad.tpjad.beans;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tpjad.tpjad.entities.Order;

import java.util.List;

@Stateless
public class OrderServiceEJB {

    @PersistenceContext(unitName = "Store")
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createOrder(Order order) {
        em.persist(order);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return em.createQuery("SELECT o FROM Order o WHERE o.user.id = :userId", Order.class)
                .setParameter("userId", userId)
                .getResultList();
    }

}
