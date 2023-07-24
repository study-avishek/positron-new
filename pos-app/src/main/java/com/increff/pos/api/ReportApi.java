package com.increff.pos.api;

import com.increff.pos.dao.ReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReportApi {
    @Autowired
    private ReportDao dao;

    public List<Object[]> getAllSalesData(LocalDateTime startDateTime, LocalDateTime endDateTime, String brandName, String catName){
        return dao.getAllSalesData(startDateTime, endDateTime, brandName, catName);
    }

    public List<Object[]> getAllBrandData(String brand, String category){
        return dao.getAllBrandData(brand, category);
    }

    public List<Object[]> getAllInventoryData(String brand, String category){
        return dao.getAllInventoryData(brand, category);
    }
}
