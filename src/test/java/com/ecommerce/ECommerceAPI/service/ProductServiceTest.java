package com.ecommerce.ECommerceAPI.service;

import com.ecommerce.ECommerceAPI.model.Product;
import com.ecommerce.ECommerceAPI.model.dao.InventoryDAO;
import com.ecommerce.ECommerceAPI.model.dao.LocalUserDAO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryDAO inventoryDAO;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private LocalUserDAO localUserDAO;


    @Test
    @Transactional
    public void testGetProducts(){
        Assertions.assertTrue(productService.getProducts().containsAll(
                List.of(new Product[]{productService.getProduct(1L),
                        productService.getProduct(2L),
                        productService.getProduct(3L),
                        productService.getProduct(4L),
                        productService.getProduct(5L)
                })));
    }

    @Test
    @Transactional
    public void testGetProduct(){
        Product product = new Product();
        product.setName("Product #1");
        product.setShortDescription("Product one short description.");
        product.setLongDescription("This is a very long description of product #1.");
        product.setPrice(5.50);
        Assertions.assertEquals(productService.getProduct(1L).getName(),
                product.getName());
        Assertions.assertEquals(productService.getProduct(1L).getShortDescription(),
                product.getShortDescription());
        Assertions.assertEquals(productService.getProduct(1L).getLongDescription(),
                product.getLongDescription());
        Assertions.assertEquals(productService.getProduct(1L).getPrice(),
                product.getPrice());
    }

    @Test
    @Transactional
    public void testGetProductByName(){
        String name = "Product #2";
        Product product = new Product();
        product.setName("Product #2");
        product.setShortDescription("Product two short description.");
        product.setLongDescription("This is a very long description of product #2.");
        product.setPrice(10.56);
        Assertions.assertEquals(productService.getProductByName(name).getName(),
                product.getName());
        Assertions.assertEquals(productService.getProductByName(name).getShortDescription(),
                product.getShortDescription());
        Assertions.assertEquals(productService.getProductByName(name).getLongDescription(),
                product.getLongDescription());
        Assertions.assertEquals(productService.getProductByName(name).getPrice(),
                product.getPrice());
    }

    @Test
    @Transactional
    public void testCreateProduct(){
        Product product = productService.createProduct("Product #6",
                4.60,"Product six short description.",
                "This is a very long description of product #6.", 15);
        Assertions.assertEquals(product, productService.getProduct(6L));
        Assertions.assertEquals(inventoryDAO.findByProduct(product).get()
                .getQuantity(), 15);
    }

    @Test
    @Transactional
    public void testUpdateProductQuantity(){
        productService.updateProductQuantity(3L, 8);
        Assertions.assertEquals(inventoryDAO.findByProduct(productService.getProduct(3L))
                .get().getQuantity(), 8);
    }
}
