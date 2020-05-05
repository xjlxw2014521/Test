package cn.itcast.store.service;

import cn.itcast.store.domain.Order;
import cn.itcast.store.domain.PageModel;
import cn.itcast.store.domain.User;

public interface OrderService {

	void saveOrder(Order order) throws Exception;

	PageModel findMyOrdersWithPage(User user, int curNum) throws Exception;

	Order findOrderByOid(String oid) throws Exception;

	void updateOrder(Order order) throws Exception ;
	
}
