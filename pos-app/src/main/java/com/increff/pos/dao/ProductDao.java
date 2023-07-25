package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {
    private static String select_id = "select p from ProductPojo p where id=:id";

    private static String select_barcode = "select p from ProductPojo p where barcode=:barcode";
    private static String select_all = "select p.name, p.barcode, b.brand, b.category, p.mrp, p.id from ProductPojo p, " +
            "BrandPojo b where (p.brandCatId = b.id)";

    @Transactional
    public int insert(ProductPojo p) {
        em().persist(p);
        return p.getId();
    }

    public ProductPojo select(int id) {
        TypedQuery<ProductPojo> query = getQuery(select_id, ProductPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public ProductPojo select(String barcode){
        TypedQuery<ProductPojo> query = getQuery(select_barcode, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return getSingle(query);
    }

    public List<Object[]> selectAll() {
        TypedQuery<Object[]> query = getQuery(select_all, Object[].class);
        return query.getResultList();
    }

}
