package com.increff.pos.dao;

import com.increff.pos.pojo.PosDaySalePojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

@Repository
public class PosDaySaleDao extends AbstractDao{
    private static String select_all = "select p from PosDaySalePojo p where (:startDate=null or date>=:startDate) " +
            "and (:endDate=null or date<=:endDate)";
    private static String select_date = "select p from PosDaySalePojo p where date>=:date";

    public void insert(PosDaySalePojo p){
        em().persist(p);
    }

    public List<PosDaySalePojo> getAll(LocalDate startDate, LocalDate endDate){
        TypedQuery<PosDaySalePojo> query = getQuery(select_all, PosDaySalePojo.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    public PosDaySalePojo select(LocalDate date){
        TypedQuery<PosDaySalePojo> query = getQuery(select_date, PosDaySalePojo.class);
        query.setParameter("date", date);
        return getSingle(query);
    }
}
