package com.increff.pos.api;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.pos.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;

@Service
@Transactional
public class UserApi {
	@Autowired
	private UserDao dao;

	public void add(UserPojo p) throws ApiException {
		dao.insert(p);
	}

	public UserPojo get(String email) throws ApiException {
		return dao.select(email);
	}
}
