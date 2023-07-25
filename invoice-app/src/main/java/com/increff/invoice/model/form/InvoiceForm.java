package com.increff.invoice.model.form;

import com.increff.invoice.model.data.InvoiceItemData;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@XmlRootElement
public class InvoiceForm {
    private String timestamp;
    private String invoiceNumber;
    private String customerName;
    private String email;
    private String phone;

    private List<InvoiceItemData> invoiceItemList;

}
