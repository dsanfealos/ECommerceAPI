package com.ecommerce.ECommerceAPI.service;

import com.ecommerce.ECommerceAPI.api.model.OrderBody;
import com.ecommerce.ECommerceAPI.api.model.OrderQuantityBody;
import com.ecommerce.ECommerceAPI.exception.ProductOutOfStockException;
import com.ecommerce.ECommerceAPI.model.LocalUser;
import com.ecommerce.ECommerceAPI.model.Product;
import com.ecommerce.ECommerceAPI.model.WebOrder;
import com.ecommerce.ECommerceAPI.model.WebOrderQuantities;
import com.ecommerce.ECommerceAPI.model.dao.AddressDAO;
import com.ecommerce.ECommerceAPI.model.dao.WebOrderDAO;
import com.ecommerce.ECommerceAPI.model.dao.WebOrderQuantitiesDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private WebOrderDAO webOrderDAO;
    private WebOrderQuantitiesDAO webOrderQuantitiesDAO;
    private ProductService productService;
    private AddressDAO addressDAO;

    public OrderService(WebOrderDAO webOrderDAO, WebOrderQuantitiesDAO webOrderQuantitiesDAO, ProductService productService, AddressDAO addressDAO) {
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

    public WebOrder createOrder(OrderBody body, LocalUser user) {
        WebOrder order = new WebOrder();
        order.setUser(user);
        order.setAddress(addressDAO.findById(body.getAddressId()).get());
        List<OrderQuantityBody> selections = body.getQuantities();
        webOrderDAO.save(order);
        for (OrderQuantityBody selection: selections){
            Product product = productService.getProduct(selection.getProductId());
            int orderedQuantity = selection.getQuantity();
            WebOrderQuantities webOrderQuantities = new WebOrderQuantities();
            if (orderedQuantity > product.getInventory().getQuantity()){
                throw new ProductOutOfStockException("The amount of quantity ordered of product " + product.getName() + " is bigger than the stock");
            }
            webOrderQuantities.setQuantity(orderedQuantity);
            webOrderQuantities.setProduct(product);
            webOrderQuantities.setOrder(order);
            webOrderQuantitiesDAO.save(webOrderQuantities);
        }
        return order;
    }

    public void deleteOrder(Long orderId){
        webOrderDAO.deleteById(orderId);
    }
}
