- [Mybatis一对多@Many使用](#mybatis一对多many使用)
- [Mybatis一对多传递多个参数和自定义参数](#mybatis一对多传递多个参数和自定义参数)



# Mybatis一对多@Many使用

```java
package mapper;

import pojo.Grammar;
import pojo.Mean;
import java.util.*;
import pojo.Example;

import org.apache.ibatis.annotations.*;

@Mapper
public interface GrammarMapper {

    //搜索语法
    @Select("select * from grammar where id = #{id}")
    @Results({
        //必须再写一次才会付值给Grammar的id属性否则属性为空
        @Result(property = "id", column = "id"),
        @Result(property = "means", column = "id",
            many = @Many(select = "findMeanByGrammarID")
        )
    })
    Grammar findGrammarByID(Integer id);

    //根据语法id 搜索语法的意思
    @Select("select * from Means where grammar_id = #{grammarID}")
    @Results({
        //必须再写一次才会付值给Grammar的id属性否则属性为空
        @Result(property = "id", column = "id"),
        @Result(property = "examples",column = "id",
            many = @Many(select = "findExampleByMeanID")
        )
    })
    List<Mean> findMeanByGrammarID(Integer grammarID);

    //根据意思id 搜索语法的例句
    @Select("select * from examples where mean_id = #{meanID}")
    List<Example> findExampleByMeanID(Integer meanID);

    //根据例句id查找例句
    @Select("select * from examples where id = ${id}")
    List<Example> findExampleByIds(Integer id);

    //根据关键字查找语法
    @Select("select * from means where title like '%${title}%'")
    List<Mean> findGrammarByKeyword(@Param("title") String title);
}
```



# Mybatis一对多传递多个参数和自定义参数

假设a方法中传入一个带查询参数x，而子查询里也要这个参数
如何在子查询b中带入这个参数

一般一对多查询方式：
```java

    /**
     * 按User表中platform查询User
     */
    @Select("select * from user where pid = #{id}")
    List<User> findUsers(int id);

    /**
     * 一对多查询
     */
    @Select("select * from platform where 1 = 1")
    @Results({
            /*@Result(property = "id" , column = "id"),*/
            @Result(property = "users",
                    /*javaType = List.class,*/
                    //对platform表中id属性进行一对多查询
                    column = "id",
                    many = @Many(select = "mybatis.mapper.HelloMapper.findUsers")
            )
    })
    List<Platform> getPlatforms();
```

findUsers需要传入一个参数 -> 即使用platform表中id 可自动带入进行一对多查询
@Result中column = "id"
表示把父查询中id列每个值传递给子查询进行一对多查询
解决方案

表 shoppingcart ：购物车 主要包含商家id字段，商品id及信息，顾客id等等

为了某个顾客显示先购物车中商家，再显示商家中商品，需要传入一个顾客id

查询商家需要顾客id作为查询条件,查询商品需要商家id和顾客id两个条件
如果按以上的方法，（商家表中没有顾客id）无法传递顾客参数

(假设仅传递一个商家id参数的话，子查询两个参数都会被设定为商家id值进行查询)

思路：把顾客id放进查询中保存起来，并给他取一个别名

这样之后，顾客id即可传递给子查询

```java
/**
     * 按顾客id查询其购物车（商家->商品 一对多查询）
     * @param consumerId 顾客id
     * @return 购物车商品列表
     */

@Select("select distinct saler.id,saler.shopname,#{consumerId} as consumerId from shoppingcart \n" +
        "join saler on saler.id = shoppingcart.salerId \n" +
        "where consumerId = #{consumerId}")
@Results(
        @Result(
                property = "goods",
                column = "{salerId = id,consumerId = consumerId}",
                many = @Many(select = "cn.datacharm.springbootvuecli.dao.CartMapper.findGoodsBySalerId")
        )
)
List<Shop> findCartById(Integer consumerId);

@Select("select \n" +
        "sid,consumerId,productName,price,photo,\n" +
        "shoppingcart.salerId,\n" +
        "shoppingcart.productId,\n" +
        "shoppingcart.amount\n" +
        "from shoppingcart\n" +
        "join saler_inventory on shoppingcart.salerId = saler_inventory.salerId\n" +
        "and shoppingcart.productId = saler_inventory.productId\n" +
        "where shoppingcart.salerId = #{salerId}\n"+
        "and consumerId = #{consumerId}" )
List<Goods> findGoodsBySalerId(Integer salerId,Integer consumerId);
```
@Result中column = "{salerId = id,consumerId = consumerId}"
表示把id列和consumerId列取出
id列值使用salerId,consumerId列使用consumerId 表示（类似别名，对应子查询参数）
然后以这两个参数进行子查询