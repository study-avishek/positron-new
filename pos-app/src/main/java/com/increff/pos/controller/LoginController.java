package com.increff.pos.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.increff.pos.dto.LoginDto;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.increff.pos.model.form.LoginForm;

import com.increff.pos.exception.ApiException;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;

@Api
@RestController
public class LoginController {

    @Autowired
    private LoginDto dto;

    @ApiOperation(value = "Logs in a user")
    @RequestMapping(path = "/public/login", method = RequestMethod.POST)
    public void login(HttpServletRequest req, @RequestBody LoginForm f) throws ApiException {
        dto.login(req, f);
    }

    @RequestMapping(path = "/session/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        dto.logout(request);
    }
}
