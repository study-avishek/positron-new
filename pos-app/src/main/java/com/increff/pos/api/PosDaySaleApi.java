package com.increff.pos.api;

import com.increff.pos.dao.PosDaySaleDao;
import com.increff.pos.pojo.PosDaySalePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PosDaySaleApi {
    @Autowired
    private PosDaySaleDao dao;

    public List<PosDaySalePojo> getAll(LocalDate startDate, LocalDate endDate){
        return dao.getAll(startDate, endDate);
    }
}
