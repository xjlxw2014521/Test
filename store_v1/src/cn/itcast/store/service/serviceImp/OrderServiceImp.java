package cn.itcast.store.service.serviceImp;

import java.util.List;

import cn.itcast.store.dao.OrderDao;
import cn.itcast.store.dao.daoImp.OrderDaoImp;
import cn.itcast.store.domain.Order;
import cn.itcast.store.domain.OrderItem;
import cn.itcast.store.domain.PageModel;
import cn.itcast.store.domain.User;
import cn.itcast.store.service.OrderService;
import cn.itcast.store.utils.BeanFactory;
import cn.itcast.store.utils.JDBCUtils;

public class OrderServiceImp implements OrderService {
	private OrderDao orderDao = (OrderDao) BeanFactory.createObject("OrderDao");
	@Override
	public void saveOrder(Order order) throws Exception {
		//保存订单和订单项ThreadLocal
		try {
			//开启事务
			JDBCUtils.startTransaction();

			orderDao.saveOrder(order);
			for(OrderItem item : order.getList()) {
				orderDao.saveOrderItem(item);
			}
			JDBCUtils.commitAndClose();
		}catch(Exception e) {
			JDBCUtils.rollbackAndClose();
		}
	}

	@Override
	public PageModel findMyOrdersWithPage(User user, int curNum) throws Exception {
		//1.建PageModel对象，目的：计算并且携带分页参数
		int totalRecords = orderDao.getTotalRecords(user);
		PageModel pm = new PageModel(curNum,totalRecords,3);
		//2.关联集合
		List list = orderDao.findMyOrdersWithPage(user,pm.getStartIndex(),pm.getPageSize());
		pm.setList(list);
		//3.关联URL
		pm.setUrl("OrderServlet?method=findMyOrdersWithPage");
		return pm;
	}

	@Override
	public Order findOrderByOid(String oid) throws Exception {
		return orderDao.findOrderByOid(oid);
	}

	@Override
	public void updateOrder(Order order) throws Exception {
		orderDao.updateOrder(order);
	}
	
	

}
