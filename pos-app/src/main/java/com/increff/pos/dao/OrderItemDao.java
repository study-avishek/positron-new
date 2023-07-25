package com.increff.pos.dao;

import com.increff.pos.model.enums.OrderStatus;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao{
    private static String select_id = "select p from OrderItemPojo p where orderId=:orderId and prodId=:prodId";
    private static String select_all = "select p from OrderItemPojo p where orderId=:orderId";
    private static String delete_items_by_order_id = "delete from OrderItemPojo p where p.orderId in" +
            "(select o.id from OutwardOrderPojo o where o.id=:orderId and o.orderStatus=:status )";
    private static String delete_item = "delete from OrderItemPojo p where orderId=:orderId and prodId=:prodId " +
            "and orderId in (select o.id from OutwardOrderPojo o where o.orderStatus=:status)";

    private static String locked_inventory = "select sum(p.quantity) from OrderItemPojo p, OutwardOrderPojo o "
        +"where (o.id = p.orderId) "
        +"and (o.orderStatus=:status) and (p.prodId=:prodId)";

    private static String get_revenue_by_datetime_range = "select sum(p.sellingPrice * p.quantity) from OrderItemPojo p where p.orderId in (select o from OutwardOrderPojo o where orderDateTime>=:previousDay and orderStatus=:orderStatus)";

    private static String get_items_count_by_datetime_range = "select sum(p.quantity * 1.0) from OrderItemPojo p where p.orderId in (select o from OutwardOrderPojo o where orderDateTime>=:previousDay and orderStatus=:orderStatus)";

    @Transactional
    public void insert(OrderItemPojo p) {
        em().persist(p);
    }

    public OrderItemPojo select(int orderId, int prodId) {
        TypedQuery<OrderItemPojo> query = getQuery(select_id, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        query.setParameter("prodId", prodId);
        return getSingle(query);
    }


    public List<OrderItemPojo> selectAll(int orderId) {
        TypedQuery<OrderItemPojo> query = getQuery(select_all, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

    public void delete(int orderId, int prodId){
        Query query = em().createQuery(delete_item);
        query.setParameter("orderId",orderId);
        query.setParameter("prodId", prodId);
        query.setParameter("status", OrderStatus.ACTIVE);
        query.executeUpdate();
    }

    public void deleteItemsByOrderId(int orderId){
        Query query = em().createQuery(delete_items_by_order_id);
        query.setParameter("orderId", orderId);
        query.setParameter("status",OrderStatus.ACTIVE);
        query.executeUpdate();
    }

    public int lockedInventory(int prodId){
        Query query = em().createQuery(locked_inventory);
        query.setParameter("prodId", prodId);
        query.setParameter("status", OrderStatus.ACTIVE);
        Long temp = (Long)query.getSingleResult();
        if(temp == null) return 0;
        return temp.intValue();
    }

    public Double getRevenueByDateTimeRange(LocalDateTime previousDay) {
        TypedQuery<Double> query = getQuery(get_revenue_by_datetime_range, Double.class);
        query.setParameter("previousDay", previousDay);
        query.setParameter("orderStatus", OrderStatus.COMPLETED);
        return query.getSingleResult();
    }

    public Double getItemsCountByDateTimeRange(LocalDateTime previousDay) {
        TypedQuery<Double> query = getQuery(get_items_count_by_datetime_range, Double.class);
        query.setParameter("previousDay", previousDay);
        query.setParameter("orderStatus", OrderStatus.COMPLETED);
        return query.getSingleResult();
    }

}
