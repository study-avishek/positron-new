package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.InventoryPojo;

@Repository
public class InventoryDao extends AbstractDao {
    private static String select_id = "select p from InventoryPojo p where id=:id";
    private static String select_all = "select p.barcode, p.name, b.brand, b.category, i.quantity, i.id from " +
            "ProductPojo p, BrandPojo b, InventoryPojo i where (i.id = p.id) and (p.brandCatId = b.id)";

    @Transactional
    public void insert(InventoryPojo p) {
        em().persist(p);
    }

    public InventoryPojo select(int id) {
        TypedQuery<InventoryPojo> query = getQuery(select_id, InventoryPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<Object[]> selectAll() {
        TypedQuery<Object[]> query = getQuery(select_all, Object[].class);
        return query.getResultList();
    }
}
