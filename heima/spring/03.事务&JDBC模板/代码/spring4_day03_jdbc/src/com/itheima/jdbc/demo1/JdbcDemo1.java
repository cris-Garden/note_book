package com.itheima.jdbc.demo1;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * JDBC模板的使用
 * @author jt
 *
 */
public class JdbcDemo1 {

	@Test
	// jdbc模板的使用类似于Dbutils.
	public void demo1(){
		// 创建连接池:
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql:///spring4_day03");
		dataSource.setUsername("root");
		dataSource.setPassword("abc");
		// 创建jdbc模板
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		jdbcTemplate.update("insert into account values (null,?,?)", "赵冠希",10000d);
	}
}
