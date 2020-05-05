package cn.itcast.store.dao.daoImp;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.store.dao.OrderDao;
import cn.itcast.store.domain.Order;
import cn.itcast.store.domain.OrderItem;
import cn.itcast.store.domain.Product;
import cn.itcast.store.domain.User;
import cn.itcast.store.utils.JDBCUtils;

public class OrderDaoImp implements OrderDao {

	@Override
	public void saveOrder(Order order) throws Exception {
		String sql = "INSERT INTO orders VALUES(?,?,?,?,?,?,?,?);";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		Object[] params = {order.getOid(),order.getOrderTime(),order.getTotal(),order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getUser().getUid()};
		qr.update(sql, params);
	}

	@Override
	public void saveOrderItem(OrderItem item) throws Exception {
		String sql = "INSERT INTO orderitem VALUES(?,?,?,?,?);";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		Object[] params = {item.getItemid(),item.getQuantity(),item.getTotal(),item.getProduct().getPid(),item.getOrder().getOid()};
		qr.update(sql, params);
	}

	@Override
	public int getTotalRecords(User user) throws Exception {
		String sql = "SELECT COUNT(*) FROM orders o WHERE o.uid=?;";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		Long num = (Long)qr.query(sql,new ScalarHandler(),user.getUid());
		return num.intValue();
	}

	@Override
	public List findMyOrdersWithPage(User user, int startIndex, int pageSize) throws Exception {
		String sql ="SELECT * from orders o where o.uid=? LIMIT ?,?;";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		List<Order>list = qr.query(sql, new BeanListHandler<Order>(Order.class),user.getUid(),startIndex,pageSize);

		//遍历订单，获取订单项
		//SELECT * FROM orderitem o,product p WHERE o.pid=p.pid AND o.oid=?;
		String sql01 = "SELECT * FROM orderitem o,product p WHERE o.pid=p.pid AND o.oid=?;";
		for(Order order : list) {
			//获取到每笔订单oid，查询每笔订单下的订单项以及订单项对应的商品信息
			String oid = order.getOid();
			List<Map<String, Object>> list02 = qr.query(sql01, new MapListHandler(),oid);
			//遍历list02
			for(Map<String, Object> map : list02) {
				OrderItem orderItem = new OrderItem();
				Product product = new Product();
				//创建时间类型的转换器
				DateConverter dt = new DateConverter();
				//设置转换的格式
				dt.setPattern("yyyy-MM-dd");
				//注册转换器
				ConvertUtils.register(dt, java.util.Date.class);
				
				//将map中属于orderItem的数据自动填充到orderItem对象上
				BeanUtils.populate(orderItem, map);
				BeanUtils.populate(product, map);
				
				orderItem.setProduct(product);
				order.getList().add(orderItem);
			}
		}
		
		return list;
	}

	@Override
	public Order findOrderByOid(String oid) throws Exception {
		String sql = "SELECT * FROM orders WHERE oid=?;";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		Order order = qr.query(sql, new BeanHandler<Order>(Order.class),oid);
		//遍历订单，获取订单项
		//SELECT * FROM orderitem o,product p WHERE o.pid=p.pid AND o.oid=?;
		String sql01 = "SELECT * FROM orderitem o,product p WHERE o.pid=p.pid AND o.oid=?;";
		
		List<Map<String, Object>> list02 = qr.query(sql01, new MapListHandler(),oid);
		//遍历list02
		for(Map<String, Object> map : list02) {
			OrderItem orderItem = new OrderItem();
			Product product = new Product();
			//创建时间类型的转换器
			DateConverter dt = new DateConverter();
			//设置转换的格式
			dt.setPattern("yyyy-MM-dd");
			//注册转换器
			ConvertUtils.register(dt, java.util.Date.class);
				
			//将map中属于orderItem的数据自动填充到orderItem对象上
			BeanUtils.populate(orderItem, map);
			BeanUtils.populate(product, map);
			
			orderItem.setProduct(product);
			order.getList().add(orderItem);
		}				
		return order;
	}

	@Override
	public void updateOrder(Order order) throws Exception {
		String sql = "UPDATE orders SET ordertime=?,total=?,state=?,address=?,name=?,telephone=? WHERE oid=?;";
		QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
		Object[] params = {order.getOrderTime(),order.getTotal(),order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getOid()};
		qr.update(sql,params);
	}
	
	
}
