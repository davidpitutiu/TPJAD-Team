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
    private String searchTerm;

    @EJB
    private ProductServiceEJB productService;

    @PostConstruct
    public void init() {
        products = productService.getAllProducts();
    }

    public void searchProducts() {
        if (searchTerm != null && !searchTerm.isEmpty()) {
            products = productService.searchProductsByName(searchTerm);
        } else {
            products = productService.getAllProducts();
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
