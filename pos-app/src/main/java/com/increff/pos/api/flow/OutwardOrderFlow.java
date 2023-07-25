package com.increff.pos.api.flow;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.OrderItemApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InvoiceItemData;
import com.increff.pos.model.form.CustomerForm;
import com.increff.pos.model.form.InvoiceForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class OutwardOrderFlow {
    @Autowired
    private OrderItemApi orderItemApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private OrderItemFlow orderItemFlow;

    public void deleteItemsByOrderId(int id) throws ApiException {
        orderItemApi.deleteItemsByOrderId(id);
    }

    public InvoiceForm complete(int id, CustomerForm customerForm) throws ApiException {
        List<OrderItemPojo> orderItemPojos = orderItemApi.getAll(id);
        DecimalFormat df = new DecimalFormat("0.#####");
        if(orderItemPojos.size() == 0) {
            throw new ApiException("Empty order cannot be invoiced");
        }

        InvoiceForm invoiceForm = new InvoiceForm();
        List<InvoiceItemData> list = new ArrayList<>();
        for(OrderItemPojo orderItemPojo : orderItemPojos){
            InventoryPojo inventoryPojo = inventoryApi.getCheck(orderItemPojo.getProdId());
            if(orderItemPojo.getQuantity() > inventoryPojo.getQuantity()){
                throw new ApiException("Inventory updated!desired quantity out of stock, updated stock: "+ Math.max(inventoryPojo.getQuantity(),0));
            }
            else{
                inventoryPojo.setQuantity(inventoryPojo.getQuantity() - orderItemPojo.getQuantity());
                inventoryApi.update(inventoryPojo.getId(), inventoryPojo);
            }
        }

        int i = 1;
        for(OrderItemPojo orderItemPojo : orderItemPojos){
            InvoiceItemData invoiceItemData = new InvoiceItemData();

            ProductPojo productPojo = productApi.getCheck(orderItemPojo.getProdId());

            invoiceItemData.setName(orderItemFlow.getProductBrandName(orderItemPojo.getProdId()));
            invoiceItemData.setItemNumber(df.format(i++));
            invoiceItemData.setMrp(df.format(productPojo.getMrp()));
            invoiceItemData.setQuantity(df.format(orderItemPojo.getQuantity()));
            invoiceItemData.setSellingPrice(df.format(orderItemPojo.getSellingPrice()));

            list.add(invoiceItemData);
        }

        invoiceForm.setInvoiceNumber(id);
        invoiceForm.setEmail(customerForm.getEmail());
        invoiceForm.setPhone(customerForm.getPhone());
        invoiceForm.setInvoiceItemList(list);
        invoiceForm.setCustomerName(customerForm.getCustomerName());
        invoiceForm.setTimestamp(LocalDateTime.now().toString());

        return invoiceForm;
    }
}
