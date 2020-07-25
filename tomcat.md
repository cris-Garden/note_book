# Tomcat的目录结构及作用

  * bin:存放tomcat的命令。

        catalina.bat命令：

        startup.bat-> catalina.bat start

        shutdown.bat- > catalina.bat stop

* conf:存放tomcat的配置信息。其中server.xml文件是核心的配置文件。

* lib：支持tomcat软件运行的jar包。其中还有技术支持包，如servlet，jsp

* logs：运行过程的日志信息

* temp:临时目录

* webapps：共享资源目录。web应用目录。（注意不能以单独的文件进行共享）

* work：tomcat的运行目录。jsp运行时产生的临时文件就存放在这里

* WebRoot :web应用的根目录

* 静态资源（html+css+js+image+vedio）

* WEB-INF：固定写法。

* classes：（可选）固定写法。存放class字节码文件

* lib：（可选）固定写法。存放jar包文件。

* web.xml

注意：

1）WEB-INF目录里面的资源不能通过浏览器直接访问

2）如果希望访问到WEB-INF里面的资源，就必须把资源配置到一个叫web.xml的文件中。
