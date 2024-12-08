package com.ecommerce.ECommerceAPI.service;

import com.ecommerce.ECommerceAPI.api.model.OrderBody;
import com.ecommerce.ECommerceAPI.model.LocalUser;
import com.ecommerce.ECommerceAPI.model.Product;
import com.ecommerce.ECommerceAPI.model.WebOrder;
import com.ecommerce.ECommerceAPI.model.WebOrderQuantities;
import com.ecommerce.ECommerceAPI.model.dao.AddressDAO;
import com.ecommerce.ECommerceAPI.model.dao.WebOrderDAO;
import com.ecommerce.ECommerceAPI.model.dao.WebOrderQuantitiesDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderSerice {

    private WebOrderDAO webOrderDAO;
    private WebOrderQuantitiesDAO webOrderQuantitiesDAO;
    private ProductService productService;
    private AddressDAO addressDAO;

    public OrderSerice(WebOrderDAO webOrderDAO, WebOrderQuantitiesDAO webOrderQuantitiesDAO, ProductService productService, AddressDAO addressDAO) {
        this.webOrderDAO = webOrderDAO;
        this.webOrderQuantitiesDAO = webOrderQuantitiesDAO;
        this.productService = productService;
        this.addressDAO = addressDAO;
    }

    public List<WebOrder> getOrders(LocalUser user){
        return webOrderDAO.findByUser(user);
    }

    public WebOrder getOrder(Long id){
        return webOrderDAO.findById(id).get();
    }

    //¿Meter los objetos address y quantities enteros o solo los integers? - Mejorable

    public WebOrder createOrder(OrderBody body, LocalUser user) throws IOException {
        WebOrder order = new WebOrder();
        order.setUser(user);
        order.setAddress(addressDAO.findById(body.getAddressId()).get());
        Map<Long, Integer> quantities = body.getQuantities();
        webOrderDAO.save(order);
        for(Long productId : quantities.keySet()){
            Product product = productService.getProduct(productId);
            WebOrderQuantities webOrderQuantities = new WebOrderQuantities();
            if (product.getInventory().getQuantity() < quantities.get(productId)){
                return null;
            }
            webOrderQuantities.setQuantity(quantities.get(productId));
            webOrderQuantities.setProduct(product);
            webOrderQuantities.setOrder(order);
            webOrderQuantitiesDAO.save(webOrderQuantities);
        }
        return order;
    }

//    public WebOrder createOrder(OrderBody body, LocalUser user){
//        WebOrder order = new WebOrder();
//        order.setUser(user);
//        order.setAddress(body.getAddress());
//        int size = body.getQuantities().size();
//        for (int i = 0; i < size; i++){
//            WebOrderQuantitiesDAO orderQuantities = body.getQuantities().get(i);
//            Product product = orderQuantities.getProduct();
//
//            if (orderQuantities.getQuantity() >
//                    product.getInventory().getQuantity()){
//                return null;
//            }
//        }
//        order.setQuantities(body.getQuantities());
//        return webOrderDAO.save(order);
//    }

    public void deleteOrder(Long orderId){
        webOrderDAO.deleteById(orderId);
    }
}
