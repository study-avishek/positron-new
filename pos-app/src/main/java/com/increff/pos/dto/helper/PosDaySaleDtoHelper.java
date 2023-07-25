package com.increff.pos.dto.helper;

import com.increff.pos.model.data.PosDaySaleData;
import com.increff.pos.pojo.PosDaySalePojo;

import java.time.format.DateTimeFormatter;

public class PosDaySaleDtoHelper {
    public static PosDaySaleData convert(PosDaySalePojo pojo){
        PosDaySaleData data = new PosDaySaleData();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-LLLL-yyyy");

        data.setDate(pojo.getDate().format(formatter));
        data.setId(pojo.getId());
        data.setItemCount((int)(double)pojo.getItemCount());
        data.setOrderCount(pojo.getOrderCount());
        data.setTotalRevenue(pojo.getTotalRevenue());

        return data;
    }

}
