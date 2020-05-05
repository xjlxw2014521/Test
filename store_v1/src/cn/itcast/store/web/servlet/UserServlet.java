package cn.itcast.store.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import cn.itcast.store.domain.User;
import cn.itcast.store.service.UserService;
import cn.itcast.store.service.serviceImp.UserServiceImp;
import cn.itcast.store.utils.MailUtils;
import cn.itcast.store.utils.MyBeanUtils;
import cn.itcast.store.utils.UUIDUtils;
import cn.itcast.store.web.base.BaseServlet;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends BaseServlet {
	public String registUI(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		return "/jsp/register.jsp";
	}

	// userRegist
	public String userRegist(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 接收表单数据
		Map<String, String[]> map = request.getParameterMap();
//map的for循环遍历
//		for (Map.Entry<String, String[]>entry:map.entrySet()) {
//			System.out.println(entry.getKey()+"+++"+entry.getValue());
//		}
		// 返回路径
		String path = null;
		// 用户对象
		User user = null;
		// 激活码
		String code = null;
		try {
			user = new User();
			MyBeanUtils.populate(user, map);
			user.setUid(UUIDUtils.getId());
			user.setState(0);
			code = UUIDUtils.getCode();
			user.setCode(code);
			System.out.println(user.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 调用业务层注册功能
		UserService userService = new UserServiceImp();
		try {
			userService.userRegist(user);
			// 注册成功，向用户邮箱发送信息，跳转到提示页面
			// 发送邮件
			MailUtils.sendMail(user.getEmail(), user.getCode());
			// 返回一个注册成功信息
			request.setAttribute("msg", "Registing Sussecc！Welcome to come here~~~~~");
			// 跳转到详情界面
		} catch (Exception e) {
			// 注册失败，跳转到提示页面
			// 返回一个注册成功信息
			request.setAttribute("msg", "Registing Failling！Please to input your information again~~~~~");
		}
		// 返回界面
		path = "/jsp/info.jsp";
		return path;
	}

	// active
	public String active(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取激活码
		String code = request.getParameter("code");
		// 调用业务层完成激活功能
		UserService userService = new UserServiceImp();
		boolean flag = true;
		try {
			flag = userService.userActive(code);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 进行激活信息提示
		if (flag == true) {
			// 激活成功
			request.setAttribute("msg", "用户激活成功，请登录！");
			return "/jsp/login.jsp";
		} else {
			// 激活失败
			request.setAttribute("msg", "用户激活失败，请重新激活！");
			return "/jsp/info.jsp";
		}
	}

	// loginUI
	public String loginUI(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		return "/jsp/login.jsp";
	}

	// userLogin
	public String userLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取用户数据
		User user = new User();
		MyBeanUtils.populate(user, request.getParameterMap());

		// 调用业务层的登录功能
		UserService userService = new UserServiceImp();
		User user02 = null;
		try {
			user02 = userService.userLogin(user);
			// 用户等成功，将用户信息放入session中
			request.getSession().setAttribute("loginUser", user02);
			response.sendRedirect("/store_v1/index.jsp");
			return null;
		} catch (Exception e) {
			// 用户登录失败
			String msg = e.getMessage();
			// 向request放入失败的信息
			request.setAttribute("msg", msg);
			return "/jsp/login.jsp";
		}
	}

	// logOut 退出
	public String logOut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//清除session
		request.getSession().invalidate();
		//重新定位到首页
		response.sendRedirect("/store_v1/index.jsp");
		return null;
	}
}
