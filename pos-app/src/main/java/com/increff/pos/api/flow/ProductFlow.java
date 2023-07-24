package com.increff.pos.api.flow;

import com.increff.pos.api.BrandApi;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class ProductFlow {

    @Autowired
    private BrandApi brandApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private ProductApi productApi;

    public BrandPojo getBrandById(int id) throws ApiException {
            return brandApi.getCheck(id);
    }

    public int getIdByBrandCategory(String brand, String category) throws ApiException{
            BrandPojo p = brandApi.getCheck(brand, category) ;
            return p.getId();
    }

    public void initInventory(int id) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setId(id);
        inventoryPojo.setQuantity(0);
        inventoryApi.add(inventoryPojo);
    }
}
