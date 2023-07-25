package com.increff.invoice.model.data;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement
public class InvoiceItemData {
    private String name;

    private String quantity;

    private String sellingPrice;

    private String mrp;

    private String itemNumber;
}
