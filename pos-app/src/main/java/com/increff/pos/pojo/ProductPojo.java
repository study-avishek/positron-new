package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(indexes = @Index(columnList = "barcode", unique = true))
public class ProductPojo extends AbstractPojo{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String barcode;
    private int brandCatId;
    private String name;
    private Double mrp;
}
