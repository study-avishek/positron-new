package com.increff.pos.api.flow;

import com.increff.pos.api.BrandApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.TsvUtil.createRow;

@Service
@Transactional
public class InventoryFlow {
    @Autowired
    private ProductApi productApi;
    @Autowired
    private BrandApi brandApi;

    public String getBarcode(int id) throws ApiException{
        ProductPojo p = productApi.getCheck(id);
        return p.getBarcode();
    }

    public String getProd(int id) throws ApiException {
        ProductPojo p = productApi.getCheck(id);
        return p.getName();
    }

    public String getBrand(int id) throws ApiException {
        ProductPojo prodP =productApi.getCheck(id);
        BrandPojo brandP = brandApi.getCheck(prodP.getBrandCatId());
        return brandP.getBrand();
    }

    public String getCategory(int id) throws ApiException{
        ProductPojo prodP = productApi.getCheck(id);
        BrandPojo brandP = brandApi.getCheck(prodP.getBrandCatId());
        return brandP.getCategory();
    }

    public List<InventoryPojo> convert(List<InventoryForm> inventoryForms, List<String> barcodes, List<List<String>> errors) throws ApiException {
        List<InventoryPojo> inventoryPojos = new ArrayList<>();
        for(int i = 0 ; i < barcodes.size() ; i++){
            InventoryPojo inventoryPojo = new InventoryPojo();
            try {
                ProductPojo productPojo = productApi.getCheck(barcodes.get(i));
                inventoryPojo.setId(productPojo.getId());
            }
            catch (ApiException e){
                errors.add(createRow(i+1, barcodes.get(i),"Product barcode does not exist"));
            }
            inventoryPojo.setQuantity(Integer.parseInt(inventoryForms.get(i).getQuantity()));

            inventoryPojos.add(inventoryPojo);
        }
        return inventoryPojos;
    }

}
