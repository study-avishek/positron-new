package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CustomerForm {

    @NotBlank(message = "Customer name cannot be blank")
    @Size(min = 1, max = 30, message = "Customer name cannot be empty or longer than 30 characters. If more than 30 characters, use last name only")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Customer name should contain alphabets only")
    private String customerName;

    @NotBlank
    @Email(message = "Enter valid customer email")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Enter valid customer phone number of length 10 without country code")
    private String phone;
}
