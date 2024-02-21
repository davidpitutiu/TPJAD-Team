package tpjad.tpjad.beans;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import tpjad.tpjad.entities.Product;
import java.util.List;

@Named
@RequestScoped
public class ProductDisplayBean {

    private List<Product> products;

    @EJB
    private ProductServiceEJB productService;

    @PostConstruct
    public void init() {
        products = productService.getAllProducts();
    }

    public List<Product> getProducts() {
        return products;
    }
}

