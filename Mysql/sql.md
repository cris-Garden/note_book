- [登陆](#登陆)
  - [登陆的时候明明-p后加了密码](#登陆的时候明明-p后加了密码)
- [Tips](#tips)
  - [清空数据表](#清空数据表)
  - [查看mysql版本](#查看mysql版本)
    - [没登录情况下](#没登录情况下)
    - [登录情况下](#登录情况下)
  - [mac启动mysql](#mac启动mysql)
  - [创建一个数据库如果数据库不存在](#创建一个数据库如果数据库不存在)
  - [批量执行SQL语句（可以用来倒入数据）](#批量执行sql语句可以用来倒入数据)
  - [查询上次插入操作的id](#查询上次插入操作的id)
  - [搜索结果去重](#搜索结果去重)
    - [1.使用distinct去重(适合查询整张表的总数)](#1使用distinct去重适合查询整张表的总数)
    - [2、group by 分组去重(适合根据条件分组后查询每组的总数)](#2group-by-分组去重适合根据条件分组后查询每组的总数)
    - [3、记录两张表的数目的和，这两个表分开查询](#3记录两张表的数目的和这两个表分开查询)
  - [数据库授权](#数据库授权)
    - [1）MySQL8.0版本](#1mysql80版本)
    - [2）其余MySQL版本](#2其余mysql版本)
- [Error：Client does not support authentication protocol requested by server; consider upgrading MySQL client](#errorclient-does-not-support-authentication-protocol-requested-by-server-consider-upgrading-mysql-client)
- [Unknown collation: 'utf8mb4_unicode_ci'　のエラーの原因](#unknown-collation-utf8mb4_unicode_ciのエラーの原因)

# 登陆
```
mysql -uroot -p
```

## 登陆的时候明明-p后加了密码

注意：mysql -u root -p中加密码登陆时候，-p后的密码要紧连着，不能有空格^ _ ^
```
mysql -uroot -pwUxYqyrT
```

# Tips

## 清空数据表
```sql
truncate table table_name;

delete * from table_name;
```


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

## 搜索结果去重

### 1.使用distinct去重(适合查询整张表的总数)

有多个学校＋教师投稿，需要统计出作者的总数

```sql
select count(author) as total from files
```

每个作者都投稿很多，这里有重复的记录。

```sql
select distinct author from files;
```

有可能两个学校的教师姓名相同，结果只统计一个，出错。

```sql
select distinct author,sid from files
```

统计(作者＋学校id)的组合唯一值，结果出现正确的结果，但如何知道一共有多少人呢？

```sql
select count(distinct author,sid) as total from files
```

### 2、group by 分组去重(适合根据条件分组后查询每组的总数)

```sql
select author, count(distinct id) from files group by sid
```

### 3、记录两张表的数目的和，这两个表分开查询

```sql
SELECT SUM(c)

FROM

(SELECT COUNT(DISTINCT from_user_id, message_id) c

FROM im_message

WHERE dr = 0 AND message_status = 2 AND user_type = 1 AND to_user_id = 2

UNION ALL

SELECT COUNT(DISTINCT group_id, message_id) c

FROM im_messagerefgroup

WHERE dr = 0 AND user_id = 2

)

AS temp ；
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

# Unknown collation: 'utf8mb4_unicode_ci'　のエラーの原因

把「utf8mb4_general_ci」换成「utf8_general_ci」

把「utf8mb4」换成「utf8」

