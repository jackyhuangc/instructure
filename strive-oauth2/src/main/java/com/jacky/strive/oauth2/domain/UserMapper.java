package com.jacky.strive.oauth2.domain;

import java.util.List;

public interface UserMapper {
	void add(User order);

	void update(User order);

	User getByUserName(String userName);

	List<User> queryAll();
}