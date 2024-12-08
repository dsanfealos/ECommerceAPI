package com.ecommerce.ECommerceAPI.api.controller.order;

import com.ecommerce.ECommerceAPI.api.model.OrderBody;
import com.ecommerce.ECommerceAPI.model.LocalUser;
import com.ecommerce.ECommerceAPI.model.WebOrder;
import com.ecommerce.ECommerceAPI.service.OrderSerice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderSerice orderSerice;

    public OrderController(OrderSerice orderSerice) {
        this.orderSerice = orderSerice;
    }

    @GetMapping
    public List<WebOrder> getOrders(@AuthenticationPrincipal LocalUser user){
        return orderSerice.getOrders(user);
    }

    //OK - LOGGEDUSER
    @GetMapping("/{orderId}")
    public ResponseEntity<WebOrder> getOrder(@PathVariable Long orderId, @AuthenticationPrincipal LocalUser user){
        WebOrder order = orderSerice.getOrder(orderId);
        if (order.getUser().getId() == user.getId()){
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    //ADMIN - OK
    @GetMapping("/admin/{orderId}")
    public ResponseEntity<WebOrder> getOrderAsAdmin(@PathVariable Long orderId){
        return ResponseEntity.ok(orderSerice.getOrder(orderId));
    }

    //OK?
    @PostMapping
    public WebOrder createOrder(@RequestBody OrderBody body,
                                                @AuthenticationPrincipal LocalUser user) throws IOException {
        return orderSerice.createOrder(body, user);
    }

    //OK
    @DeleteMapping("/{orderId}")
    public ResponseEntity<WebOrder> deleteOrder(@PathVariable Long orderId,
                                                @AuthenticationPrincipal LocalUser user){
        if (orderSerice.getOrder(orderId).getUser().getId() == user.getId()){
            orderSerice.deleteOrder(orderId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
