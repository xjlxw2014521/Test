package cn.itcast.store.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.store.domain.Category;
import cn.itcast.store.service.CategoryService;
import cn.itcast.store.service.serviceImp.CategoryServiceImp;
import cn.itcast.store.utils.JedisUtils;
import cn.itcast.store.web.base.BaseServlet;
import net.sf.json.JSONArray;
import redis.clients.jedis.Jedis;

/**
 * Servlet implementation class CategoryServlet
 */
@WebServlet("/CategoryServlet")
public class CategoryServlet extends BaseServlet {
	// findAllCats
	public String findAllCats(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		//在redis中获取全部分类信息
		Jedis jedis = JedisUtils.getJedis();
		String jsonStr = jedis.get("allCats");
		if(null==jsonStr || "".equals(jsonStr)) {
			// 调用业务层功能，获取全部分类信息，返回一个集合
			CategoryService categoryService = new CategoryServiceImp();
			List<Category> list = categoryService.getAllCats();
			//全部分类信息转化为JSON格式
			jsonStr = JSONArray.fromObject(list).toString();
			
			//将全部信息存入redis中
			jedis.set("allCats", jsonStr);
			
			//将全部分类信息响应到客户端
			resp.setContentType("application/json;charset=utf-8");
			resp.getWriter().print(jsonStr);
		}else {
			System.out.println("redis中有数据");
			//将全部分类信息响应到客户端
			resp.setContentType("application/json;charset=utf-8");
			resp.getWriter().print(jsonStr);
		}
		//释放资源
		JedisUtils.closeJedis(jedis);
		return null;
	}
}
