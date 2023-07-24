package com.increff.pos.controller;


import com.increff.pos.dto.InventoryDto;
import com.increff.pos.exception.ApiException;

import com.increff.pos.exception.UploadException;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * Interprets JSON requests as InventoryForm
 * Provides JSON requests as InventoryData
 * Maps request path to DTO layer
 */


@Api
@RestController
public class InventoryController {

    @Autowired
    InventoryDto dto;

    //Updates inventory size of a product with specified ID
    @ApiOperation(value = "Updates inventory size of a product")
    @RequestMapping(path = "/api/admin/inventory/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody InventoryForm f) throws ApiException {
        dto.update(id, f);
    }

    @ApiOperation(value = "tsv upload")
    @RequestMapping(path = "/api/admin/inventory/upload", method = RequestMethod.POST)
    public void tsvUpload(@RequestParam("file")MultipartFile file) throws UploadException, ApiException {
        dto.tsvUpload(file);
    }

    //Gets inventory data of all products
    @ApiOperation(value = "Gets inventory data of all products")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAll() throws ApiException{
        return dto.getAll();
    }

    @ApiOperation(value = "Gets inventory data a products")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable int id) throws ApiException{
        return dto.get(id);
    }


}
