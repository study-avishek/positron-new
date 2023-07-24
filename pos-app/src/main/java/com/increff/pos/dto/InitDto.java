package com.increff.pos.dto;

import java.util.Objects;

import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.exception.ApiException;
import com.increff.pos.api.UserApi;


@Component
public class InitDto {

	@Autowired
	private UserApi api;


	@Value("${supervisor.email}")
	private String supervisorEmail;
	public void initSite(UserForm form) throws ApiException {
		ValidationUtil.checkValid(form);
		normalize(form);
		UserPojo data = api.get(form.getEmail());
		if(data != null) throw new ApiException("Email address already registered");
		UserPojo p = new UserPojo();
		p.setEmail(form.getEmail());
		p.setPassword(form.getPassword());
		if(Objects.equals(form.getEmail(), supervisorEmail)){
			p.setRole("admin");
		}
		else{
			p.setRole("standard");
		}
		api.add(p);
	}

	public void normalize(UserForm form) {
		form.setEmail(form.getEmail().toLowerCase().trim());
	}
}
