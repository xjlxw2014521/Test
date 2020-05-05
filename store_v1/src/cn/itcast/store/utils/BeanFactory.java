package cn.itcast.store.utils;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.itcast.store.dao.UserDao;
import cn.itcast.store.domain.User;


public class BeanFactory {
	public static Object createObject(String name){
		//解析XML
		//通过传递过来的name获取application.xml中name对应的class值
		
		//获取Document对象
		SAXReader reader = new SAXReader();	
		//如何获取application.xml文件的输入流
		InputStream is = BeanFactory.class.getClassLoader().getResourceAsStream("application.xml");
		Document doc;
		try {
			doc = reader.read(is);
			//通过Document对象获取根节点beans
			Element rootElement = doc.getRootElement();
			//通过根节点获取到根节点下所有的子节点bean，返回集合
			List<Element> list = rootElement.elements();
			//遍历集合，判断每个元素上的id的值是否和当前的name一致
			for (Element ele : list) {
				//ele相当于beans节点下的每个bean
				//获取到当前节点的id属性值
				String id = ele.attributeValue("id");
				if(id.equals(name)) {
					String str = ele.attributeValue("class");
					//通过反射创建对象并返回
					Class clazz = Class.forName(str);
					return clazz.newInstance();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void main(String[] args) throws SQLException {
		UserDao us = (UserDao) createObject("UserDao");
		User user = new User();
		user.setUsername("18774549098");
		user.setPassword("123456");
		User userLogin = us.userLogin(user);
		System.out.println(userLogin);
	}
}
