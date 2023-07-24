package com.increff.pos.controller;


import com.increff.pos.dto.ProductDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.exception.UploadException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.DataTableResponse;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.xml.crypto.Data;
import java.util.List;

@Api
@RestController
public class ProductController{

    @Autowired
    private ProductDto dto;

    @ApiOperation(value = "Adds a product")
    @RequestMapping(path = "/api/admin/product", method = RequestMethod.POST)
    public void add(@RequestBody ProductForm form) throws ApiException {
        dto.add(form);
    }

    @ApiOperation(value = "Adds multiple product")
    @RequestMapping(path = "/api/admin/product/upload", method = RequestMethod.POST)
    public void tsvUpload(@RequestParam("file")MultipartFile file) throws ApiException, UploadException {
        dto.tsvUpload(file);
    }

    @ApiOperation(value = "Gets a product by ID")
    @RequestMapping(path = "/api/admin/product/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable int id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets list of all products")
    @RequestMapping(path = "/api/product", method = RequestMethod.GET)
    public List<ProductData> getAll() throws ApiException {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates a product")
    @RequestMapping(path = "/api/admin/product/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody ProductForm f) throws ApiException {
        dto.update(id, f);
    }
}
