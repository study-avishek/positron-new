package com.increff.pos.controller;

import com.increff.pos.dto.OrderItemDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.exception.UploadException;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderItemForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api
@RestController
public class OrderItemController {

    @Autowired
    private OrderItemDto dto;

    @ApiOperation(value = "Adds a order item")
    @RequestMapping(path = "/api/order-item/{id}", method = RequestMethod.POST)
    public void add(@PathVariable int id, @RequestBody OrderItemForm form) throws ApiException {
        dto.add(id, form);
    }

    @ApiOperation(value = "Adds order by TSV")
    @RequestMapping(path = "/api/order-item/upload/{id}", method = RequestMethod.POST)
    public void tsvUpload(@PathVariable int id, @RequestParam("file") MultipartFile file) throws UploadException {
        dto.tsvUpload(id, file);
    }

    @ApiOperation(value = "Gets a order item")
    @RequestMapping(path = "/api/order-item/{id}/{prodId}", method = RequestMethod.GET)
    public OrderItemData get(@PathVariable int id, @PathVariable int prodId) throws ApiException {
        return dto.get(id, prodId);
    }

    @ApiOperation(value = "Gets list of all order items")
    @RequestMapping(path = "/api/order-item/{id}", method = RequestMethod.GET)
    public List<OrderItemData> getAll(@PathVariable int id) throws ApiException {
        return dto.getAll(id);
    }

    @ApiOperation(value = "Updates a order item")
    @RequestMapping(path = "/api/order-item/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody OrderItemForm f) throws ApiException {
        dto.update(id, f);
    }

    @ApiOperation(value = "Deletes an order item")
    @RequestMapping(path="/api/order-item/{id}/{prodId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id, @PathVariable int prodId) throws ApiException{
        dto.delete(id, prodId);
    }
}
