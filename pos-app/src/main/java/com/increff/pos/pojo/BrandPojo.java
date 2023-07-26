package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(indexes = @Index(columnList = "brand, category", unique = true))
public class BrandPojo extends AbstractPojo{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String brand;
    private String category;
}
