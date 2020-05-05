package cn.itcast.store.dao;

import java.util.List;

import cn.itcast.store.domain.Order;
import cn.itcast.store.domain.OrderItem;
import cn.itcast.store.domain.User;

public interface OrderDao {

	void saveOrder(Order order) throws Exception;

	void saveOrderItem(OrderItem item) throws Exception;

	int getTotalRecords(User user) throws Exception;

	List findMyOrdersWithPage(User user, int startIndex, int pageSize) throws Exception;

	Order findOrderByOid(String oid) throws Exception;

	void updateOrder(Order order) throws Exception;

}
