package com.increff.pos.service.util;

import com.increff.pos.model.form.*;

public class ConstructorUtil {
    public static BrandForm createBrandForm(String name, String category){
        BrandForm form = new BrandForm();
        form.setBrand(name);
        form.setCategory(category);
        return form;
    }

    public static ProductForm createProductForm(String name, String brand, String category, String barcode, String mrp){
        ProductForm form  = new ProductForm();
        form.setName(name);
        form.setBrand(brand);
        form.setCategory(category);
        form.setBarcode(barcode);
        form.setMrp(mrp);
        return form;
    }

    public static InventoryForm createInventoryForm(String quantity){
        InventoryForm form = new InventoryForm();
        form.setQuantity(quantity);
        return form;
    }

    public static OrderItemForm createCurrentOrderForm(String barcode, String quantity, String sellingPrice){
        OrderItemForm form = new OrderItemForm();
        form.setBarcode(barcode);
        form.setQuantity(quantity);
        form.setSellingPrice(sellingPrice);
        return form;
    }

    public static CustomerForm createCustomerForm(String name, String email, String phone){
        CustomerForm form = new CustomerForm();
        form.setCustomerName(name);
        form.setEmail(email);
        form.setPhone(phone);
        return form;
    }

    public static UserForm createUserForm(String email, String password){
        UserForm form = new UserForm();
        form.setEmail(email);
        form.setPassword(password);
        return form;
    }

    public static LoginForm createLoginForm(String email, String password){
        LoginForm form = new LoginForm();
        form.setEmail(email);
        form.setPassword(password);
        return form;
    }

    public static SalesReportForm createSalesReportForm(String brand, String category, String startDate, String endDate){
        SalesReportForm form = new SalesReportForm();
        form.setBrand(brand);
        form.setCategory(category);
        form.setStartDate(startDate);
        form.setEndDate(endDate);

        return form;
    }
}
