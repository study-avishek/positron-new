package com.increff.invoice.service.util;

import com.increff.invoice.model.data.InvoiceItemData;
import com.increff.invoice.model.form.InvoiceForm;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


public class constructorUtil {
    public static InvoiceForm createInvoiceForm(int n){
        InvoiceForm form = new InvoiceForm();
        form.setCustomerName("test name");
        form.setEmail("test@test.com");
        form.setPhone("1234567890");
        form.setInvoiceNumber(1234);
        form.setTimestamp("01-01-1900");
        List<InvoiceItemData> invoiceItemDataList = new ArrayList<>();
        for(int i = 0 ; i < n ; i++){
            InvoiceItemData invoiceItemData = new InvoiceItemData();
            invoiceItemData.setItemNumber(i+1);
            invoiceItemData.setMrp((double) (100 + i*10));
            invoiceItemData.setQuantity(i+1);
            invoiceItemData.setSellingPrice((double) (95+i*10));
            invoiceItemData.setName("test product "+i);
            invoiceItemDataList.add(invoiceItemData);
        }
        form.setInvoiceItemList(invoiceItemDataList);
        return form;
    }
}
