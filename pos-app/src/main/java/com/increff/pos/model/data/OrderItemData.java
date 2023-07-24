package com.increff.pos.model.data;

import com.increff.pos.model.form.OrderItemForm;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class OrderItemData extends OrderItemForm {

    private int prodId;
    private String productBrandName;
    private Double mrp;
    private Double itemTotal;
}