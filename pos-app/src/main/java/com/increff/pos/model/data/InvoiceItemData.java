package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InvoiceItemData {
    private String name;
    private int quantity;
    private Double sellingPrice;
    private Double mrp;
    private int itemNumber;
}
