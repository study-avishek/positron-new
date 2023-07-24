package com.increff.pos.service.util;

import com.increff.pos.dto.*;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.data.OutwardOrderData;
import com.increff.pos.model.form.CustomerForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static com.increff.pos.service.util.ConstructorUtil.*;
import static junit.framework.TestCase.assertEquals;

@Component
public class OrderUtil {
    @Autowired
    private OutwardOrderDto outwardOrderDto;

    @Autowired
    private OrderItemDto orderItemDto;

    @Autowired
    private BrandDto brandDto;

    @Autowired
    private ProductDto productDto;

    @Autowired
    private InventoryDto inventoryDto;


    public void createProduct(int n) throws ApiException {
        for(int i = 0 ; i < n ; i++){
            brandDto.add(createBrandForm("brand"+i, "category"+i));;
            int prodId = productDto.add(createProductForm("product"+i, "brand"+i, "category"+i, "barcode"+i, ""+(1000+i*100)));
            inventoryDto.update(prodId, createInventoryForm("100"));
        }
    }

    public void createOrder(int n) throws ApiException{
        for(int i = 0; i < n ; i++){
            int orderId = outwardOrderDto.add().getId();
            for(int j = 0 ; j  < n ; j++){
                orderItemDto.add(orderId, createCurrentOrderForm("barcode"+j, "10", ""+(900+j*100)));
            }
            List<OrderItemData> orderItemList = orderItemDto.getAll(orderId);
            assertEquals(n, orderItemList.size());
        }
    }

    // delete first n orders from orderList
    public void deleteOrder(List<OutwardOrderData> orderLists, int n) throws ApiException{
        for(int i = 0 ; i < n ; i++){
            outwardOrderDto.delete(orderLists.get(i).getId());
        }
    }

    //completes all the orders in the order list
    public void completeOrder(List<OutwardOrderData> orderList) throws ApiException, IOException {
        for (OutwardOrderData outwardOrderData : orderList) {
            CustomerForm customerForm = createCustomerForm("Test Name", "test@email.com", "1234567890");
            outwardOrderDto.complete(outwardOrderData.getId(), customerForm);
        }
    }
}
