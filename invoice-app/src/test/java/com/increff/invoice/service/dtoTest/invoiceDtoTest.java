package com.increff.invoice.service.dtoTest;

import com.increff.invoice.dto.InvoiceDto;
import com.increff.invoice.model.form.InvoiceForm;
import com.increff.invoice.service.util.AbstractUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.xml.bind.JAXBException;

import static com.increff.invoice.service.util.constructorUtil.createInvoiceForm;
import static org.junit.Assert.assertEquals;

public class invoiceDtoTest extends AbstractUnitTest {

    @Autowired
    private InvoiceDto dto;



    @Test
    public void testGetInvoice() throws JAXBException {
        InvoiceForm form = createInvoiceForm(5);
        dto.getInvoice(form);
    }
}
