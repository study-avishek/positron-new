package com.increff.pos.controller;

import com.increff.pos.dto.PosDaySaleDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.PosDaySaleData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class PosDaySaleController {
    @Autowired
    private PosDaySaleDto dto;

    @ApiOperation(value = "Gets reports of every day")
    @RequestMapping(path = "/api/pos-day-sale/{startDate}/{endDate}", method = RequestMethod.GET)
    public List<PosDaySaleData> getAll(@PathVariable String startDate, @PathVariable String endDate) throws ApiException {
        return dto.getAll(startDate, endDate);
    }
}
