package com.increff.pos.controller;

import com.increff.pos.dto.OutwardOrderDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.OutwardOrderData;
import com.increff.pos.model.form.CustomerForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Api
@RestController
@Validated
public class OutwardOrderController {
    @Autowired
    private OutwardOrderDto dto;

    @ApiOperation(value = "creates an order")
    @RequestMapping(path = "/api/order", method = RequestMethod.POST)
    public OutwardOrderData add(){
        return dto.add();
    }

    @ApiOperation(value = "deletes an order")
    @RequestMapping(path = "/api/order/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException {
        dto.delete(id);
    }

    @ApiOperation(value = "Completes an order")
    @RequestMapping(path = "/api/order/complete-order/{id}", method = RequestMethod.PUT)
    public ResponseEntity complete(@PathVariable int id, @RequestBody @Valid CustomerForm customerForm) throws ApiException,IOException {
        return dto.complete(id, customerForm);
    }

    @ApiOperation(value = "Gets all orders")
    @RequestMapping(path = "/api/order", method = RequestMethod.GET)
    public List<OutwardOrderData> getAll() throws ApiException {
        return dto.getAll();
    }
}