package com.increff.pos.dto.helper;

import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.util.StringUtil;

import static com.increff.pos.util.StringUtil.trimZeros;


public class InventoryDtoHelper {

    public static InventoryPojo convert(InventoryForm f){
        InventoryPojo p = new InventoryPojo();
        p.setQuantity(Integer.parseInt(f.getQuantity()));
        return p;
    }

    public static InventoryData convert(InventoryPojo p, String prod, String brand, String category, String barcode) {
        InventoryData d = new InventoryData();
        d.setId(p.getId());
        d.setProd(prod);
        d.setBrand(brand);
        d.setCategory(category);
        d.setQuantity(String.valueOf(p.getQuantity()));
        d.setBarcode(barcode);
        return d;
    }

    public static void normalize(InventoryForm form){
        form.setQuantity(trimZeros(form.getQuantity()));
    }
}
