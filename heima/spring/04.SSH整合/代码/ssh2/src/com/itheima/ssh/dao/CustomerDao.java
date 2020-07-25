package com.itheima.ssh.dao;

import java.util.List;

import com.itheima.ssh.domain.Customer;

/**
 * 客户管理的DAO层的接口
 * @author jt
 *
 */
public interface CustomerDao {

	void save(Customer customer);
	
	void update(Customer customer);
	
	void delete(Customer customer);
	
	Customer findById(Long cust_id);
	
	List<Customer> findAllByHQL();
	
	List<Customer> findAllByQBC();
	
	List<Customer> findAllByNamedQuery();
	
	

}
