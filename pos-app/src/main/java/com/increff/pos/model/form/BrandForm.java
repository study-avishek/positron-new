package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class BrandForm {

    @NotBlank(message = "Brand name cannot be empty")
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*()_+=\\\\{}|;':,.<>?/ -]*", message = "Brand name cannot contain emoji, non standard characters etc.")
    @Size(max = 50, message = "Brand name cannot be more than 50 characters")
    private String brand;

    @NotBlank(message = "Category name cannot be empty")
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*()_+=\\\\{}|;':,.<>?/ -]*", message = "Category name cannot contain emoji, non standard characters etc.")
    @Size(max = 50, message = "Category name cannot more than 50 characters")
    private String category;

}
