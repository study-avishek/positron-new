package com.increff.pos.pojo;

import com.increff.pos.model.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OutwardOrderPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence1")
    @SequenceGenerator(name = "sequence1", initialValue = 1000, allocationSize = 1)
    private int id;

    private LocalDateTime orderDateTime;

    @Enumerated(EnumType.ORDINAL)
    private OrderStatus orderStatus;


}
