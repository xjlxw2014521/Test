package cn.itcast.store.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.store.domain.Category;
import cn.itcast.store.service.CategoryService;
import cn.itcast.store.service.serviceImp.CategoryServiceImp;
import cn.itcast.store.utils.UUIDUtils;
import cn.itcast.store.web.base.BaseServlet;

/**
 * Servlet implementation class AdminCategoryServlet
 */
@WebServlet("/AdminCategoryServlet")
public class AdminCategoryServlet extends BaseServlet {
	//findAllCats
	public String findAllCats(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取全部分类信息
		CategoryService categoryService= new CategoryServiceImp();
		List<Category>list = categoryService.getAllCats();
		//全部分类信息放入request
		request.setAttribute("allCats", list);
		//转发到/admin/category/list.jsp
		return "/admin/category/list.jsp";
	}
	
	//addCategoryUI
	public String addCategoryUI(HttpServletRequest request, HttpServletResponse response) throws Exception {
		///admin/category/add.jsp
		return "/admin/category/add.jsp";
	}
	
	//addCategory
	public String addCategory(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取分类名称
		String cname = request.getParameter("cname");
		//创建分类ID
		String id = UUIDUtils.getId();
		Category category = new Category();
		category.setCid(id);
		category.setCname(cname);
		//调用业务层添加分类功能
		CategoryService categoryService = new CategoryServiceImp();
		categoryService.addCategory(category);
		//重定向到查询全部分类信息 
		response.sendRedirect("/store_v1/AdminCategoryServlet?method=findAllCats");
		return null;
	}
	
	
	
	
}
