package com.increff.pos.dao;

import com.increff.pos.pojo.UserPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
public class UserDao extends AbstractDao {
	private static String select_email = "select p from UserPojo p where email=:email";

	
	@Transactional
	public void insert(UserPojo p) {
		em().persist(p);
	}

	public UserPojo select(String email) {
		TypedQuery<UserPojo> query = getQuery(select_email, UserPojo.class);
		query.setParameter("email", email);
		return getSingle(query);
	}
}
