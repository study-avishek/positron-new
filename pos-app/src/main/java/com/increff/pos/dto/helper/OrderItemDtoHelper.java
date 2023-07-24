package com.increff.pos.dto.helper;

import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.util.StringUtil;

import static com.increff.pos.util.StringUtil.toLowerCase;
import static com.increff.pos.util.StringUtil.trimZeros;

public class OrderItemDtoHelper {

    public static void normalize(OrderItemForm form){
        form.setBarcode(toLowerCase(form.getBarcode()));
        form.setQuantity(trimZeros(form.getQuantity()));
        form.setSellingPrice(trimZeros(form.getSellingPrice()));
    }

    public static OrderItemPojo convert(OrderItemForm form, int id, int prodId, Double mrp){
        OrderItemPojo p = new OrderItemPojo();
        p.setOrderId(id);
        p.setQuantity(Integer.parseInt(form.getQuantity()));
        p.setProdId(prodId);
        p.setSellingPrice(Double.parseDouble(form.getSellingPrice()));
        return p;
    }

    public static OrderItemData convert(OrderItemPojo p, String barcode, String productBrandName, Double mrp) {
        OrderItemData data = new OrderItemData();
        data.setBarcode(barcode);
        data.setProductBrandName(productBrandName);
        data.setMrp(mrp);
        data.setItemTotal(p.getSellingPrice()*p.getQuantity());
        data.setQuantity(String.valueOf(p.getQuantity()));
        data.setSellingPrice(String.valueOf(p.getSellingPrice()));
        data.setProdId(p.getProdId());
        return data;
    }
}
