package com.increff.pos.job;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.dao.OutwardOrderDao;
import com.increff.pos.dao.PosDaySaleDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.OutwardOrderPojo;
import com.increff.pos.pojo.PosDaySalePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Configuration
@Service
@EnableScheduling
@Transactional
public class OrderSummeryScheduler {

    @Autowired
    private OutwardOrderDao outwardOrderDao;
    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private PosDaySaleDao posDaySaleDao;



    @Scheduled(cron = "${scheduler.time}")
    public void scheduleOrder(){
        Instant instant = Instant.now();
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        LocalDate currentDate = currentDateTime.toLocalDate();
        PosDaySalePojo p = new PosDaySalePojo();

        List<OutwardOrderPojo> outwardOrderPojoList = outwardOrderDao.getAllByDateTimeRange(currentDateTime);

        Double itemsCount = orderItemDao.getItemsCountByDateTimeRange(currentDateTime);

        Double revenue = orderItemDao.getRevenueByDateTimeRange(currentDateTime);


        if(itemsCount == null){
            itemsCount = 0.0;
        }

        if(revenue == null){
            revenue = 0.0;
        }


        PosDaySalePojo toUpdate= getCheck(currentDate);
        if(toUpdate == null){
            p.setDate(currentDate);
            p.setOrderCount(outwardOrderPojoList.size());
            p.setTotalRevenue(revenue);
            p.setItemCount(itemsCount);
            posDaySaleDao.insert(p);
        }
        else{
            toUpdate.setOrderCount(outwardOrderPojoList.size());
            toUpdate.setTotalRevenue(revenue);
            toUpdate.setItemCount(itemsCount);
        }

    }

    public PosDaySalePojo getCheck(LocalDate currentDate){
        return posDaySaleDao.select(currentDate);
    }
}
