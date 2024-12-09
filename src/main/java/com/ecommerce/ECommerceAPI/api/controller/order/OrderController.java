package com.ecommerce.ECommerceAPI.api.controller.order;

import com.ecommerce.ECommerceAPI.api.model.OrderBody;
import com.ecommerce.ECommerceAPI.model.LocalUser;
import com.ecommerce.ECommerceAPI.model.WebOrder;
import com.ecommerce.ECommerceAPI.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<WebOrder> getOrders(@AuthenticationPrincipal LocalUser user){
        return orderService.getOrders(user);
    }

    //OK - LOGGEDUSER
    @GetMapping("/{orderId}")
    public ResponseEntity<WebOrder> getOrder(@PathVariable Long orderId, @AuthenticationPrincipal LocalUser user){
        WebOrder order = orderService.getOrder(orderId);
        if (order.getUser().getId() == user.getId()){
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    //ADMIN - OK
    @GetMapping("/admin/{orderId}")
    public ResponseEntity<WebOrder> getOrderAsAdmin(@PathVariable Long orderId){
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    //OK?
    @PostMapping
    public WebOrder createOrder(@RequestBody OrderBody body,
                                                @AuthenticationPrincipal LocalUser user){
        return orderService.createOrder(body, user);
    }

    //OK
    @DeleteMapping("/{orderId}")
    public ResponseEntity<WebOrder> deleteOrder(@PathVariable Long orderId,
                                                @AuthenticationPrincipal LocalUser user){
        if (orderService.getOrder(orderId).getUser().getId() == user.getId()){
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
