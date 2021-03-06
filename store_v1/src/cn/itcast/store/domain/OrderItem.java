package cn.itcast.store.domain;

public class OrderItem {
	private String itemid;//id
	
	
	private int quantity;//数量
	private double total;//小计
	//1_product携带更多数据
	//2_面向对象角度,对象关联对象
	private Product product;
	
	
	private Order order;
	
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
}
