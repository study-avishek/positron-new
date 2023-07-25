package com.increff.pos.dto.helper;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BrandDtoHelper {
    public static BrandData convert(BrandPojo p) {
        BrandData d = new BrandData();
        d.setCategory(p.getCategory());
        d.setBrand(p.getBrand());
        d.setId(p.getId());
        return d;
    }

    public static BrandPojo convert(BrandForm f) {
        BrandPojo p = new BrandPojo();
        p.setCategory(f.getCategory());
        p.setBrand(f.getBrand());
        return p;
    }

    public static List<BrandPojo> convert(List<BrandForm> forms) {
        List<BrandPojo> brandPojos = new ArrayList<>();
        for (BrandForm form : forms) {
            BrandPojo p = new BrandPojo();
            p.setBrand(form.getBrand());
            p.setCategory(form.getCategory());
            brandPojos.add(p);
        }
        return brandPojos;
    }

    public static void normalize(BrandForm p) {
        p.setBrand(StringUtil.toLowerCase(p.getBrand()));
        p.setCategory(StringUtil.toLowerCase(p.getCategory()));
    }

    public static BrandForm createBrandForm(String[] columns, HashMap<String, Integer> map) {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(columns[map.get("brand")]);
        brandForm.setCategory(columns[map.get("category")]);
        return brandForm;
    }

}
