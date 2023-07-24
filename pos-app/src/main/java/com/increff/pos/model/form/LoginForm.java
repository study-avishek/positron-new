package com.increff.pos.model.form;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@Setter
public class LoginForm {

	@NotBlank(message = "Email cannot be blank")
	@Email(message = "Enter a valid email address")
	private String email;

	@NotBlank(message = "Password must not be blank")
	private String password;
}
