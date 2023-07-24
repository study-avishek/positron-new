package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderItemPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence2")
    @SequenceGenerator(name = "sequence2", initialValue = 100000, allocationSize = 1)
    private int id;

    private int orderId;

    private int prodId;

    private int quantity;

    private Double sellingPrice;
}
