package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SalesReportData {
    private String brand;
    private String category;
    private long quantity;
    private double revenue;
}
