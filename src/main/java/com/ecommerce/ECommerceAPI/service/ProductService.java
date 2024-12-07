package com.ecommerce.ECommerceAPI.service;

import com.ecommerce.ECommerceAPI.model.Inventory;
import com.ecommerce.ECommerceAPI.model.Product;
import com.ecommerce.ECommerceAPI.model.dao.InventoryDAO;
import com.ecommerce.ECommerceAPI.model.dao.ProductDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductDAO productDAO;
    private InventoryDAO inventoryDAO;

    public ProductService(ProductDAO productDAO, InventoryDAO inventoryDAO) {
        this.productDAO = productDAO;
        this.inventoryDAO = inventoryDAO;
    }

    public List<Product> getProducts(){
        return productDAO.findAll();
    }

    public Product getProduct(Long id){
        Optional<Product> product = productDAO.findById(id);
        return product.get();
    }

    public Product getProductByName(String name){
        return productDAO.findByName(name).get();
    }

    public void createProduct(String name, Double price,
                                String shortDesc, String longDesc,
                              Integer quantity){
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setShortDescription(shortDesc);
        product.setLongDescription(longDesc);
        productDAO.save(product);

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(quantity);
        inventoryDAO.save(inventory);
    }

    public String updateProductQuantity(Long id, Integer newQuantity){
        Inventory inventory = inventoryDAO.findByProduct(getProduct(id)).get();
        inventory.setQuantity(newQuantity);
        inventoryDAO.save(inventory);
        return "Quantity updated successfully";
    }

    public void deleteProduct(Long id){
        productDAO.deleteById(id);
    }
}
