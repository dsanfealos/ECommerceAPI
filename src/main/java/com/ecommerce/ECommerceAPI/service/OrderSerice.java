package com.ecommerce.ECommerceAPI.service;

import com.ecommerce.ECommerceAPI.model.LocalUser;
import com.ecommerce.ECommerceAPI.model.WebOrder;
import com.ecommerce.ECommerceAPI.model.dao.WebOrderDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderSerice {

    private WebOrderDAO webOrderDAO;

    public OrderSerice(WebOrderDAO webOrderDAO) {
        this.webOrderDAO = webOrderDAO;
    }

    public List<WebOrder> getOrders(LocalUser user){
        return webOrderDAO.findByUser(user);
    }


}
