package cn.itcast.store.service.serviceImp;

import java.util.List;

import cn.itcast.store.dao.ProductDao;
import cn.itcast.store.dao.daoImp.ProductDaoImp;
import cn.itcast.store.domain.PageModel;
import cn.itcast.store.domain.Product;
import cn.itcast.store.service.ProductService;
import cn.itcast.store.utils.BeanFactory;

public class ProductServiceImp implements ProductService {

	ProductDao productDao = (ProductDao) BeanFactory.createObject("ProductDao");
	
	@Override
	public List<Product> findHots() throws Exception {
		return productDao.findHots();
	}

	@Override
	public List<Product> findNews() throws Exception {
		// TODO Auto-generated method stub
		return productDao.findNews();
	}

	@Override
	public Product findProductByPid(String pid) throws Exception {
		// TODO Auto-generated method stub
		return productDao.findProductByPid(pid);
	}
	
	@Override
	public PageModel findProductsByCidWithPage(String cid, int curNum) throws Exception {
		//1.创建PageModel对象:目的：计算分页参数
		//统计当前分类下商品个数select count(*) from product where cid=?;
		int totalRecords = productDao.findTotalRecords(cid);
		PageModel pm = new PageModel(curNum,totalRecords,13);
		//2.关联集合
		//select * from product where cid=? LIMIT ?,?;
		List list = productDao.findProductByCidWithPage(cid,pm.getStartIndex(),pm.getPageSize());
		pm.setList(list);
		//3.关联url
		pm.setUrl("ProductServlet?method=findProductsByCidWithPage&cid="+cid);
		return pm;
	}

	@Override
	public PageModel findAllProductsWithPage(int curNum) throws Exception {
		//创建对象,计算每个页面的容量
		int total = productDao.findTotalRecords();
		PageModel pageModel = new PageModel(curNum, total, 5);
		//关联集合,select * from product limit ?,?;
		List<Product> list = productDao.findAllProductsWithPage(pageModel.getStartIndex(),pageModel.getPageSize());
		pageModel.setList(list);
		//关联url
		pageModel.setUrl("AdminProductServlet?method=findAllProductsWithPage");
		return pageModel;
	}

	@Override
	public void saveProduct(Product product) throws Exception {
		productDao.saveProduct(product);
	}
	
	
}
