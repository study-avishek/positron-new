package com.increff.pos.dto;

import com.increff.pos.api.PosDaySaleApi;
import com.increff.pos.dto.helper.PosDaySaleDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.PosDaySaleData;
import com.increff.pos.pojo.PosDaySalePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.increff.pos.dto.helper.PosDaySaleDtoHelper.convert;

@Component
public class PosDaySaleDto {
    @Autowired
    private PosDaySaleApi api;

    public List<PosDaySaleData> getAll(String startDate, String endDate) throws ApiException{
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate sd, ed;
        try {
            sd = Objects.equals(startDate, "0") ? null : LocalDate.parse(startDate, dtf);
            ed = Objects.equals(endDate, "0") ? null : LocalDate.parse(endDate, dtf);
        }
        catch (DateTimeParseException e){
            throw new ApiException("Unable to parse dates. Please input valid dates");
        }
        List<PosDaySalePojo> list1 = api.getAll(sd, ed);
        List<PosDaySaleData> list2 = new ArrayList<>();
        for(PosDaySalePojo pojo: list1){
            list2.add(convert(pojo));
        }
        return list2 ;
    }

}
