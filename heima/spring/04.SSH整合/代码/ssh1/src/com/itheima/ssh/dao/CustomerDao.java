package com.itheima.ssh.dao;

import com.itheima.ssh.domain.Customer;

/**
 * 客户管理的DAO层的接口
 * @author jt
 *
 */
public interface CustomerDao {

	void save(Customer customer);

}
