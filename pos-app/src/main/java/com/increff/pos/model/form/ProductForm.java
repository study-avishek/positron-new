package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ProductForm {
    @NotBlank(message = "Product name cannot be empty")
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*()_+=\\\\{}|;':,.<>?/ -]*", message = "Product name cannot contain emoji, non standard characters etc.")
    @Size(max = 50, message = "Product name cannot be more than 30 characters")
    private String name;

    @NotBlank(message = "Brand cannot be empty")
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*()_+=\\\\{}|;':,.<>?/ -]*", message = "Brand cannot contain emoji, non standard characters etc.")
    @Size( max = 50, message = "Brand cannot be more than 30 characters")
    private String brand;

    @NotBlank(message = "Category cannot be empty")
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*()_+=\\\\{}|;':,.<>?/ -]*", message = "Category cannot contain emoji, non standard characters etc.")
    @Size(max = 50, message = "Category cannot be more than 30 characters")
    private String category;

    @NotBlank(message = "Product barcode cannot be empty")
    @Size(min = 6, max = 20, message = "Barcode should be of length 6 to 20")
    @Pattern(regexp = "[a-zA-Z0-9]*", message = "Barcode should contain alpha-numeric characters only")
    private String barcode;

    @Pattern(regexp = "^[0-9]{1,10}(\\.[0-9]{1,2})?$", message = "Please enter a valid denomination less than 100,000,000")
    private String mrp;
}
