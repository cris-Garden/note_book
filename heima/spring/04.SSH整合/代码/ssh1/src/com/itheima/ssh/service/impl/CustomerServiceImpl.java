package com.itheima.ssh.service.impl;

import org.springframework.transaction.annotation.Transactional;

import com.itheima.ssh.dao.CustomerDao;
import com.itheima.ssh.domain.Customer;
import com.itheima.ssh.service.CustomerService;
/**
 * 客户管理的业务层的实现类
 * @author jt
 *
 */
@Transactional
public class CustomerServiceImpl implements CustomerService {

	// 注入DAO;
	private CustomerDao customerDao;
	
	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	@Override
	public void save(Customer customer) {
		System.out.println("Service中的save方法执行了...");
		customerDao.save(customer);
	}

}
