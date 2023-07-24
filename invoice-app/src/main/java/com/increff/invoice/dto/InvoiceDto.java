package com.increff.invoice.dto;

import com.increff.invoice.api.InvoiceApi;
import com.increff.invoice.model.form.InvoiceForm;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;

@Component
public class InvoiceDto {
    @Autowired
    private InvoiceApi api;

    public String getInvoice(InvoiceForm form) throws JAXBException {
        return api.generateInvoice(form);
    }
}
