package cn.itcast.store.service.serviceImp;

import java.util.List;

import cn.itcast.store.dao.CategoryDao;
import cn.itcast.store.dao.daoImp.CategoryDaoImp;
import cn.itcast.store.domain.Category;
import cn.itcast.store.service.CategoryService;
import cn.itcast.store.utils.BeanFactory;
import cn.itcast.store.utils.JedisUtils;
import redis.clients.jedis.Jedis;

public class CategoryServiceImp implements CategoryService {

	CategoryDao categoryDao = (CategoryDao) BeanFactory.createObject("CategoryDao");
	@Override
	public List<Category> getAllCats() throws Exception {
		List<Category>list = categoryDao.getAllCats();
		return list;
	}

	@Override
	public void addCategory(Category category) throws Exception {
		//本质是向Mysql插入一条数据
		categoryDao.addCategory(category);
		//更新redis缓存
		Jedis jedis = JedisUtils.getJedis();
		jedis.del("allCats");
		JedisUtils.closeJedis(jedis);
		
	}
	
}
