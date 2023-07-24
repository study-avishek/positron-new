package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class UserForm {
	@Getter
	@Setter
	@Email(message = "Enter a valid email address")
	private String email;

	@Getter
	@Setter
	@NotBlank(message = "Password must not be blank")
	@Size(min = 8, max = 20, message = "Password length should be between 8 and 20")
	@Pattern(regexp = "^.*(?=.{8,})(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "Password should contain 1 uppercase or lowercase, 1 special character and 1 number")
	private String password;
}
