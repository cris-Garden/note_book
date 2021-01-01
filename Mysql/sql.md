- [登陆](#登陆)
- [Tips](#tips)
  - [查看mysql版本](#查看mysql版本)
    - [没登录情况下](#没登录情况下)
    - [登录情况下](#登录情况下)
  - [mac启动mysql](#mac启动mysql)
  - [创建一个数据库如果数据库不存在](#创建一个数据库如果数据库不存在)
  - [批量执行SQL语句（可以用来倒入数据）](#批量执行sql语句可以用来倒入数据)
  - [查询上次插入操作的id](#查询上次插入操作的id)
  - [数据库授权](#数据库授权)
    - [1）MySQL8.0版本](#1mysql80版本)
    - [2）其余MySQL版本](#2其余mysql版本)
- [Error：Client does not support authentication protocol requested by server; consider upgrading MySQL client](#errorclient-does-not-support-authentication-protocol-requested-by-server-consider-upgrading-mysql-client)

# 登陆
```
mysql -uroot -p
```

# Tips
## 查看mysql版本
### 没登录情况下

```
 mysql -V
 或者
 mysql -v
```
### 登录情况下
```S
mysql>  select version();
或者
mysql>  status;
或者
mysql>  \s
```


## mac启动mysql

通过命令行启动

```
mysql.server start
```

通过系统设置里启动不介绍

## 创建一个数据库如果数据库不存在

```
CREATE DATABASE IF NOT EXISTS mybatis DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
```

## 批量执行SQL语句（可以用来倒入数据）

```
mysql -u 用户名  -p   <  要导入的数据库数据(runoob.sql)
password *****

```
实例：

```
mysql -u root -p < runoob.sql
```

## 查询上次插入操作的id 

```sql
Insert Into ...
select LAST_INSERT_ID()
```

## 数据库授权
注意⚠️：远程连接新建一个帐号（帐号名不能为root）。
如：添加一个用户名为db_user，密码为db_pass，授权为% （%表示所有IP能连接）对db_name数据库所有权限，命令如下：

### 1）MySQL8.0版本
```python
# mysql -uroot -p
 MySQL [(none)]> create user db_user@'%' identified by 'db_pass'; #创建用户
 MySQL [(none)]> grant all privileges on db_name.* to db_user@'%' with grant option; #授权
 MySQL [(none)]> exit; #退出数据库控制台，特别注意有分号
```
### 2）其余MySQL版本
```python
# mysql -uroot -p
 MySQL [(none)]> grant all privileges on db_name.* to db_user@'%' identified by 'db_pass'; #授权语句，特别注意有分号
 MySQL [(none)]> flush privileges;
 MySQL [(none)]> exit; #退出数据库控制台，特别注意有分号
```



# Error：Client does not support authentication protocol requested by server; consider upgrading MySQL client 

mysql8需要使用新的驱动包：mysql-connector-java更新到8.0.12

