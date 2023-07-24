package com.increff.pos.model.data;

import com.increff.pos.model.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class OutwardOrderData {
    private int id ;
    private OrderStatus orderStatus;
    private long itemCount;
    private Double revenue;
}
