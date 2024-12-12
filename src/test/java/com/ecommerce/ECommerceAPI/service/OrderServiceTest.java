package com.ecommerce.ECommerceAPI.service;

import com.ecommerce.ECommerceAPI.api.model.OrderBody;
import com.ecommerce.ECommerceAPI.api.model.OrderQuantityBody;
import com.ecommerce.ECommerceAPI.exception.ProductOutOfStockException;
import com.ecommerce.ECommerceAPI.model.Address;
import com.ecommerce.ECommerceAPI.model.LocalUser;
import com.ecommerce.ECommerceAPI.model.WebOrder;
import com.ecommerce.ECommerceAPI.model.WebOrderQuantities;
import com.ecommerce.ECommerceAPI.model.dao.AddressDAO;
import com.ecommerce.ECommerceAPI.model.dao.LocalUserDAO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private LocalUserDAO localUserDAO;

    @Autowired
    private AddressDAO addressDAO;

    @Test
    @Transactional
    public void testGetOrder(){
        LocalUser user1 = localUserDAO.findById(1L).get();
        Address address = addressDAO.findById(1L).get();
        WebOrder order = orderService.getOrder(user1.getWebOrders().get(0).getId());
        WebOrder reference = new WebOrder();
        reference.setUser(user1);
        reference.setAddress(address);

        WebOrderQuantities webOrderQuantities1= new WebOrderQuantities();
        WebOrderQuantities webOrderQuantities2= new WebOrderQuantities();
        webOrderQuantities1.setOrder(order);
        webOrderQuantities2.setOrder(order);
        webOrderQuantities1.setProduct(productService.getProduct(1L));
        webOrderQuantities2.setProduct(productService.getProduct(2L));
        webOrderQuantities1.setQuantity(5);
        webOrderQuantities2.setQuantity(5);

        Assertions.assertEquals(order.getUser(), reference.getUser());
        Assertions.assertEquals(order.getAddress(), reference.getAddress());
        Assertions.assertEquals(order.getQuantities().get(0).getOrder(),
                webOrderQuantities1.getOrder());
        Assertions.assertEquals(order.getQuantities().get(1).getOrder(),
                webOrderQuantities2.getOrder());
        Assertions.assertEquals(order.getQuantities().get(0).getQuantity(),
                webOrderQuantities1.getQuantity());
        Assertions.assertEquals(order.getQuantities().get(1).getQuantity(),
                webOrderQuantities2.getQuantity());
        Assertions.assertEquals(order.getQuantities().get(0).getProduct().getId(),
                webOrderQuantities1.getProduct().getId());
        Assertions.assertEquals(order.getQuantities().get(1).getProduct().getId(),
                webOrderQuantities2.getProduct().getId());
    }


    @Test
    @Transactional
    public void testGetOrders(){
        LocalUser user1 = localUserDAO.findById(1L).get();

        Assertions.assertTrue(Objects.equals(orderService.getOrders(user1),
                List.of(orderService.getOrder(1L),
                        orderService.getOrder(2L),
                        orderService.getOrder(3L))));
    }

    @Test
    @Transactional
    public void testCreateOrder(){
        LocalUser user1 = localUserDAO.findById(1L).get();
        OrderBody body = new OrderBody();

        OrderQuantityBody orderQuantityBody1 = new OrderQuantityBody();
        OrderQuantityBody orderQuantityBody2 = new OrderQuantityBody();
        orderQuantityBody1.setProductId(3L);
        orderQuantityBody2.setProductId(4L);
        orderQuantityBody1.setQuantity(7);
        orderQuantityBody2.setQuantity(30);
        body.setAddressId(2L);
        body.setQuantities(List.of(orderQuantityBody1, orderQuantityBody2));
        WebOrder order = orderService.createOrder(body, user1);

        Assertions.assertEquals(order, orderService.getOrder(6L));
        orderQuantityBody1.setQuantity(70);
        body.setQuantities(List.of(orderQuantityBody1, orderQuantityBody2));

        Assertions.assertThrows(ProductOutOfStockException.class,
                () -> orderService.createOrder(body, user1),
                "Order quantity should be bigger than Inventory quantity.");

    }

    @Test
    @Transactional
    public void testDeleteOrder(){
        orderService.deleteOrder(2L);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> orderService.getOrder(2L), "Order should be deleted");
    }
}
