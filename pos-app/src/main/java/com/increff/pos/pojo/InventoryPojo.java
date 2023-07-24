package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class InventoryPojo extends AbstractPojo{
    @Id
    private int id;
    private int quantity;
}
