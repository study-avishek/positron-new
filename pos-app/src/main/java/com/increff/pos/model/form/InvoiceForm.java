package com.increff.pos.model.form;

import com.increff.pos.model.data.InvoiceItemData;

import lombok.Getter;
import lombok.Setter;


import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@XmlRootElement
public class InvoiceForm {
    private String timestamp;
    private int invoiceNumber;


    private String customerName;

    private String email;
    private String phone;

    private List<InvoiceItemData> invoiceItemList;

}
