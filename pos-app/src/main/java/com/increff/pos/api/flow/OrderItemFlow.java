package com.increff.pos.api.flow;

import com.increff.pos.api.*;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.enums.OrderStatus;
import com.increff.pos.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class OrderItemFlow {

    @Autowired
    private ProductApi productApi;
    @Autowired
    private BrandApi brandApi;
    @Autowired
    private InventoryApi inventoryApi;

    @Autowired OutwardOrderApi outwardOrderApi;

    public int getId(String barcode) throws ApiException {
        ProductPojo p = productApi.getCheck(barcode);
        return p.getId();
    }

    public String getBarcode(int prodId) throws ApiException{
        ProductPojo p = productApi.getCheck(prodId);
        return p.getBarcode();
    }

    public String getProductBrandName(int prodId) throws ApiException{
        ProductPojo productPojo = productApi.getCheck(prodId);
        BrandPojo brandPojo = brandApi.getCheck(productPojo.getBrandCatId());
        return productPojo.getName() + " " + brandPojo.getBrand();
    }

    public Double getMrp(int prodId) throws ApiException {
        ProductPojo p = productApi.getCheck(prodId);
        return p.getMrp();
    }

    public int getQuantity(String barcode) throws ApiException{
        ProductPojo productPojo = productApi.getCheck(barcode);
        InventoryPojo inventoryPojo = inventoryApi.getCheck(productPojo.getId());
        return inventoryPojo.getQuantity();
    }

    public void checkPrice(OrderItemPojo p) throws ApiException {
        ProductPojo productPojo = productApi.getCheck(p.getProdId());

        if(p.getSellingPrice() > productPojo.getMrp()){
            throw new ApiException("Selling price cannot be greater than MRP: " + productPojo.getMrp());
        }
    }

    public void checkOrderId(int orderId) throws ApiException {
        outwardOrderApi.get(orderId);
    }
}
