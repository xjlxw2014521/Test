package cn.itcast.store.web.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import cn.itcast.store.domain.Category;
import cn.itcast.store.domain.PageModel;
import cn.itcast.store.domain.Product;
import cn.itcast.store.service.CategoryService;
import cn.itcast.store.service.ProductService;
import cn.itcast.store.service.serviceImp.CategoryServiceImp;
import cn.itcast.store.service.serviceImp.ProductServiceImp;
import cn.itcast.store.utils.UUIDUtils;
import cn.itcast.store.utils.UploadUtils;
import cn.itcast.store.web.base.BaseServlet;

/**
 * Servlet implementation class AdminProductServlet
 */
@WebServlet("/AdminProductServlet")
public class AdminProductServlet extends BaseServlet {
	//findAllProductsWithPage
	public String findAllProductsWithPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取当前页
		int curNum = Integer.parseInt(request.getParameter("num"));
		//调用业务层查询全部商品信息返回PageModel
		ProductService productService = new ProductServiceImp();
		PageModel pageModel = productService.findAllProductsWithPage(curNum);
		//pageModel放入request
		request.setAttribute("page", pageModel);
		//转发到/admin/product/list.jsp
		return "/admin/product/list.jsp";
	}
	//addProductUI
	public String addProductUI(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//调用服务层，获取全部分类信息
		CategoryService categoryService = new CategoryServiceImp();
		List<Category>list = categoryService.getAllCats();
		//将分类信息存入reqeust中
		request.setAttribute("allCats", list);
		//转发到/admin/product/add.jsp
		return "/admin/product/add.jsp";
	}
	//addProduct
	public String addProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,String>map = new HashMap<String,String>();
		
		try {
			//获取form表单上传的信息
			//利用request.getInputStream();获取到请求体中全部数据，进行拆分和封装
			DiskFileItemFactory fac = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(fac);			
			List<FileItem>list = upload.parseRequest(request);
			/*
			 * 遍历集合 
			 */
			for (FileItem item : list) {
				if(item.isFormField()) {
					//如果当前的fileItem对象是普通项 
					//将普通项上name属性的值作为域，将获取到的内容作为值，放入MAP中
					map.put(item.getFieldName(), item.getString("utf-8"));
				}else {
					/*
					 * 如果当前fileItem对象是上传项 通过fileItem获取到输入流对象，通过输入流可以获取到图片二进制数据
					 * 在服务端创建一个空文件（后缀必须和上传到服务端的文件名后缀一致
					 * 建立和空文件对应的输出流
					 * 将输入流中的数据刷到输出流中
					 * 释放资源
					 * 向map中存入一个键值对的数据userhead<===>/image/11.bmp
					 */
					String oldFileName = item.getName();
					String newFileName = UploadUtils.getUUIDName(oldFileName);
		
					InputStream is = item.getInputStream();
					String realPath = getServletContext().getRealPath("/products/3/");
					String dir = UploadUtils.getDir(newFileName);
					String path = realPath+dir;
					File newDir = new File(path);
					if(!newDir.exists()) {
						newDir.mkdirs();
					}
					
					File finalFile = new File(newDir,newFileName);
					if(!finalFile.exists()) {
						finalFile.createNewFile();
					}
					OutputStream os = new FileOutputStream(finalFile);
					IOUtils.copy(is, os);
					IOUtils.closeQuietly(is);
					IOUtils.closeQuietly(os);
					map.put("pimage", "/products/3/"+dir+"/"+newFileName);
				}
			}
			/*
			 * 利用BeanUtils将map中的数据填充到user对象上
			 */
			Product product = new Product();
			BeanUtils.populate(product, map);
			product.setPid(UUIDUtils.getId());
			product.setPdate(new Date());
			product.setPflag(0);
			/*
			 * 调用service_dao将user上携带的数据存入数据创库，重定向到查询全部商品信息路径
			 *调用服务层存储商品信息
			 */
			ProductService productService = new ProductServiceImp();
			productService.saveProduct(product);

			//重定向到/store_v1/AdminProductServlet?method=findAllProductsWithPage&num=1
			response.sendRedirect("/store_v1/AdminProductServlet?method=findAllProductsWithPage&num=1");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
