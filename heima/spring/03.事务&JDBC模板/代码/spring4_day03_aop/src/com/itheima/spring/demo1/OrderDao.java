package com.itheima.spring.demo1;

public class OrderDao {

	public void save(){
		System.out.println("保存订单...");
	}
	public void update(){
		System.out.println("修改订单...");
	}
	public String delete(){
		System.out.println("删除订单...");
		return "赵冠希";
	}
	public void find(){
		System.out.println("查询订单...");
//		int d = 1/0;
	}
}
