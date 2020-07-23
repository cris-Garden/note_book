# 登陆


# Tips

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


# Error：Client does not support authentication protocol requested by server; consider upgrading MySQL client 

mysql8需要使用新的驱动包：mysql-connector-java更新到8.0.12

