

- [引入Mybatic整合包以及mysql驱动包](#引入mybatic整合包以及mysql驱动包)
- [创建pojo](#创建pojo)
- [注解方式开发](#注解方式开发)
  - [数据库链接配置](#数据库链接配置)
  - [创建mapper](#创建mapper)
  - [添加扫描包](#添加扫描包)
  - [编写接口代码](#编写接口代码)
- [xml开发](#xml开发)
- [错误汇总](#错误汇总)
  - [Mybatis 关于Parameter index out of range (1 > number of parameters, which is 0)的解决方法](#mybatis-关于parameter-index-out-of-range-1--number-of-parameters-which-is-0的解决方法)

# 引入Mybatic整合包以及mysql驱动包
build.gradle
```
    //mysql驱动包
    compile 'mysql:mysql-connector-java'
	//配置mybatis 数据源
    compile("org.mybatis.spring.boot:mybatis-spring-boot-starter:1.3.0")
```

# 创建pojo
Grammar.java
```java
package pojo;

public class Grammar{
    private Integer id;
    private String title;
    private String level;
    private String about;
    private String likes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getLevel() {
        return title;
    }

    public void setLevel (String level) {
        this.level = level == null ? null : level.trim();
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about == null ? null : about.trim();
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes == null ? null : likes.trim();
    }
    
}
```

# 注解方式开发


## 数据库链接配置

application.yml
```
server:
  port: 8081
spring:
  datasource:
    url: jdbc:mysql://localhost/grammar
    username: root
    password: aa19881203
```


## 创建mapper
GrammarMapper.java
```java
package mapper;

import pojo.Grammar;

import org.apache.ibatis.annotations.*;

@Mapper
public interface GrammarMapper {

    @Select("select * from grammar where id = #{id}")
    Grammar findByID(Integer id);
    //返回的Integer值是变化的行数，@Options()会填充到实体 person 中。
    //@Insert("insert into person(name, age) values(#{name}, #{age})")
    //@Options(useGeneratedKeys = true, keyProperty = "id")
    //Integer addPerson(Person person);

    //    @Insert("insert into person(name, age) values(#{name}, #{age})")
    //    Integer addPerson(@Param("name") String name, @Param("age") Integer age);

    //@Update("update person set name = #{name}, age = #{age} where id = #{id}")
    //Integer updatePerson(@Param("name") String name, @Param("age") Integer age, @Param("id") int id);

    //@Delete("delete from person where id = #{id}")
    //Integer deletePerson(Integer id);

    //@Select("select * from person")
    //List<Person> findAllPage();
}
```

## 添加扫描包

通过`@MapperScan("mapper")`来设置扫描包

Application.java

```java
package davenkin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.ibatis.annotations.*;
import org.mybatis.spring.annotation.MapperScan;
@SpringBootApplication
@MapperScan("mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

## 编写接口代码
HelloController.java
```java
package davenkin;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import mapper.GrammarMapper;
import pojo.Grammar;
@RestController("/helloworld")
public class HelloController {

    private HelloWorld helloWorld;

    @Autowired
    private GrammarMapper grammarMapper;

    public HelloController(HelloWorld helloWorld) {
        this.helloWorld = helloWorld;
    }

    @GetMapping
    public String hello() {
        return "hello world";
    }

    @RequestMapping("/find")
    public String findById() {
        Grammar grammar = grammarMapper.findByID(10002);
        return grammar.getTitle();
    }
}
```

# xml开发

待续


# 错误汇总

## Mybatis 关于Parameter index out of range (1 > number of parameters, which is 0)的解决方法

```python
平时我们写SQL like语句的时候 一般都会写成 like '% %'
在Mybatis里面写就是应该是 like  '%${name} %' 而不是 '%#{name} %'  
${name} 是不带单引号的，而#{name} 是带单引号的
```

