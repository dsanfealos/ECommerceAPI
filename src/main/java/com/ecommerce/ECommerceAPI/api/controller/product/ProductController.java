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

    //OK
    @PutMapping
    public ResponseEntity<Inventory> updateProductQuantity(@RequestBody Map<String,String> json){
        Inventory inventory = productService.updateProductQuantity(Long.valueOf(json.get("id")),
                Integer.valueOf(json.get("quantity")));
        return ResponseEntity.ok(inventory);
    }
}
