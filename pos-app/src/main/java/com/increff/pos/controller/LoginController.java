package com.increff.pos.controller;


import com.increff.pos.dto.LoginDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.form.LoginForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
