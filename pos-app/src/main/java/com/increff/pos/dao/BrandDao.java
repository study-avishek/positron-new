package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao {
    private static String select_id = "select p from BrandPojo p where id=:id";
    private static String select_brand_cat = "select p from BrandPojo p where brand=:brand and category=:category";
    private static String select_all = "select p from BrandPojo p";


    public int insert(BrandPojo p) {
        em().persist(p);
        return p.getId();
    }

    public BrandPojo select(int id) {
        TypedQuery<BrandPojo> query = getQuery(select_id, BrandPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public BrandPojo select(String brand, String category) {
        TypedQuery<BrandPojo> query = getQuery(select_brand_cat, BrandPojo.class);
        query.setParameter("brand", brand);
        query.setParameter("category", category);
        return getSingle(query);
    }

    public List<BrandPojo> selectAll() {
        TypedQuery<BrandPojo> query = getQuery(select_all, BrandPojo.class);
        return query.getResultList();
    }
}
