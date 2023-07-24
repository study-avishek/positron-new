package com.increff.pos.controller;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.SalesReportForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class ReportController {

    @Autowired
    private ReportDto dto;

    @ApiOperation(value = "Get list of sales report")
    @RequestMapping(path = "/api/report/sales", method = RequestMethod.POST)
    public List<SalesReportData> getAllSalesData(@RequestBody SalesReportForm form) throws ApiException {
        return dto.getAllSalesData(form);
    }

    @ApiOperation(value="Get list of brand report")
    @RequestMapping(path="/api/report/brands", method = RequestMethod.POST)
    public List<BrandData> getAllBrandData(@RequestBody BrandForm form) throws ApiException{
        return dto.getAllBrandData(form);
    }

    @ApiOperation(value = "Get list of inventory report")
    @RequestMapping(path="/api/report/inventory", method = RequestMethod.POST)
    public List<InventoryReportData> getAllInventoryData(@RequestBody BrandForm form) throws ApiException{
        return dto.getAllInventoryData(form);
    }


}
