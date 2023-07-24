package com.increff.invoice.api;

import com.increff.invoice.model.form.InvoiceForm;
import com.increff.invoice.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;

@Component
public class InvoiceApi {

    @Autowired
    private ConvertUtil convert;

    public String generateInvoice(InvoiceForm form) throws JAXBException {
        convert.pojo2XmlConverter(form);
        return convert.xml2base64();

    }
}
