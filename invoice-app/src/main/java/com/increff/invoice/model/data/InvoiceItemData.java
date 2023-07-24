package com.increff.invoice.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceItemData {
    private String name;
    private int quantity;
    private Double sellingPrice;
    private Double mrp;
    private int itemNumber;
}
