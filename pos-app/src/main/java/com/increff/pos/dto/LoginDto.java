package com.increff.pos.dto;

import com.increff.pos.api.UserApi;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.form.LoginForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

import static com.increff.pos.dto.helper.LoginDtoHelper.convert;
import static com.increff.pos.util.ValidationUtil.checkValid;

@Component
public class LoginDto {
	@Autowired
	private UserApi api;

	public void login(HttpServletRequest req, LoginForm f) throws ApiException{
		checkValid(f);
		UserPojo p = api.get(f.getEmail());
		boolean authenticated = (p != null && Objects.equals(p.getPassword(), f.getPassword()));
		if (!authenticated) {
			throw new ApiException("Invalid credentials");
		}

		Authentication authentication = convert(p);

		HttpSession session = req.getSession(true);

		SecurityUtil.createContext(session);

		SecurityUtil.setAuthentication(authentication);
	}

	public void logout(HttpServletRequest request){
		request.getSession().invalidate();
	}

}
