package cn.itcast.store.service;

import java.sql.SQLException;

import cn.itcast.store.domain.User;

public interface UserService {

	public void userRegist(User user) throws SQLException;

	public boolean userActive(String code) throws SQLException;

	public User userLogin(User user) throws SQLException;
	
}
