package com.increff.invoice.controller;


import com.increff.invoice.dto.InvoiceDto;
import com.increff.invoice.model.form.InvoiceForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBException;

@Api
@RestController
public class invoiceController {

    @Autowired
    private InvoiceDto dto;

    @ApiOperation(value = "get pdf as base64")
    @RequestMapping(path = "/api/invoice", method = RequestMethod.POST)
    public String getInvoice(@RequestBody InvoiceForm form) throws JAXBException {
        return dto.getInvoice(form);
    }

}
