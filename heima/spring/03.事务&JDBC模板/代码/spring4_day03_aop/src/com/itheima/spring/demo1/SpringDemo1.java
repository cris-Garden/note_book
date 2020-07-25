package com.itheima.spring.demo1;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Spring的AOP的注解开发
 * @author jt
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SpringDemo1 {
	@Resource(name="orderDao")
	private OrderDao orderDao;
	
	@Test
	public void demo1(){
		orderDao.save();
		orderDao.update();
		orderDao.delete();
		orderDao.find();
	}
}
