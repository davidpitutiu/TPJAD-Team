package tpjad.tpjad.beans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import tpjad.tpjad.entities.Order;
import java.util.List;

@Named
@RequestScoped
public class OrderBean {

    @EJB
    private OrderServiceEJB orderService;

    public List<Order> getUserOrders() {
        FacesContext context = FacesContext.getCurrentInstance();
        Long userId = (Long) context.getExternalContext().getSessionMap().get("userId");
        if (userId != null) {
            return orderService.getOrdersByUserId(userId);
        }
        return null; // or empty list
    }
}
