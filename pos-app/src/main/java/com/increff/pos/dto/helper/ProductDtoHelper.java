package com.increff.pos.dto.helper;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StringUtil;
import java.util.*;
import java.util.stream.Collectors;

import static com.increff.pos.util.TsvUtil.createRow;

public class ProductDtoHelper {

    public static ProductData convert(ProductPojo p, String brand, String category){
        ProductData d = new ProductData();

        d.setId(p.getId());
        d.setName(p.getName());
        d.setMrp(String.valueOf(p.getMrp()));
        d.setBarcode(p.getBarcode());
        d.setBrand(brand);
        d.setCategory(category);

        return d;
    }

    public static  ProductPojo convert(ProductForm f, int brandCatId) {
        ProductPojo p = new ProductPojo();

        p.setName(f.getName());
        p.setBarcode(f.getBarcode());
        p.setMrp(Double.parseDouble(f.getMrp()));
        p.setBrandCatId(brandCatId);

        return p;
    }

    public static void normalize(ProductForm p){
        p.setName(StringUtil.toLowerCase(p.getName()));
        p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
        p.setBrand(StringUtil.toLowerCase(p.getBrand()));
        p.setCategory(StringUtil.toLowerCase(p.getCategory()));
        p.setMrp(StringUtil.trimZeros(p.getMrp()));
    }

    public static ProductForm createProductForm(String[] columns, HashMap<String, Integer> map, List<List<String>> error, int i){
        ProductForm form = new ProductForm();

        form.setName(columns[map.get("name")]);
        form.setBrand(columns[map.get("brand")]);
        form.setCategory(columns[map.get("category")]);
        form.setBarcode(columns[map.get("barcode")]);
        form.setMrp(columns[map.get("mrp")]);

        return form;
    }
}

