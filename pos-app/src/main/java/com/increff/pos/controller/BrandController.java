package com.increff.pos.controller;


import com.increff.pos.dto.BrandDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.exception.UploadException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.DataTableResponse;
import com.increff.pos.model.form.BrandForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Api
@RestController
@MultipartConfig
public class BrandController {

    @Autowired
    private BrandDto dto;

    @ApiOperation(value = "Adds a brand")
    @RequestMapping(path = "/api/admin/brand", method = RequestMethod.POST)
    public void add(@RequestBody BrandForm form) throws ApiException {
        dto.add(form);
    }

    @ApiOperation(value = "tsv upload")
    @RequestMapping(path = "/api/admin/brand/upload", method = RequestMethod.POST)
    public void tsvUpload(@RequestParam("file") MultipartFile file) throws ApiException, UploadException {
        dto.tsvUpload(file);
    }


    @ApiOperation(value = "Gets a brand by ID")
    @RequestMapping(path = "/api/admin/brand/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable int id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets list of all brand")
    @RequestMapping(path = "/api/brand", method = RequestMethod.GET)
    public List<BrandData> getAll() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ipAddress = request.getRemoteAddr();
        return dto.getAll();
    }


    @ApiOperation(value = "Updates a brand")
    @RequestMapping(path = "/api/admin/brand/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody BrandForm f) throws ApiException {
        dto.update(id, f);
    }
}
