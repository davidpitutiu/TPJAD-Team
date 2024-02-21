package tpjad.tpjad.beans;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tpjad.tpjad.entities.Product;
import java.util.List;

@Stateless
public class ProductServiceEJB {

    @PersistenceContext(unitName = "Store")
    private EntityManager entityManager;

    public List<Product> getAllProducts() {
        return entityManager.createQuery("SELECT p FROM Product p", Product.class).getResultList();
    }

    public Product getProductById(Long id) {
        return entityManager.find(Product.class, id);
    }

    public void addProduct(Product product) {
        entityManager.persist(product);
    }

    public void updateProduct(Product product) {
        entityManager.merge(product);
    }

    public void deleteProduct(Long id) {
        Product product = entityManager.find(Product.class, id);
        if (product != null) {
            entityManager.remove(product);
        }
    }
    public Product findProductById(Long productId) {
        return entityManager.find(Product.class, productId);
    }
}
