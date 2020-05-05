package cn.itcast.store.web.servlet;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.store.domain.Product;
import cn.itcast.store.service.ProductService;
import cn.itcast.store.service.serviceImp.ProductServiceImp;
import cn.itcast.store.web.base.BaseServlet;

/**
 * Servlet implementation class IndexServlet
 */
@WebServlet("/IndexServlet")
public class IndexServlet extends BaseServlet {
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		//调用业务层功能，获取全部分类信息，返回一个集合
		/*
		 * CategoryService categoryService = new CategoryServiceImp();
		 * List<Category>list = categoryService.getAllCats();
		 * req.setAttribute("allCats", list);
		 */
		//将返回的集合放入request
		
		//调用业务层查询最新商品，查询最热商品，返回两个集合
		ProductService productService = new ProductServiceImp();
		List<Product>list01 = productService.findHots();
		List<Product>list02 = productService.findNews();
		//将两个集合放入request中
		req.setAttribute("hots", list01);
		req.setAttribute("news", list02);

		//转发到真实的首页
		return "/jsp/index.jsp";
	}

}
