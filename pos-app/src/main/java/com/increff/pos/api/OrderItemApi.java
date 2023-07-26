package com.increff.pos.api;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.enums.OrderStatus;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class OrderItemApi {

    @Autowired
    private OrderItemDao dao;

    public void add(OrderItemPojo p) throws ApiException {
        dao.insert(p);
    }

    public void delete(int id, int prodId) throws ApiException{
        dao.delete(id, prodId);
    }

    public List<OrderItemPojo> getAll(int id) {
        return dao.selectAll(id);
    }

    public void update(int id, OrderItemPojo p) throws ApiException {
        OrderItemPojo toUpdate = getCheck(id, p.getProdId());
        toUpdate.setQuantity(p.getQuantity());
        toUpdate.setSellingPrice(p.getSellingPrice());
    }

    public OrderItemPojo getCheck(int orderId, int prodId) throws ApiException {
        return dao.select(orderId, prodId);
    }

    public int lockedInventory(int prodId) throws ApiException {
        return dao.lockedInventory(prodId);
    }
}
