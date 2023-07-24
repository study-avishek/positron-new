package com.increff.pos.dto;

import com.increff.pos.api.ReportApi;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.SalesReportForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.increff.pos.util.ValidationUtil.checkValid;

@Component
public class ReportDto {
    @Autowired
    private ReportApi api;
    public List<SalesReportData> getAllSalesData(SalesReportForm form) throws ApiException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate sd = Objects.equals(form.getStartDate(), "") ? null: LocalDate.parse(form.getStartDate(), dtf);
        LocalDate ed = Objects.equals(form.getEndDate(), "") ? null: LocalDate.parse(form.getEndDate(), dtf);
        LocalDateTime startDateTime = sd == null? null: sd.atTime(0,0,0);
        LocalDateTime endDateTime = ed == null? null:ed.atTime(23,59,59);
        String brandName = (Objects.equals(form.getBrand(), "") ? null: form.getBrand());
        String catName = (Objects.equals(form.getCategory(), "") ?null: form.getCategory());

        List<Object[]> list1 =  api.getAllSalesData(startDateTime, endDateTime, brandName, catName);
        List<SalesReportData> list2 = new ArrayList<>();
        for(Object[] obj: list1){
            SalesReportData data = new SalesReportData();
            data.setBrand((String)obj[0]);
            data.setCategory((String)obj[1]);
            data.setQuantity((long)obj[2]);
            data.setRevenue((double)obj[3]);
            list2.add(data);
        }
        return list2;

    }

    public List<BrandData> getAllBrandData(BrandForm form) throws ApiException{
        String brand = (Objects.equals(form.getBrand(), "") ? null: form.getBrand());
        String category = (Objects.equals(form.getCategory(), "") ?null: form.getCategory());
        List<Object[]> list1 = api.getAllBrandData(brand, category);
        List<BrandData> list2 = new ArrayList<>();
        for(Object[] obj: list1){
            BrandData brandData = new BrandData();
            brandData.setBrand((String) obj[0]);
            brandData.setCategory((String) obj[1]);
            list2.add(brandData);
        }
        return list2;
    }

    public List<InventoryReportData> getAllInventoryData(BrandForm form) throws ApiException{
        String brand = (Objects.equals(form.getBrand(), "") ? null: form.getBrand());
        String category = (Objects.equals(form.getCategory(), "") ?null: form.getCategory());
        List<Object[]> list1= api.getAllInventoryData(brand, category);
        List<InventoryReportData> list2 = new ArrayList<>();
        for(Object[] obj: list1){
            InventoryReportData inventoryReportData = new InventoryReportData();
            inventoryReportData.setBrand((String) obj[0]);
            inventoryReportData.setCategory((String) obj[1]);
            inventoryReportData.setQuantity((long)obj[2]);
            list2.add(inventoryReportData);
        }
        return list2;
    }
}
