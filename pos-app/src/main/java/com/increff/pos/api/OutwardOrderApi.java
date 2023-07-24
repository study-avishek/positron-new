package com.increff.pos.api;

import com.increff.pos.dao.OutwardOrderDao;
import com.increff.pos.model.enums.OrderStatus;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.OutwardOrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OutwardOrderApi {

    @Autowired
    private OutwardOrderDao dao;


    public OutwardOrderPojo add(OutwardOrderPojo p){
        p.setOrderStatus(OrderStatus.ACTIVE);
        p.setOrderDateTime(LocalDateTime.now());
        return dao.add(p);
    }

    public void delete(int id){
        dao.delete(id);
    }

    public List<Object[]> getAll(){
        return dao.getAll();
    }

    public void changeStatus(int id) throws ApiException {
        OutwardOrderPojo p = dao.select(id, OrderStatus.ACTIVE);
        if(p==null){
            throw new ApiException("Order is already invoiced");
        }
        p.setOrderStatus(OrderStatus.COMPLETED);
        p.setOrderDateTime(LocalDateTime.now());
    }

    public OutwardOrderPojo get(int id) throws ApiException{
        OutwardOrderPojo p = dao.select(id, OrderStatus.ACTIVE);
        if(p == null){
            throw new ApiException("No active orders with this ID: "+id);
        }
        return p;
    }

}
