package com.increff.pos.service.dtoTest;

import com.increff.pos.dto.*;
import com.increff.pos.model.enums.OrderStatus;
import com.increff.pos.exception.ApiException;
import com.increff.pos.exception.UploadException;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.data.OutwardOrderData;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.util.OrderUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static com.increff.pos.service.util.ConstructorUtil.*;
import static junit.framework.TestCase.assertEquals;

public class OrderDtoTest extends AbstractUnitTest {
    @Autowired
    private BrandDto brandDto;

    @Autowired
    private ProductDto productDto;

    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private OrderItemDto orderItemDto;

    @Autowired
    private OutwardOrderDto outwardOrderDto;

    @Autowired
    private OrderUtil orderUtil;

    //Test create, delete, complete order
    @Test
    public void testOrder() throws ApiException, IOException {
        // Create 5 brand-category and product
        orderUtil.createProduct(5);

        //Create 5 orders and add all the 5 items to it
        orderUtil.createOrder(5);

        //Check if all the 5 orders are created
        List<OutwardOrderData> orderLists = outwardOrderDto.getAll();

        for(OutwardOrderData outwardOrderData: orderLists){
            assertEquals(OrderStatus.ACTIVE, outwardOrderData.getOrderStatus());
        }
        assertEquals(5, orderLists.size());


        // Delete 3 orders
        orderUtil.deleteOrder(orderLists, 3);

        //check if 3 orders are deleted
        orderLists = outwardOrderDto.getAll();
        assertEquals(2, orderLists.size());

        // complete order
        orderUtil.completeOrder(orderLists);
    }

    //Test delete order items of an order
    @Test
    public void testDeleteOrderItem() throws ApiException{
        orderUtil.createProduct(5);
        orderUtil.createOrder(5);
        List<OutwardOrderData> orderLists = outwardOrderDto.getAll();
        for(OutwardOrderData orderData: orderLists){
            List<OrderItemData> list = orderItemDto.getAll(orderData.getId());
            for(OrderItemData orderItemData : list){
                orderItemDto.delete(orderData.getId(), orderItemData.getProdId());
            }
            list = orderItemDto.getAll(orderData.getId());
            assertEquals(0, list.size());
        }
    }


    //Test update order items of an order
    @Test
    public void testUpdateOrderItem() throws ApiException{
        orderUtil.createProduct(1);
        orderUtil.createOrder(1);

        int orderId = outwardOrderDto.getAll().get(0).getId();
        int prodId = orderItemDto.getAll(orderId).get(0).getProdId();

        OrderItemForm currentOrderData = orderItemDto.get(orderId, prodId);
        assertEquals("900.0", currentOrderData.getSellingPrice());

        OrderItemForm orderItemForm = createCurrentOrderForm("barcode0", "10", "800.00");
        orderItemDto.update(orderId, orderItemForm);

        currentOrderData = orderItemDto.get(orderId, prodId);
        assertEquals("800.0", currentOrderData.getSellingPrice());
    }


    //Test inventory out of stock
    @Test(expected = ApiException.class)
    public void testOutOfStock() throws ApiException{
        orderUtil.createProduct(1);
        int orderId = outwardOrderDto.add().getId();
        orderItemDto.add(orderId, createCurrentOrderForm("barcode0", "101", "1000.00"));
    }

    //Test selling price more than mrp
    @Test(expected = ApiException.class)
    public void testSellingPriceGreaterThanMrp() throws ApiException{
        orderUtil.createProduct(1);
        int orderId = outwardOrderDto.add().getId();
        orderItemDto.add(orderId, createCurrentOrderForm("barcode0", "10", "10000.00"));
    }


    //Test add existing item
    @Test
    public void testAddExistingItem() throws ApiException{
        orderUtil.createProduct(1);
        int orderId = outwardOrderDto.add().getId();
        orderItemDto.add(orderId, createCurrentOrderForm("barcode0", "10", "1000.00"));
        orderItemDto.add(orderId, createCurrentOrderForm("barcode0", "10", "1000.00"));
        int prodId = orderItemDto.getAll(orderId).get(0).getProdId();
        assertEquals("20", orderItemDto.get(orderId, prodId).getQuantity());
    }

    //Test wrong barcode entered while creating order
    @Test(expected = ApiException.class)
    public void testAddInvalidBarcode() throws ApiException{
        orderUtil.createProduct(1);
        int orderId = outwardOrderDto.add().getId();
        orderItemDto.add(orderId, createCurrentOrderForm("barcode1", "10", "1000.00"));
    }

