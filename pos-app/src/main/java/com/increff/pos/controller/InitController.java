package com.increff.pos.controller;

import com.increff.pos.dto.InitDto;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.form.UserForm;
import com.increff.pos.exception.ApiException;

import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InitController extends AbstractUiController {

    @Autowired
    private InitDto dto;


    @ApiOperation(value = "Initializes application")
    @RequestMapping(path = "/public/init", method = RequestMethod.POST)
    public void initSite(@RequestBody UserForm form) throws ApiException {
        dto.initSite(form);
    }
}
