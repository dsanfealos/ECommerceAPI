package com.ecommerce.ECommerceAPI.api.controller.product;

import com.ecommerce.ECommerceAPI.model.Inventory;
import com.ecommerce.ECommerceAPI.model.Product;
import com.ecommerce.ECommerceAPI.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getProducts(){
        return productService.getProducts();
    }

    //OK
    @GetMapping("/{productId}")
    public Product getProduct(@PathVariable Long productId){
        return productService.getProduct(productId);
    }

    //OK
    @GetMapping("/search")
    public Product getProductByName(@RequestParam String name){
        return productService.getProductByName(name);
    }

    //ADMIN - OK
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Map<String, String> json){
        Product product = productService.createProduct(json.get("name"), Double.valueOf(json.get("price")),
                json.get("shortDesc"), json.get("longDesc"),
                Integer.valueOf(json.get("quantity")));
        return ResponseEntity.ok(product);
    }

    //OK
    @PutMapping
    public ResponseEntity<Inventory> updateProductQuantity(@RequestBody Map<String,String> json){
        Inventory inventory = productService.updateProductQuantity(Long.valueOf(json.get("id")),
                Integer.valueOf(json.get("quantity")));
        return ResponseEntity.ok(inventory);
    }

    //ADMIN - OK
    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return "Product deleted successfully";
    }
}
