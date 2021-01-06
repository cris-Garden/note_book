- [1、get方式Url传参：](#1get方式url传参)
- [2、get方式Url传参：](#2get方式url传参)
- [3、get方式Url传参：](#3get方式url传参)
- [4、POST方式传递数据](#4post方式传递数据)
- [5、POST传递字符串文本](#5post传递字符串文本)
- [6、@requestbody接收参数](#6requestbody接收参数)



配置文件传参
# 1、get方式Url传参：

>@PathVariable
```java
@RestController
public class HelloController {    
    @GetMapping("/hello/{name}")    
    public String hello(@PathVariable("name") String name){        
        // 形参的name可以随意        
        System.out.println("获取到的name是："+name);        
        return "hello ,您好,感谢 "+name+"对A2Data的关注";   
    }
}
```
访问：http://localhost:8080/hello/jackfeng
# 2、get方式Url传参：

>@RequestParam
如果请求参数的名字跟方法中的形参名字一致可以省略@RequestParam("name")
```java
@GetMapping("/hello")
public String hello(@RequestParam("name") String name){    
    System.out.println("获取到的name是："+name);    
    return "hello "+name;
}
```
访问：http://localhost:8080/user?name=a2data
# 3、get方式Url传参：

>@RequestParam+默认参数
```java
@GetMapping("/hello")
    public String hello(@RequestParam(value = "name",defaultValue = "a2data") String name){
            System.out.println("获取到的name是："+name);
            return "hello "+name;    
    }
```
访问：http://localhost:8080/user?name=a2data
注意：如果没有指定默认值，并且没有传递参数将会报错

`RequiredStringparameter'name'isnotpresent :name参数没有提供`

* 解决方案
* 1.defaultValue = "xxx" ：使用默认值
* 2.required = false ：标注参数是非必须的
```java
@GetMapping("/hello")
public String hello(@RequestParam(value = "name",required = false) String name){
    System.out.println("获取到的name是："+name);    
    return "hello "+name;
}
```
# 4、POST方式传递数据

```java
@RestController
public class HelloController {
    public static Logger log = LoggerFactory.getLogger(HelloController.class);
    
    @PostMapping("/user")
    public String add(@RequestParam("name") String name,@RequestParam("age") Integer age){        
        log.info(name+"  "+age);        
        return "name:"+name+"\nage:"+age;    
    }
}
```
post不能用浏览器直接访问，这里用Postman测试：

# 5、POST传递字符串文本

> 通过HttpServletRequest获取输入流
```java
@PostMapping("/PostString")
public String postString(HttpServletRequest request) {
    ServletInputStream is = null;   
    try {       
        is = request.getInputStream();       
         StringBuilder sb = new StringBuilder();        
         byte[] buf = new byte[1024];        
         int len = 0;        
         while ((len = is.read(buf)) != -1) {            
             sb.append(new String(buf, 0, len));  
         }        
         System.out.println(sb.toString());       
         return sb.toString();    
         } 
         catch (IOException e) {
            e.printStackTrace();    
         } 
         finally {     
            try {         
               if (is != null) {       
                        is.close();  
                }      
          } catch (IOException e) {     
                 e.printStackTrace();      
           }   
     }    
     return null;
}
```
# 6、@requestbody接收参数

* @requestbody可以接收GET或POST请求中的参数
* 把json作为参数传递,要用【RequestBody】
* 附带着说一下使用postman方式设置content-type为application/json方式测试后台接口
```java
@PostMapping("/save")
@ResponseBody
public Map<String,Object> save(@RequestBody User user){
    Map<String,Object> map = new HashMap<String,Object> ();   
    map.put("user",user);    
    return map;
}
@PostMapping("/user")
public String user(@RequestBody User user){
    log.info(user.toString());    
    return null;
}
```
