package com.increff.pos.dto;

import com.increff.pos.api.OrderItemApi;
import com.increff.pos.api.flow.OrderItemFlow;
import com.increff.pos.exception.ApiException;
import com.increff.pos.exception.UploadException;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.increff.pos.dto.helper.OrderItemDtoHelper.convert;
import static com.increff.pos.util.StringUtil.toLowerCase;
import static com.increff.pos.util.StringUtil.trimZeros;
import static com.increff.pos.util.TsvUtil.createRow;
import static com.increff.pos.util.TsvUtil.parseHeader;
import static com.increff.pos.util.ValidationUtil.checkValid;
import static com.increff.pos.util.ValidationUtil.checkValidMultiple;

@Component
public class OrderItemDto {
    @Autowired
    private OrderItemApi api;
    @Autowired
    private OrderItemFlow flow;

    public void add(int orderId, OrderItemForm form) throws ApiException {
        flow.checkOrderId(orderId);
        normalize(form);
        checkValid(form);
        int prodId = flow.getId(form.getBarcode());
        OrderItemPojo p = convert(form, orderId, prodId, flow.getMrp(prodId));
        checkPrice(p);
        OrderItemPojo existing = api.getCheck(orderId, p.getProdId());

        int neededQuantity = p.getQuantity() + ((existing != null) ? existing.getQuantity() : 0);
        int effectiveInventory = getInventorySize(p.getProdId()) - api.lockedInventory(p.getProdId());

        if (p.getQuantity() > effectiveInventory) {
            throw new ApiException("Total available quantity: " + Math.max(effectiveInventory,0));
        } else if(existing != null){
            p.setQuantity(neededQuantity);
            api.update(orderId, p);
        }
        else{
            api.add(p);
        }
    }

    public void addAll(int id,List<OrderItemForm> forms, List<List<String>> errors){
        int i = 1;
        for(OrderItemForm form: forms){
            try{
                add(id, form);
            }
            catch (ApiException e){
                errors.add(createRow(i, "Barcode: " + form.getBarcode() + "\n" +
                        "Quantity: " + form.getQuantity() + "\n" +
                        "Selling Price: " + form.getSellingPrice() + "\n", e.getMessage()));
            }
            i++;
        }
    }

    @Transactional(rollbackOn = UploadException.class)
    public void tsvUpload(int id, MultipartFile file) throws UploadException {
        List<List<String>> errors = new ArrayList<>();
        try {
            List<OrderItemForm> forms = tsvToCurrentOrderForm(file, errors);
            if(errors.size() > 0) throw new UploadException("Order upload failed", errors);
            checkValidMultiple(forms, errors);
            if(errors.size() > 0) throw new UploadException("Order upload failed", errors);
            addAll(id, forms, errors);
            if(errors.size() > 0) throw new UploadException("Order upload failed", errors);
        }
        catch (IOException e){
            throw new UploadException("Something went wrong! make sure tsv contains less than 5000 lines");
        }

    }

    public OrderItemData get(int id, int prodId) throws ApiException {
        OrderItemPojo p = api.getCheck(id, prodId);
        return convert(p, flow.getBarcode(prodId), flow.getProductBrandName(prodId), flow.getMrp(prodId));
    }

    public List<OrderItemData> getAll(int id) throws ApiException {
        List<OrderItemPojo> list = api.getAll(id);
        List<OrderItemData> list2 = new ArrayList<>();
        for (OrderItemPojo p : list) {
            int prodId = p.getProdId();
            list2.add(convert(p, flow.getBarcode(prodId), flow.getProductBrandName(prodId), flow.getMrp(prodId)));
        }
        return list2;
    }

    public void update(int id, OrderItemForm form) throws ApiException {
        normalize(form);
        checkValid(form);

        int prodId = flow.getId(form.getBarcode());
        OrderItemPojo p = convert(form, id, prodId, flow.getMrp(prodId));
        checkPrice(p);
        OrderItemPojo existing = api.getCheck(id, p.getProdId());
        int effectiveInventory = getInventorySize(p.getProdId()) - api.lockedInventory(p.getProdId()) + (existing != null? existing.getQuantity():0);
        if (p.getQuantity() > effectiveInventory) {
            throw new ApiException("Total available quantity: " + Math.max(effectiveInventory,0));
        }
        api.update(id, p);
    }

    public void delete(int id, int prodId) throws ApiException {
        api.delete(id, prodId);
    }

    public int getInventorySize(int prodId) throws ApiException {
        return flow.getQuantity(flow.getBarcode(prodId));
    }

    public void checkPrice(OrderItemPojo p) throws ApiException{
        flow.checkPrice(p);
    }

    public List<OrderItemForm> tsvToCurrentOrderForm(MultipartFile file, List<List<String>> errors) throws UploadException, IOException {
        List<OrderItemForm> objectsList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        HashMap<String, Integer> map = parseHeader(br.readLine(), new String[]{"barcode","quantity","selling price"});
        String line;
        int i = 1;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] columns = line.split("\t");

            if (columns.length != 3) {
                errors.add(createRow(i, "Column size: " + columns.length, "Column size must be 2"));
            }

            OrderItemForm form = createOrderForm(columns, map, errors, i);
            objectsList.add(form);
            i++;
        }
        if(br.readLine() != null) throw new UploadException("Input contains more than 5000 lines");

        return objectsList;
    }

    public OrderItemForm createOrderForm(String[] columns, HashMap<String, Integer> map, List<List<String>> errors, int i){
        OrderItemForm form = new OrderItemForm();
        form.setBarcode(columns[map.get("barcode")]);
        form.setQuantity(columns[map.get("quantity")]);
        form.setSellingPrice(columns[map.get("selling price")]);
        normalize(form);
        return form;
    }

    public void normalize(OrderItemForm form){
        form.setBarcode(toLowerCase(form.getBarcode()));
        form.setQuantity(trimZeros(form.getQuantity()));
        form.setSellingPrice(trimZeros(form.getSellingPrice()));
    }
}