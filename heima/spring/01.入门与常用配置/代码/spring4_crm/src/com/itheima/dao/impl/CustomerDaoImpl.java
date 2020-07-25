package com.itheima.dao.impl;

import com.itheima.dao.CustomerDao;
import com.itheima.domain.Customer;

public class CustomerDaoImpl implements CustomerDao {

	@Override
	public void save(Customer customer) {
		System.out.println("CustomerDao中的save方法执行了...");
	}

}
