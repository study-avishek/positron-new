package com.increff.pos.controller;

import com.increff.pos.dto.InitDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.form.UserForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
