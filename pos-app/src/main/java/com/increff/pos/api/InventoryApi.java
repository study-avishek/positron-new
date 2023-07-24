package com.increff.pos.api;

import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.dao.InventoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class InventoryApi {

    @Autowired
    private InventoryDao dao;

    public void add(InventoryPojo p) throws ApiException {
        dao.insert(p);
    }

    public void addAll(List<InventoryPojo> inventoryPojos) throws ApiException{
        for(InventoryPojo inventoryPojo: inventoryPojos){
            update(inventoryPojo.getId(), inventoryPojo);
        }
    }

    public void update(int id, InventoryPojo p) throws ApiException {
        InventoryPojo toUpdate = getCheck(id);
        toUpdate.setQuantity(p.getQuantity());
    }

    public List<Object[]> getAll(){
        return dao.selectAll() ;
    }

    public InventoryPojo getCheck(int id) throws ApiException {
        InventoryPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Inventory ID does not exists, ID: " + id);
        }
        return p;
    }
}
