package cn.itcast.store.service.serviceImp;

import java.sql.SQLException;

import cn.itcast.store.dao.UserDao;
import cn.itcast.store.dao.daoImp.UserDaoImpl;
import cn.itcast.store.domain.User;
import cn.itcast.store.service.UserService;
import cn.itcast.store.utils.BeanFactory;

public class UserServiceImp implements UserService {

	UserDao userDao = (UserDao) BeanFactory.createObject("UserDao");
	@Override
	public void userRegist(User user) throws SQLException {
		// 实现注册
		userDao.userRegist(user);
	}

	@Override
	public boolean userActive(String code) throws SQLException {
		// 实现激活
		// 对DB发送select * from user where code=?
		User user = userDao.userActive(code);

		if (null != user) {
			// 可以根据激活码查询到一个用户
			// 修改用户的状态，清除激活码。
			user.setState(1);
			user.setCode(null);
			// 对数据库执行一次真实的更新操作
			userDao.updateUser(user);
			return true;
		} else {
			// 不可以根据激活码查询到一个用户
			return false;
		}
	}

	@Override
	public User userLogin(User user) throws SQLException {
		//使用异常在模块之间传递数据
		
		// 实现激活
		User userInfo = userDao.userLogin(user);
		if(null == userInfo) {
			//抛出异常
			throw new RuntimeException("密码不正确！");
		}else if(userInfo.getState() == 0) {
			//抛出异常
			throw new RuntimeException("用户未激活！");
		}else {
			///返回用户信息
			return userInfo;
		}
	}

}
