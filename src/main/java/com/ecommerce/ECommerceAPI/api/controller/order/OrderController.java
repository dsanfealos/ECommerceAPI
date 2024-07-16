package com.ecommerce.ECommerceAPI.api.controller.order;

import com.ecommerce.ECommerceAPI.model.LocalUser;
import com.ecommerce.ECommerceAPI.model.WebOrder;
import com.ecommerce.ECommerceAPI.service.OrderSerice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
