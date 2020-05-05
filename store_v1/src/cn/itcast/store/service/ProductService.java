package cn.itcast.store.service;

import java.util.List;

import cn.itcast.store.domain.PageModel;
import cn.itcast.store.domain.Product;

public interface ProductService {

	public List<Product> findHots() throws Exception;

	public List<Product> findNews() throws Exception;

	public Product findProductByPid(String pid) throws Exception;

	public PageModel findProductsByCidWithPage(String cid, int curNum) throws Exception;

	public PageModel findAllProductsWithPage(int curNum) throws Exception;

	public void saveProduct(Product product) throws Exception;

}
