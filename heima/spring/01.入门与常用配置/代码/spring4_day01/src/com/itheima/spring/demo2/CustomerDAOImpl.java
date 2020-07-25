package com.itheima.spring.demo2;

public class CustomerDAOImpl implements CustomerDAO {
	
	public void setup(){
		System.out.println("CustomerDAOImpl被初始化了...");
	}

	@Override
	public void save() {
		System.out.println("CustomerDAOImpl的save方法执行了...");
	}
	
	public void destroy(){
		System.out.println("CustomerDAOImpl被销毁了...");
	}

}
