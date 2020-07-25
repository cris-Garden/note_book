package com.itheima.ssh.test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itheima.ssh.domain.Customer;
import com.itheima.ssh.service.CustomerService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SSHDemo1 {

	@Resource(name="customerService")
	private CustomerService customerService;
	
	@Test
	// 修改
	public void demo1(){
		Customer customer = customerService.findById(1l);
		customer.setCust_name("赵洪");
		customerService.update(customer);
	}
	
	@Test
	// 删除
	public void demo2(){
		Customer customer = customerService.findById(1l);
		customerService.delete(customer);
	}
	
	@Test
	// 查询所有：HQL
	public void demo3(){
		List<Customer> list = customerService.findAllByHQL();
		for (Customer customer : list) {
			System.out.println(customer);
		}
	}
	
	@Test
	// 查询所有：QBC
	public void demo4(){
		List<Customer> list = customerService.findAllByQBC();
		for (Customer customer : list) {
			System.out.println(customer);
		}
	}
	
	@Test
	// 查询所有：命名查询
	public void demo5(){
		List<Customer> list = customerService.findAllByNamedQuery();
		for (Customer customer : list) {
			System.out.println(customer);
		}
	}
}
