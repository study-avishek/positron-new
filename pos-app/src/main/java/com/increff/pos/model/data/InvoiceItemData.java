package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InvoiceItemData {
    private String name;
    private String quantity;
    private String sellingPrice;
    private String mrp;
    private String itemNumber;
}
