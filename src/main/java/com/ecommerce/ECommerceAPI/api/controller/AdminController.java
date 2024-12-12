package com.ecommerce.ECommerceAPI.api.controller;

import com.ecommerce.ECommerceAPI.model.LocalUser;
import com.ecommerce.ECommerceAPI.model.Product;
import com.ecommerce.ECommerceAPI.model.WebOrder;
import com.ecommerce.ECommerceAPI.model.dao.LocalUserDAO;
import com.ecommerce.ECommerceAPI.service.OrderService;
import com.ecommerce.ECommerceAPI.service.ProductService;
import com.ecommerce.ECommerceAPI.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private OrderService orderService;
    private UserService userService;
    private ProductService productService;
    private LocalUserDAO localUserDAO;

    public AdminController(OrderService orderService, UserService userService,
                           ProductService productService, LocalUserDAO localUserDAO) {
        this.orderService = orderService;
        this.userService = userService;
        this.productService = productService;
        this.localUserDAO = localUserDAO;
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<WebOrder> getOrderAsAdmin(@PathVariable Long orderId){
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<LocalUser> getLocalUser(@PathVariable Long userId){
        Optional<LocalUser> user = localUserDAO.findById(userId);
        if (user.isPresent()) return ResponseEntity.ok(user.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/user/search")
    public ResponseEntity<LocalUser> getLocalUserByUsername(@RequestParam String username){
        Optional<LocalUser> user = localUserDAO.findByUsernameIgnoreCase(username);
        if (user.isPresent()) return ResponseEntity.ok(user.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/product/{productId}")
    public String deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return "Product deleted successfully";
    }

    @PostMapping("/product")
    public ResponseEntity<Product> createProduct(@RequestBody Map<String, String> json){
        Product product = productService.createProduct(json.get("name"), Double.valueOf(json.get("price")),
                json.get("shortDesc"), json.get("longDesc"),
                Integer.valueOf(json.get("quantity")));
        return ResponseEntity.ok(product);
    }
}
