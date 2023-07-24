package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PosDaySaleData {
    private int id;
    private String date;
    private int orderCount;
    private int itemCount;
    private Double totalRevenue;
}
