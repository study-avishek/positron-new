package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class OrderItemForm {

    @Size(min = 6, max = 20, message = "Barcode should be of length 6 to 20")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Barcode should contain alpha-numeric characters only")
    private String barcode;

    @Pattern(regexp = "^[0-9]{1,10}$", message = "Quantity must be a valid positive integer less than 100,000,000")
    private String quantity;


    @Pattern(regexp = "^[0-9]{1,10}(\\.[0-9]{1,2})?$", message = "Please enter a valid denomination less than 100,000,000")
    private String sellingPrice;
}
