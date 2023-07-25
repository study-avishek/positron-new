package com.increff.pos.api;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ProductApi {

    @Autowired
    private ProductDao dao;

    public int add(ProductPojo p) throws ApiException {
        ProductPojo temp = dao.select(p.getBarcode());
        if(temp != null) {
            throw new ApiException("Product barcode already exists: " + p.getBarcode());
        }
        return dao.insert(p);
    }

    public void addAll(List<ProductPojo> productPojoList) throws ApiException {
        for(ProductPojo p: productPojoList) {
            dao.insert(p);
        }
    }

    @Transactional
    public List<Object[]> getAll() {
        return dao.selectAll();
    }

    public void update(int id, ProductPojo p) throws ApiException {
        ProductPojo toUpdate = getCheck(id);
        ProductPojo temp = dao.select(p.getBarcode());

        if(temp != null && temp.getId() != id && Objects.equals(temp.getBarcode(), p.getBarcode())) {
            throw new ApiException("Product barcode already exists: " + p.getBarcode());
        }


        toUpdate.setBrandCatId(p.getBrandCatId());
        toUpdate.setName(p.getName());
        toUpdate.setMrp(p.getMrp());
        toUpdate.setBarcode(p.getBarcode());
    }

    public ProductPojo getCheck(int id) throws ApiException {
        ProductPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Product ID does not exists: " + id);
        }
        return p;
    }

    public ProductPojo getCheck(String barcode) throws ApiException{
        ProductPojo p =dao.select(barcode);
        if(p == null){
            throw new ApiException("Product barcode does not exists: " + barcode);
        }
        return p;
    }
}
