package com.increff.pos.api;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BrandApi {

    @Autowired
    private BrandDao dao;

    //Checks if a brand-category combination already exists or not and sends POJO to DAO layer
    public int add(BrandPojo p) throws ApiException {
        BrandPojo temp = dao.select(p.getBrand(), p.getCategory());
        if(temp != null){
            throw new ApiException("Brand category already exists, Brand: " + p.getBrand() + " | Category: " + p.getCategory());
        }
        return dao.insert(p);
    }

    public void addAll(List<BrandPojo> brandPojos){
        for(BrandPojo brandPojo: brandPojos){
            dao.insert(brandPojo);
        }
    }

    //Gets a single POJO from DAO by ID
    public BrandPojo get(int id) throws ApiException {
        return getCheck(id);
    }

    //Gets lis of POJO from DAO
    public List<BrandPojo> getAll() {
        return dao.selectAll();
    }


    //Throws exception if updated brand-category combination already exists or not otherwise updates
    public void update(int id, BrandPojo p) throws ApiException {
        BrandPojo toUpdate = getCheck(id);
        BrandPojo temp= dao.select(p.getBrand(), p.getCategory());
        if(temp != null && temp.getId() != toUpdate.getId()){
            throw new ApiException("Brand category already exists, Brand: " + p.getBrand() + " | Category: " + p.getCategory());
        }
        toUpdate.setCategory(p.getCategory());
        toUpdate.setBrand(p.getBrand());
    }

    //Checks if an ID exists or not
    public BrandPojo getCheck(int id) throws ApiException {
        BrandPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Brand ID does not exists, ID: " + id);
        }
        return p;
    }

    //Checks if brand-category combination exists or not
    public BrandPojo getCheck(String brand, String category) throws ApiException{
        BrandPojo p = dao.select(brand, category);
        if(p == null){
            throw new ApiException("Brand category does not exists, Brand: " + brand + "| Category: " + category);
        }
        return p;
    }
}
