- [Mybatis基本数据操作](#mybatis基本数据操作)
	- [根据用户id查找](#根据用户id查找)
	- [根据用户名模糊查找](#根据用户名模糊查找)
	- [添加用户](#添加用户)
	- [根据用户id更新用户](#根据用户id更新用户)
	- [根据id删除用户](#根据id删除用户)
- [Mybatis测试程序流程](#mybatis测试程序流程)
- [Dao开发](#dao开发)
	- [原始Dao开发方式](#原始dao开发方式)
		- [1.配置maper文件](#1配置maper文件)
		- [2. Dao接口开发](#2-dao接口开发)
		- [3. Dao接口实现](#3-dao接口实现)
		- [测试程序](#测试程序)
		- [原始Dao测试缺点有哪些](#原始dao测试缺点有哪些)
	- [Mapper动态代理](#mapper动态代理)
		- [Mapper接口开发需要遵循的四个原则](#mapper接口开发需要遵循的四个原则)
		- [mapper动态代理开发](#mapper动态代理开发)
		- [Mapper自动代理是如何确定方法调用的是SelectOne方法还是SelectList方法。](#mapper自动代理是如何确定方法调用的是selectone方法还是selectlist方法)
- [SqlMapConfig.xml配置文件都有哪些节点](#sqlmapconfigxml配置文件都有哪些节点)
	- [properties（属性）](#properties属性)
	- [settings（全局配置参数）](#settings全局配置参数)
	- [typeAliases（类型别名）](#typealiases类型别名)
		- [mybatis默认支持别名](#mybatis默认支持别名)
		- [自定义别名](#自定义别名)
	- [typeHandlers（类型处理器）](#typehandlers类型处理器)
	- [objectFactory（对象工厂）](#objectfactory对象工厂)
	- [plugins（插件）](#plugins插件)
	- [environments（环境集合属性对象）](#environments环境集合属性对象)
	- [environment（环境子属性对象）](#environment环境子属性对象)
	- [transactionManager（事务管理）](#transactionmanager事务管理)
	- [dataSource（数据源）](#datasource数据源)
	- [mappers（映射器）](#mappers映射器)
		- [`<mapper resource=" " />` 配置](#mapper-resource---配置)
		- [`<mapper class=" " />` 配置](#mapper-class---配置)
		- [`<package name=""/>`配置](#package-name配置)
- [输入映射和输出映射](#输入映射和输出映射)
	- [输入参数映射](#输入参数映射)
		- [输入类型是一个基本类型](#输入类型是一个基本类型)
		- [输入类型是一个pojo](#输入类型是一个pojo)
		- [输入类型是一个pojo包装对象](#输入类型是一个pojo包装对象)
	- [返回值映射](#返回值映射)
		- [reslutType映射](#resluttype映射)
			- [基本类型返回](#基本类型返回)
			- [输出pojo对象](#输出pojo对象)
			- [输出pojo列表](#输出pojo列表)
		- [resultMap映射](#resultmap映射)
- [动态sql](#动态sql)
		- [Foreach遍历对象的属性](#foreach遍历对象的属性)
		- [Foreach如何遍历Array](#foreach如何遍历array)
		- [Foreach如何遍历List](#foreach如何遍历list)
- [关联查询](#关联查询)
- [Mybatis整合spring](#mybatis整合spring)
- [Mybatis逆向工程（了解）](#mybatis逆向工程了解)
- [Tips](#tips)
	- [resultType的使用要注意什么？](#resulttype的使用要注意什么)
	- [resultMap如何使用](#resultmap如何使用)
	- [parameterMap和parameterType区别？](#parametermap和parametertype区别)
	- [mapper通过什么来定位使用哪一中操作](#mapper通过什么来定位使用哪一中操作)
	- [namepace的作用是什么](#namepace的作用是什么)
	- [mybaits的日志配置打印日志等级](#mybaits的日志配置打印日志等级)
	- [Intteger和String等基本类型都是mybatis已经帮你配好了。](#intteger和string等基本类型都是mybatis已经帮你配好了)
	- [#{}和${}区别](#和区别)
	- [mybatis和hiberate的区别](#mybatis和hiberate的区别)

# Mybatis基本数据操作

## 根据用户id查找
```xml
<select id="findByID" parameterType="Integer" resultType="com.itheima.pojo.User">
		select * from user where id = #{v}
</select>
```
```java
	@Test
	public void testMybatis() throws Exception {
		// 加载核心配置文件
		String resource = "sqlMapConfig.xml";
		InputStream in = Resources.getResourceAsStream(resource);
		// 创建SqlSessionFactory
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);

		System.out.println("bb");

		// 创建SqlSession
		SqlSession sqlSession = sqlSessionFactory.openSession();

		System.out.println("aa");

		// 执行Sql语句
		User user = sqlSession.selectOne("test.findByID", 10);

		System.out.println(user);

		sqlSession.close();
	}
```

## 根据用户名模糊查找
```xml
<!-- 根据用户名模糊查找 -->
<select id="findUserByUserName" parameterType="String" resultType="com.itheima.pojo.User">
		select * from user where username like '%${value}%'
</select>
```
```java
    @Test
	public void findUserByUserName() throws Exception {
		// 加载核心配置文件
		String resource = "sqlMapConfig.xml";
		InputStream in = Resources.getResourceAsStream(resource);
		// 创建SqlSessionFactory
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);

		System.out.println("bb");

		// 创建SqlSession
		SqlSession sqlSession = sqlSessionFactory.openSession();

		System.out.println("aa");

		// 执行Sql语句
		List<User> users = sqlSession.selectList("test.findUserByUserName", "五");

		for (User user : users) {
			System.out.println(user);
		}

		sqlSession.close();
	}
```

##  添加用户
添加用户配置解析。#{username}为什么不使用#{user.username}是因为com.itheima.pojo.User与数据表的字段名字一一对应
```xml
    <!-- 添加用户 -->
	<insert id="insertUser" parameterType="com.itheima.pojo.User">
	    <!-- selectkey id:表示查找的id放到User的id里面，Integer表示类型，AFTER：表示id是在插入操作执行之后。Oracle是表示id是在插入操作执行之前就生成id然后执行插入 -->
		<selectKey keyProperty="id" resultType="Integer" order="AFTER">
			select LAST_INSERT_ID()
		</selectKey>
		insert into user (username,birthday,address,sex) 
		values (#{username},#{birthday},#{address},#{sex})
	</insert>
```
```java
    // 添加用户
	@Test
	public void testInsertUser() throws Exception {
		// 加载核心配置文件
		String resource = "sqlMapConfig.xml";
		InputStream in = Resources.getResourceAsStream(resource);
		// 创建SqlSessionFactory
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
		// 创建SqlSession
		SqlSession sqlSession = sqlSessionFactory.openSession();

		// 执行Sql语句
		User user = new User();
		user.setUsername("何炅");
		user.setBirthday(new Date());
		user.setAddress("sadfsafsafs");
		user.setSex("男");
		int i = sqlSession.insert("test.insertUser", user);
		sqlSession.commit();
		System.out.println(user.getId());
	}
```

## 根据用户id更新用户
```xml
    <!-- 更新 -->
	<update id="updateUserById" parameterType="com.itheima.mybatis.pojo.User">
		update user 
		set username = #{username},sex = #{sex},birthday = #{birthday},address = #{address}
		where id = #{id}
	</update>
```
```java
    // 更新用户
	@Test
	public void testUpdateUserById() throws Exception {
		// 加载核心配置文件
		String resource = "sqlMapConfig.xml";
		InputStream in = Resources.getResourceAsStream(resource);
		// 创建SqlSessionFactory
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
		// 创建SqlSession
		SqlSession sqlSession = sqlSessionFactory.openSession();

		// 执行Sql语句
		User user = new User();
		user.setId(29);
		user.setUsername("何炅292929");
		user.setBirthday(new Date());
		user.setAddress("222222sadfsafsafs");
		user.setSex("女");
		int i = sqlSession.update("test.updateUserById", user);
		sqlSession.commit();
	}
```
## 根据id删除用户
```xml
    <delete id="deleteUserById" parameterType="Integer">
		delete from user 
		where id = #{vvvvv}
	</delete>
```
```java
    // 删除
	@Test
	public void testDelete() throws Exception {
		// 加载核心配置文件
		String resource = "sqlMapConfig.xml";
		InputStream in = Resources.getResourceAsStream(resource);
		// 创建SqlSessionFactory
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
		// 创建SqlSession
		SqlSession sqlSession = sqlSessionFactory.openSession();

		sqlSession.delete("test.deleteUserById", 27);
		sqlSession.commit();
	}
```

# Mybatis测试程序流程
测试程序步骤：
 1. 创建SqlSessionFactoryBuilder对象
 2. 加载SqlMapConfig.xml配置文件
 3. 创建SqlSessionFactory对象
 4. 创建SqlSession对象
 5. 执行SqlSession对象执行查询，获取结果User
 6. 打印结果
 7. 释放资源

# Dao开发
Maybatis的Dao的两种开发方式

## 原始Dao开发方式
原始Dao开发方法需要程序员编写Dao接口和Dao实现类。
### 1.配置maper文件
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="test">
	<select id="findByID" parameterType="Integer" resultType="com.itheima.pojo.User">
		select * from user where id = #{v}
	</select>
	
	<!-- 根据用户名模糊查找 -->
	<select id="findUserByUserName" parameterType="String" resultType="com.itheima.pojo.User">
		select * from user where username like '%${value}%'
	</select>
	
	<!-- 添加用户 -->
	<insert id="insertUser" parameterType="com.itheima.pojo.User">
	    <!-- selectkey id:表示查找的id放到User的id里面，Integer表示类型，AFTER：表示id是在插入操作执行之后。Oracle是表示id是在插入操作执行之前就生成id然后执行插入 -->
		<selectKey keyProperty="id" resultType="Integer" order="AFTER">
			select LAST_INSERT_ID()
		</selectKey>
		insert into user (username,birthday,address,sex) 
		values (#{username},#{birthday},#{address},#{sex})
	</insert>
	
	<!-- 更新 -->
	<update id="updateUserById" parameterType="com.itheima.pojo.User">
		update user 
		set username = #{username},sex = #{sex},birthday = #{birthday},address = #{address}
		where id = #{id}
	</update>
	
	<!-- 删除 -->
	<delete id="deleteUserById" parameterType="Integer">
		delete from user 
		where id = #{vvvvv}
	</delete>
	
</mapper>
```
### 2. Dao接口开发
```java
package com.itheima.mybatis.dao;

import com.itheima.mybatis.pojo.User;

public interface UserDao {

	//通过用户ID查询一个用户
	public User selectUserById(Integer id);
}
```
### 3. Dao接口实现

```java
package com.itheima.mybatis.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.itheima.mybatis.pojo.User;

/**
 * Dao
 * @author lx
 *
 */
public class UserDaoImpl implements UserDao {

	//注入
	private SqlSessionFactory sqlSessionFactory;
	public UserDaoImpl(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	//通过用户ID查询一个用户
	public User selectUserById(Integer id){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		return sqlSession.selectOne("test.findUserById", id);
	}
	
}

```

### 测试程序

@Before 注解表示测试开始需要执行的公共部分。

```java
package com.itheima.mybatis.junit;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import com.itheima.mybatis.dao.UserDao;
import com.itheima.mybatis.dao.UserDaoImpl;
import com.itheima.mybatis.pojo.User;

public class MybatisDaoTest {

	public SqlSessionFactory sqlSessionFactory;
	@Before
	public void before() throws Exception {
		//加载核心配置文件
		String resource = "sqlMapConfig.xml";
		InputStream in = Resources.getResourceAsStream(resource);
		//创建SqlSessionFactory
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
	}
	@Test
	public void testDao() throws Exception {
		
		UserDao userDao = new UserDaoImpl(sqlSessionFactory);
		
		User user = userDao.selectUserById(10);
		System.out.println(user);
	}
}
```

### 原始Dao测试缺点有哪些
原始Dao开发中存在以下问题：
* Dao方法体存在重复代码：通过SqlSessionFactory创建SqlSession，调用SqlSession的数据库操作方法
* 调用sqlSession的数据库操作方法需要指定statement的id，这里存在硬编码，不得于开发维护。
* 需要手动实现接口


## Mapper动态代理

Mapper接口开发方法只需要程序员编写Mapper接口（相当于Dao接口），由Mybatis框架根据接口定义创建接口的动态代理对象，代理对象的方法体同上边Dao接口实现类方法（由SqlSession类来完成，即实现类不需要写）。

### Mapper接口开发需要遵循的四个原则
1.	Mapper.xml文件中的namespace与mapper接口的类路径相同。
2.	Mapper接口方法名和Mapper.xml中定义的每个statement的id相同 
3.	Mapper接口方法的输入参数类型和mapper.xml中定义的每个sql 的parameterType的类型相同
4.	Mapper接口方法的输出参数类型和mapper.xml中定义的每个sql的resultType的类型相同

### mapper动态代理开发

创建Mapper接口

```java
package com.itheima.mapper;

import com.itheima.pojo.User;

public interface UserMapper {

	
	//遵循四个原则
	//接口 方法名  == User.xml 中 id 名
	//返回值类型  与  Mapper.xml文件中返回值类型要一致
	//方法的入参类型 与Mapper.xml中入参的类型要一致
	//命名空间 绑定此接口
	public User findUserById(Integer id);
	
}

```
配置

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- 写Sql语句   -->
<mapper namespace="com.itheima.mapper.UserMapper">
	<!-- 通过ID查询一个用户 -->
	<select id="findUserById" parameterType="Integer" resultType="com.itheima.pojo.User">
		select * from user where id = #{v}
	</select>

</mapper>
```

配置sqlMapConfig.xml
```xml
....
	<!-- Mapper的位置 Mapper.xml 写Sql语句的文件的位置 -->
	<mappers>
		<mapper resource="com/itheima/mapper/UserMapper.xml" />
	</mappers>

....
```
测试
```java
package com.itheima.unit_test;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;


public class MybatisMapperTest {

	
	@Test
	public void testMapper() throws Exception {
		//加载核心配置文件
		String resource = "sqlMapConfig.xml";
		InputStream in = Resources.getResourceAsStream(resource);
		//创建SqlSessionFactory
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
		//创建SqlSession
		SqlSession sqlSession = sqlSessionFactory.openSession();
		
		//SqlSEssion帮我生成一个实现类  （给接口）
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		
		
		User user = userMapper.findUserById(10);
		System.out.println(user);
	}
}

```


### Mapper自动代理是如何确定方法调用的是SelectOne方法还是SelectList方法。
动态代理对象调用sqlSession.selectOne()和sqlSession.selectList()是根据mapper接口方法的返回值决定，如果返回list则调用selectList方法，如果返回单个对象则调用selectOne方法。

###

# SqlMapConfig.xml配置文件都有哪些节点

## properties（属性）

jdbc.properties文件

```javascript
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/mybatis?characterEncoding=utf-8
jdbc.username=root
jdbc.password=root
```
SqlMapConfig.xml配置
```xml
<properties resource="jdbc.properties"/>
```
属性的使用
`${jdbc.driver},${jdbc.user},${jdbc.password}`是对属性的引用
```xml
	<!-- 和spring整合后 environments配置将废除 -->
	<environments default="development">
		<environment id="development">
			<!-- 使用jdbc事务管理 -->
			<transactionManager type="JDBC" />
			<!-- 数据库连接池 -->
			<dataSource type="POOLED">
				<property name="driver" value="${jdbc.driver}" />
				<property name="url"
					value="jdbc:mysql://localhost:3306/mybatis?characterEncoding=utf-8" />
				<property name="username" value="${jdbc.username}" />
				<property name="password" value="${jdbc.password}" />
			</dataSource>
		</environment>
	</environments>

```
## settings（全局配置参数）
## typeAliases（类型别名）

### mybatis默认支持别名

|别名| 映射的类型|
|:--- | :---|
|_byte|byte |
|_long |	long |
|_short |short |
|_int| 	int |
|_integer| 	int |
|_double |double |
|_float |float |
|_boolean |	boolean |
|string |String| 
|byte |Byte| 
|long |Long |
|short |Short |
|int |Integer |
|integer |Integer |
|double |Double |
|float |Float |
|boolean |Boolean |
|date |Date |
|decimal |BigDecimal |
|bigdecimal |BigDecimal |
|map|Map|

### 自定义别名

```xml
<typeAliases>
	<!-- 类型别名 取完别名之后在mapper的配置文件可以用User替代com.itheima.mybatis.pojo.User	 -->
	<typeAlias type="com.itheima.mybatis.pojo.User" alias="User"/>
	<!-- 包别名 导入包下或者子包所有的类以大写小写都可以的方式命名别名 User或user都可以	 -->
	<package name="com.itheima.mybatis.pojo"/>
</typeAliases>
```

## typeHandlers（类型处理器）
## objectFactory（对象工厂）
## plugins（插件）
## environments（环境集合属性对象）
## environment（环境子属性对象）
## transactionManager（事务管理）
## dataSource（数据源）
## mappers（映射器）

mapper的几种配置方法,有一种url方式配置没什么意义可以不用。
项目强烈推荐使用package。

### `<mapper resource=" " />` 配置
使用相对于类路径的资源（现在的使用方式）

如：`<mapper resource="sqlmap/User.xml" />`

### `<mapper class=" " />` 配置

使用mapper接口类路径

如：`<mapper class="cn.itcast.mybatis.mapper.UserMapper"/>`

注意：此种方法要求mapper接口名称和mapper映射文件名称相同，且放在同一个目录中。

### `<package name=""/>`配置

注册指定包下的所有mapper接口

如：`<package name="cn.itcast.mybatis.mapper"/>`

注意：此种方法要求mapper接口名称和mapper映射文件名称相同，且放在同一个目录中。

# 输入映射和输出映射

配置一个输入pojo（下面的例子里通用）
```java
package com.itheima.mybatis.pojo;

import java.io.Serializable;
import java.util.List;
/**
 * new Message
 * @author lx
 *
 */
public class QueryVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private User user;
	
	List<Integer> idsList;
	
	Integer[] ids;
	
	
	public List<Integer> getIdsList() {
		return idsList;
	}
	public void setIdsList(List<Integer> idsList) {
		this.idsList = idsList;
	}
	public Integer[] getIds() {
		return ids;
	}
	public void setIds(Integer[] ids) {
		this.ids = ids;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

}

```

Mapper.xml映射文件中定义了操作数据库的sql，每个sql是一个statement，映射文件是mybatis的核心。
## 输入参数映射

### 输入类型是一个基本类型
[参考](#mapper动态代理开发)
### 输入类型是一个pojo
[参考](#根据用户id更新用户)
### 输入类型是一个pojo包装对象
[参考](#输出pojo列表)

## 返回值映射

将sql的操作结果返回给程序的过程主要同reslutType（自动）和resultMap（手动）两种方式

### reslutType映射

#### 基本类型返回
配置xml
```xml
	<select id="countUser" resultType="Integer">
	 	select count(1) from user
	 </select>
```
编写mapper
```java
public interface UserMapper {
	.....
	//查询数据条数
	public Integer countUser();
	....
}
```

测试
```java
	@Test
	public void testMapperQueryVoCOunt() throws Exception {
		//加载核心配置文件
		String resource = "sqlMapConfig.xml";
		InputStream in = Resources.getResourceAsStream(resource);
		//创建SqlSessionFactory
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
		//创建SqlSession
		SqlSession sqlSession = sqlSessionFactory.openSession();
		
		//SqlSEssion帮我生成一个实现类  （给接口）
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		
		Integer i = userMapper.countUser();
		System.out.println(i);
	}
```
#### 输出pojo对象
配置xml
```xml

```
配置mapper类
```java
public interface UserMapper {
	....
	public User findUserById(Integer id);
	....
}
```
测试
```java
	@Test
	public void testMapper() throws Exception {
		//加载核心配置文件
		String resource = "sqlMapConfig.xml";
		InputStream in = Resources.getResourceAsStream(resource);
		//创建SqlSessionFactory
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
		//创建SqlSession
		SqlSession sqlSession = sqlSessionFactory.openSession();
		
		//SqlSEssion帮我生成一个实现类  （给接口）
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User user = userMapper.findUserById(10);
		System.out.println(user);
	}
```

#### 输出pojo列表

配置xml
```xml
	<!-- 根据用户名模糊查询 -->
	<select id="findUserByQueryVo" parameterType="QueryVo" resultType="com.itheima.mybatis.pojo.User">
		select * from user where username like "%"#{user.username}"%"
	</select>
```
配置mapper类
```java
public interface UserMapper {
	....
	public List<User> findUserByQueryVo(QueryVo vo);
	....
}
```
测试
```java
	@Test
	public void testMapperQueryVo() throws Exception {
		//加载核心配置文件
		String resource = "sqlMapConfig.xml";
		InputStream in = Resources.getResourceAsStream(resource);
		//创建SqlSessionFactory
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
		//创建SqlSession
		SqlSession sqlSession = sqlSessionFactory.openSession();
		
		//SqlSEssion帮我生成一个实现类  （给接口）
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		QueryVo vo = new QueryVo();
		User user = new User();
		user.setUsername("五");
		vo.setUser(user);
		
		List<User> us = userMapper.findUserByQueryVo(vo);
		for (User u : us) {
			System.out.println(u);
			
		}
	}
```
### resultMap映射


resultType可以指定将查询结果映射为pojo，但需要pojo的属性名和sql查询的列名一致方可映射成功。
	如果sql查询字段名和pojo的属性名不一致，可以通过resultMap将字段名和属性名作一个对应关系 ，resultMap实质上还需要将查询结果映射到pojo对象中。
	resultMap可以实现将查询结果映射为复杂类型的pojo，比如在查询结果映射对象中包括pojo和list实现一对一查询和一对多查询。

需求：查询订单表order的所有数据
sql：SELECT id, user_id, number, createtime, note FROM `order`

user_id名字不一样如果是用自动映射会自动被忽略为null


xml配置
```xml
	<resultMap type="Orders" id="orders">
		<result column="user_id" property="userId"/>
	</resultMap>
	
	<select id="selectOrdersList" resultMap="orders">
		SELECT id, user_id, number, createtime, note FROM orders 
	</select>
```
这里resultMap的标签可以只适配不一致的部分，其他智能适配。

pojo
```java
package com.itheima.mybatis.pojo;

import java.io.Serializable;
import java.util.Date;

public class Orders  implements Serializable{
    @Override
	public String toString() {
		return "Orders [id=" + id + ", userId=" + userId + ", number=" + number + ", createtime=" + createtime
				+ ", note=" + note + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer userId;

    private String number;

    private Date createtime;

    private String note;
    
    //附加对象  用户对象
    private User user;
    
    
    
    
    

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number == null ? null : number.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }   
}
```

测试
```java
	//	查询订单表order的所有数据
	@Test
	public void testOrderList() throws Exception {
		//加载核心配置文件
		String resource = "sqlMapConfig.xml";
		InputStream in = Resources.getResourceAsStream(resource);
		//创建SqlSessionFactory
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
		//创建SqlSession
		SqlSession sqlSession = sqlSessionFactory.openSession();
		
		OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
		
		List<Orders> ordersList = mapper.selectOrdersList();
		for (Orders orders : ordersList) {
			System.out.println(orders);
		}
	}
```

# 动态sql

通过mybatis提供的各种标签方法实现动态拼接sql。

需求：根据性别和名字查询用户
查询sql：
SELECT id, username, birthday, sex, address FROM `user` WHERE sex = 1 AND username LIKE '%张%'

##	If标签

单纯的使用if语句，如果sex为空的情况会出错，需要在where后面加一个1=1来解决为空的错误。

xml 配置
```xml
<select id="queryUserByWhere" parameterType="user" resultType="user">
	SELECT id, username, birthday, sex, address FROM `user`
	WHERE 1=1
	<if test="sex != null and sex != ''">
		AND sex = #{sex}
	</if>
	<if test="username != null and username != ''">
		AND username LIKE
		'%${username}%'
	</if>
</select>
```

##	Where标签

上面的sql还有where 1=1 这样的语句，很麻烦
可以使用where标签进行改造

根据性别和名字查询用户  where 可以去掉第一个前and,后and是不可以的。

```xml
<!-- 根据条件查询用户 -->
<select id="queryUserByWhere" parameterType="user" resultType="user">
	SELECT id, username, birthday, sex, address FROM `user`
<!-- where标签可以自动添加where，同时处理sql语句中第一个and关键字 -->
	<where>
		<if test="sex != null">
			AND sex = #{sex}
		</if>
		<if test="username != null and username != ''">
			AND username LIKE
			'%${username}%'
		</if>
	</where>
</select>

```
##	Sql片段
Sql中可将重复的sql提取出来，使用时用include引用即可，最终达到sql重用的目的。

如果要使用别的Mapper.xml配置的sql片段，可以在refid前面加上对应的Mapper.xml的namespace

把上面例子中的id, username, birthday, sex, address提取出来，作为sql片段，如下：

```xml
<!-- 根据条件查询用户 -->
<select id="queryUserByWhere" parameterType="user" resultType="user">
	<!-- SELECT id, username, birthday, sex, address FROM `user` -->
	<!-- 使用include标签加载sql片段；refid是sql片段id -->
	SELECT <include refid="userFields" /> FROM `user`
	<!-- where标签可以自动添加where关键字，同时处理sql语句中第一个and关键字 -->
	<where>
		<if test="sex != null">
			AND sex = #{sex}
		</if>
		<if test="username != null and username != ''">
			AND username LIKE
			'%${username}%'
		</if>
	</where>
</select>

<!-- 声明sql片段 -->
<sql id="userFields">
	id, username, birthday, sex, address
</sql>

```
##	Foreach标签

### Foreach遍历对象的属性
Mapper接口方法
```java

```

Mapper配置文件
```xml

```

### Foreach如何遍历Array

### Foreach如何遍历List




# 关联查询

互联网开发拒绝复杂的sql查询，拒绝多对多查询。因为带宽太大的话消耗资源

对于商品的数据模型
以订单为中心，订单和用户的关系是一对一关系。
以用户为中心，订单和用户的关系是一对多关系。

一旦使用了一对一和一对多之后返回的类型一定是手动映射reslutMap。

##	一对一关联
##	一对多关联

# Mybatis整合spring
##	如何整合spring
##	使用原始的方式开发dao
##	使用Mapper接口动态代理
# Mybatis逆向工程（了解）
dao，pojo，mapper自动生成
了解一下就好。只能是单表查询，全表查询，不能多表查询。不能sql优化。

# Tips

## resultType的使用要注意什么？
maper.xml的mapper节点的select的resultType属性的自动映射的条件是pojo的属性名必须和数据表的字段名完全一样否则需要手动映射。

```xml
<select id="queryUserById" parameterType="int"
		resultType="cn.itcast.mybatis.pojo.User">
		SELECT * FROM `user` WHERE id  = #{id}
</select>
```

## resultMap如何使用
mapper的resultMap属性表示手动映射

## parameterMap和parameterType区别？
mapper的select标签的parameterMap已经废弃不用了。现在用的是parameterType。


## mapper通过什么来定位使用哪一中操作
mapper的自标签通过id来区分具体调用的是哪一个标签。

## namepace的作用是什么
mapper的namespace属性是用于区分其他mapper同名的id

```xml
<mapper namespace="test">

	<!-- id:statement的id 或者叫做sql的id-->
	<!-- parameterType:声明输入参数的类型 -->
	<!-- resultType:声明输出结果的类型，应该填写pojo的全路径 -->
	<!-- #{}：输入参数的占位符，相当于jdbc的？ -->
	<select id="queryUserById" parameterType="int"
		resultType="cn.itcast.mybatis.pojo.User">
		SELECT * FROM `user` WHERE id  = #{id}
	</select>

</mapper>

```

## mybaits的日志配置打印日志等级

```python
log4j.rootLogger=DEBUG, stdout
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n

# mapper 包下的所有接口的方法都会打日志，会打印不低于 DEBUG 级别的日志
log4j.logger.org.mybatis.mapper=TRACE
```

## Intteger和String等基本类型都是mybatis已经帮你配好了。
## #{}和${}区别
#{x}:表示占位符号（预编译）等价于‘xx’会自带双引号，${}:表示字符串拼接（不是预编译，不防止SQL注入）,$变量只能用value来表示。
```sql

select * from user where username like '%${value}%' //不防止sql注入

select * from user where username like "%"%#{v}"%" //防止sql注入

```

## mybatis和hiberate的区别

Mybatis和hibernate不同，它不完全是一个ORM框架，因为MyBatis需要程序员自己编写Sql语句。mybatis可以通过XML或注解方式灵活配置要运行的sql语句，并将java对象和sql语句映射生成最终执行的sql，最后将sql执行的结果再映射生成java对象。

Mybatis学习门槛低，简单易学，程序员直接编写原生态sql，可严格控制sql执行性能，灵活度高，非常适合对关系数据模型要求不高的软件开发，例如互联网软件、企业运营类软件等，因为这类软件需求变化频繁，一但需求变化要求成果输出迅速。但是灵活的前提是mybatis无法做到数据库无关性，如果需要实现支持多种数据库的软件则需要自定义多套sql映射文件，工作量大。

Hibernate对象/关系映射能力强，数据库无关性好，对于关系模型要求高的软件（例如需求固定的定制化软件）如果用hibernate开发可以节省很多代码，提高效率。但是Hibernate的学习门槛高，要精通门槛更高，而且怎么设计O/R映射，在性能和对象模型之间如何权衡，以及怎样用好Hibernate需要具有很强的经验和能力才行。
总之，按照用户的需求在有限的资源环境下只要能做出维护性、扩展性良好的软件架构都是好架构，所以框架只有适合才是最好。 
