package com.increff.pos.dao;

import com.increff.pos.model.enums.OrderStatus;
import com.increff.pos.pojo.OutwardOrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OutwardOrderDao extends AbstractDao{

    private static String delete_id ="delete from OutwardOrderPojo p where id=:id and orderStatus=:status";
    private static String select_all="select o.id, o.orderStatus, coalesce(SUM(oi.quantity),0), coalesce(SUM(oi.quantity*oi.sellingPrice),0) from " +
            "OutwardOrderPojo o left join OrderItemPojo oi ON o.id = oi.orderId group by o.id, o.orderStatus";
    private static String select_id="select p from OutwardOrderPojo p where id=:id and orderStatus=:status";
    private static String get_by_datetime_range = "select p from OutwardOrderPojo p where orderDateTime>=:previousDay and orderStatus=:orderStatus";

    public OutwardOrderPojo add(OutwardOrderPojo p){
        em().persist(p);
        return p;
    }

    public OutwardOrderPojo select(int id, OrderStatus status){
        TypedQuery<OutwardOrderPojo> query = getQuery(select_id, OutwardOrderPojo.class);
        query.setParameter("id", id);
        query.setParameter("status",status);
        return query.getSingleResult();
    }

    public void delete(int id){
        Query query = em().createQuery(delete_id);
        query.setParameter("id", id);
        query.setParameter("status",OrderStatus.ACTIVE);
        query.executeUpdate();
    }

    public List<Object[]> getAll(){
        TypedQuery<Object[]> query = getQuery(select_all, Object[].class);
        return query.getResultList();
    }

    public List<OutwardOrderPojo> getAllByDateTimeRange(LocalDateTime previousDay) {
        TypedQuery<OutwardOrderPojo> query = getQuery(get_by_datetime_range, OutwardOrderPojo.class);
        query.setParameter("previousDay", previousDay);
        query.setParameter("orderStatus", OrderStatus.COMPLETED);
        return query.getResultList();
    }
}
