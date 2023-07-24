package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.method.P;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class InventoryForm {

    @Pattern(regexp = "^[0-9]{1,10}$", message = "Enter a valid non-negative integer less than 100,000,000")
    private String quantity;
}
