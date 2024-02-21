package tpjad.tpjad.beans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import tpjad.tpjad.dtos.ProductDTO;
import tpjad.tpjad.entities.Product;

@Named
@RequestScoped
public class ProductBean {
    private ProductDTO productDTO = new ProductDTO();

    @EJB
    private ProductServiceEJB productService; // Directly use ProductServiceEJB

    public ProductDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(ProductDTO productDTO) {
        this.productDTO = productDTO;
    }

    public String addProduct() {
        Product product = new Product(productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), productDTO.getStock());
        productService.addProduct(product);
        return "productList"; // Navigate accordingly
    }
}
