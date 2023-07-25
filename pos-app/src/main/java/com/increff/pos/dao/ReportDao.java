package com.increff.pos.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReportDao extends AbstractDao{

    private static String sales_report = "SELECT b.brand, b.category, SUM(oi.quantity), SUM(oi.quantity * oi.sellingPrice) " +
            "FROM OrderItemPojo oi, OutwardOrderPojo o, ProductPojo p, BrandPojo b " +
            "WHERE o.id = oi.orderId " +
            "AND oi.prodId = p.id " +
            "AND p.brandCatId = b.id " +
            "AND (:startDateTime IS NULL OR o.orderDateTime >= :startDateTime) " +
            "AND (:endDateTime IS NULL OR o.orderDateTime <= :endDateTime) " +
            "AND (:brand IS NULL OR b.brand = :brand) " +
            "AND (:category IS NULL OR b.category = :category) " +
            "GROUP BY b.brand, b.category";

    private static String inventory_report = "SELECT b.brand, b.category, SUM(i.quantity) FROM BrandPojo b, ProductPojo p, "+
            "InventoryPojo i WHERE i.id = p.id "+
            "AND p.brandCatId = b.id "+
            "AND (:brand IS NULL OR b.brand=:brand) "+
            "AND (:category IS NULL OR b.category = :category) "+
            "GROUP BY b.brand, b.category";

    public List<Object[]> getAllSalesData(LocalDateTime startDateTime, LocalDateTime endDateTime, String brand, String category) {
        TypedQuery<Object[]> query = em().createQuery(sales_report, Object[].class);
        query.setParameter("startDateTime", startDateTime);
        query.setParameter("endDateTime", endDateTime);
        query.setParameter("brand", brand);
        query.setParameter("category", category);

        return query.getResultList();
    }

    public List<Object[]> getAllInventoryData(String brand, String category){
        TypedQuery<Object[]> query = em().createQuery(inventory_report, Object[].class);
        query.setParameter("brand", brand);
        query.setParameter("category", category);
        return query.getResultList();
    }
}