    //Test add locked inventory out of stock
    @Test(expected = ApiException.class)
    public void testAddLockedOutOfStock() throws ApiException{
        orderUtil.createProduct(1);
        int orderId1 = outwardOrderDto.add().getId();
        int orderId2 = outwardOrderDto.add().getId();

        orderItemDto.add(orderId1, createCurrentOrderForm("barcode0", "60", "1000.00"));
        orderItemDto.add(orderId2, createCurrentOrderForm("barcode0", "60", "1000.00"));
    }

    @Test(expected = ApiException.class)
    public void testUpdateOutOfStock() throws ApiException{
        orderUtil.createProduct(1);
        orderUtil.createOrder(1);
        int orderId = outwardOrderDto.getAll().get(0).getId();
        OrderItemForm orderItemForm = createCurrentOrderForm("barcode0", "200", "1000.00");
        orderItemDto.update(orderId, orderItemForm);
    }

    @Test(expected = ApiException.class)
    public void testUpdateOutOfStockLockedInventory() throws ApiException{
        orderUtil.createProduct(2);
        orderUtil.createOrder(2);
        int orderId1 = outwardOrderDto.getAll().get(0).getId();
        int orderId2 = outwardOrderDto.getAll().get(1).getId();
        OrderItemForm orderItemForm = createCurrentOrderForm("barcode0", "95", "1000.00");
        orderItemDto.update(orderId1, orderItemForm);
    }

    @Test(expected = ApiException.class)
    public void testInvalidCustomerForm() throws ApiException, IOException {
        orderUtil.createProduct(1);
        outwardOrderDto.add();
        int orderId = outwardOrderDto.getAll().get(0).getId();
        outwardOrderDto.complete(orderId, createCustomerForm("Avishek", "wrongemail","1234"));
    }

    @Test(expected = ApiException.class)
    public void testInventoryOutOfStockDuringOrder() throws ApiException, IOException {
        brandDto.add(createBrandForm("test brand", "test category"));
        int prodId = productDto.add(createProductForm("test product","test brand","test category","testbarcode","1000.00"));
        inventoryDto.update(prodId, createInventoryForm("100"));
        int orderId = outwardOrderDto.add().getId();
        orderItemDto.add(orderId, createCurrentOrderForm("testbarcode","100","1000.00"));
        inventoryDto.update(prodId, createInventoryForm("50"));
        outwardOrderDto.complete(orderId, createCustomerForm("Avishek","test@test.com", "1234567890"));
    }

    @Test
    public void testTsvUploadValid() throws ApiException, IOException, UploadException {
        orderUtil.createProduct(5);
        FileInputStream tsv = new FileInputStream(new File("src/test/resources/tsv/test_order_valid.tsv"));
        MultipartFile multipartFile = new MockMultipartFile("test.tsv", tsv);
        int id = outwardOrderDto.add().getId();
        orderItemDto.tsvUpload(id, multipartFile);
        List<OrderItemData> dataList = orderItemDto.getAll(id);
        assertEquals(5, dataList.size());
    }

    @Test(expected = UploadException.class)
    public void testTsvUploadInvalidFile() throws ApiException, IOException, UploadException {
        orderUtil.createProduct(5);
        FileInputStream tsv = new FileInputStream(new File("src/test/resources/tsv/test_invalid.tsv"));
        MultipartFile multipartFile = new MockMultipartFile("test.tsv", tsv);
        int id = outwardOrderDto.add().getId();
        orderItemDto.tsvUpload(id, multipartFile);
    }

    @Test(expected = UploadException.class)
    public void testTsvUploadInvalidFileFormat() throws ApiException, IOException, UploadException {
        orderUtil.createProduct(5);
        FileInputStream tsv = new FileInputStream(new File("src/test/resources/tsv/test_invalid_format"));
        MultipartFile multipartFile = new MockMultipartFile("test.tsv", tsv);
        int id = outwardOrderDto.add().getId();
        orderItemDto.tsvUpload(id, multipartFile);
    }

    @Test(expected = UploadException.class)
    public void testTsvUploadInvalidData() throws ApiException, IOException, UploadException {
        orderUtil.createProduct(1);
        FileInputStream tsv = new FileInputStream(new File("src/test/resources/tsv/test_order_invalid.tsv"));
        MultipartFile multipartFile = new MockMultipartFile("test.tsv", tsv);
        int id = outwardOrderDto.add().getId();
        orderItemDto.tsvUpload(id, multipartFile);
    }



}
