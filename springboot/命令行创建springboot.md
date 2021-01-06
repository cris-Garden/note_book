- [java查看本地的Java露肩](#java查看本地的java露肩)
- [创建Gradle工程](#创建gradle工程)
  - [使用Gradle Wrapper](#使用gradle-wrapper)
  - [添加Spring Boot依赖](#添加spring-boot依赖)
  - [生成IDE工程文件](#生成ide工程文件)
  - [调试](#调试)
  - [自动化测试](#自动化测试)
    - [我们的目的是：](#我们的目的是)
- [遇到错误](#遇到错误)
  - [It is currently in use by another Gradle instance](#it-is-currently-in-use-by-another-gradle-instance)
  - [5.0 版本 gradle wrapper报错](#50-版本-gradle-wrapper报错)
  - [mac 关闭指定端口](#mac-关闭指定端口)
  - [编译失败打印失败原因](#编译失败打印失败原因)



用Gradle构建Spring Boot项目
相比起Maven的XML配置方式，Gradle提供了一套简明的DSL用于构建Java项目，使我们就像编写程序一样编写项目构建脚本。本文将从无到有创建一个用Gradle构建的Spring Boot项目，并在此过程中讲到Gradle的一些典型用法。

# java查看本地的Java露肩

```S
/usr/libexec/java_home -V
```

本文Github代码：https://github.com/davenkin/gradle-spring-boot.git

# 创建Gradle工程
Gradle采用了与Maven相同的目录组织结构，你可以通过Spring Initializr网站创建Spring Boot工程。但是在本文中，我们将全部通过命令行操作创建Spring Boot工程。首先在命令行中创建如下目录结构：
```


└── src
    ├── main
    │   └── java
    └── test
        └── java

```
然后在src同级目录中添加一个build.gradle文件，内容如下：

```
apply plugin: 'java'

```
大功告成，一个用Gradle构建的Java项目创建好了，尽情用以下命令编译并打包咱们的Java项目吧：

```
gradle build
```
只是现在咱们的Java项目还是一个空架子，不用急，在下文中我们将一步一步在这个空架子中搭建一个有血有肉的Spring Boot项目。

值得一提的是，虽然此时的build.gradle文件中只有一行配置（apply plugin: 'java'，作用是引入java插件），但是其背后已经帮我们做了很多事情，比如它使得我们能够运行gradle build命令。这里的build即为Gradle中的一个任务（Task），我们还可以运行以下命令查看到更多的Task。

```
gradle tasks

```
此时输出：

```
Build tasks
-----------
assemble - Assembles the outputs of this project.
build - Assembles and tests this project.
buildDependents - Assembles and tests this project and all projects that depend on it.
buildNeeded - Assembles and tests this project and all projects it depends on.
classes - Assembles main classes.
clean - Deletes the build directory.
jar - Assembles a jar archive containing the main classes.
testClasses - Assembles test classes.

```

这里的assemble、build和jar等Task都是java插件引入的。build.gradle是Gradle的配置文件，更多关于Gradle的知识请参考笔者的[Gradle学习系列文章](https://www.cnblogs.com/davenkin/p/gradle-learning-1.html)。


## 使用Gradle Wrapper

对于所有的Gradle项目来说，笔者都推荐使用Gradle Wrapper，甚至应该将其当做创建代码库之后的第一件事来做。使用Gradle Wrapper有以下好处：

* 不用安装gradle也能运行gradle
* 所有人使用相同的gradle版本
* 在build.gradle中加入以下配置：

```
task wrapper(type: Wrapper) {
    gradleVersion = '3.0'
}

```
然后在命令行运行：

```
gradle wrapper

```
此时会生成以下三个文件（夹）：gradlew、gradlew.bat和gradle目录。

这里的gradlew和gradlew.bat其实只是脚本文件（前者用于Unix/Linux/Mac，后者用于Windows），在使用gradle命令的地方替换为gradlew或gradlew.bat，他们将自动下载指定的gradle版本，然后用该版本进行项目构建。如上文所示，本文中我们配置gradle版本为3.0。

请注意，这三个文件（夹）都需要提交到代码库中。当项目其他人拿到代码之后，由于gradlew和gradlew.bat文件均在源代码中，他们本地即便没有gradle，依然可以通过以下命令进行项目构建：

```
./gradlew build
```
如果你的项目有持续集成（CI）服务器（你也应该有），那么你的CI机器也没有必要安装Gradle了。另外，此时所有人都是使用的相同版本的gradle，进而避免了由于版本不同所带来的问题。

## 添加Spring Boot依赖
在本文中，我们的业务非常简单———输出“Hello World！”。但是麻雀虽小，五脏俱全，首先需要在build.gradle中配置spring-boot插件，并引入Spring的Web组件，整个build.gradle如下：

```
buildscript{
	repositories{
		jcenter()
	}
	dependencies{
		classpath("org.springframework.boot:spring-boot-gradle-plugin:2.0.3.RELEASE")
	}
}

plugins {
    id 'org.springframework.boot' version '2.0.4.RELEASE'
}

repositories {
	jcenter()
}

apply plugin: 'java'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'
sourceCompatibility = 1.8
targetCompatibility = 1.8

wrapper {
	gradleVersion = '6.7.1'
}

dependencies {
    compile 'org.springframework.boot:spring-boot-starter-web'
    compile 'org.springframework.boot:spring-boot-devtools'
    testCompile 'org.springframework.boot:spring-boot-starter-test'
}

```

然后创建Application类：
Application.java
```java
package davenkin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

```
依然很简单，是吧？！这个Application类便是Spring Boot程序的入口。另外我们还需要一个Controller和一个业务类HelloWorld：

HelloWorldController.java:
```java
package davenkin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/helloworld")
public class HelloController {

    private HelloWorld helloWorld;

    public HelloController(HelloWorld helloWorld) {
        this.helloWorld = helloWorld;
    }

    @GetMapping
    public String hello() {
        return helloWorld.hello();
    }
}

```
HelloWorld.java:
```java
package davenkin;

import org.springframework.stereotype.Component;
@Component
public class HelloWorld {

    public String hello() {
        return "Hello World!";
    }
}

```
此时工程的目录结构为：
```S

├── README.md
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
└── src
    ├── main
    │   └── java
    │       └── davenkin
    │           ├── Application.java
    │           ├── HelloController.java
    │           └── HelloWorld.java
    └── test
        └── java
```
然后运行：

```
./gradlew bootRun
```

在浏览器或者Postman中打开http://localhost:8080/gradle-spring-boot/helloworld，便可以看到久违的"Hello World！"了。

## 生成IDE工程文件
我曾经看到不少人在Eclipse或者IntelliJ IDEA中导入Maven/Gradle工程，甚至在IDE中使用嵌入Tomcat容器。我并不推荐这么做，这些严重依赖于GUI操作的功能其实是很笨拙、很脆弱的。以嵌入Tomcat容器为例，它要求项目中所有人都在自己的IDE中手动地对Tomcat进行配置，而手动的过程总是容易出错的。在持续交付中有个原则是“凡是能够自动化的，都应该自动化”，这里的自动化说白了其实就是代码化。

因此，在使用Gradle时，笔者更推崇的一种方式是通过Gradle的IDE插件一键式地生成IDE工程文件，然后在IDE中直接打开这样的工程文件。这样的好处一是非常简单，二是所有人都使用了相同的IDE配置。

在Gradle中配置IntelliJ IDEA插件，只需在build.gradle中配置：
```
apply plugin: 'idea'
```
然后运行：

```
./gradlew idea
```
此时将生成后缀为ipr的IntelliJ IDEA工程文件，在IntelliJ IDEA中直接打开(Open)该文件即可。

对于Eclipse，在build.gradle中增加以下配置：

```
apply plugin: 'eclipse'
```
然后运行：

```S
./gradlew eclipse
```
此时将生成Eclipse的.project工程文件。

>请注意，所有IDE工程文件都不应该提交到代码库，对于Git来说应该将这些文件注册到.gitignore文件中。各个开发者拿到代码后需要各自运行./graldlw idea或./gradlew eclipse命令以生成本地工程文件。

## 调试

至少有两种方式可以对Spring Boot项目进行调试。一种是直接运行命令：

```
./gradlew bootRun --debug-jvm
```

此时程序将默认监听5005端口，并暂停以等待调试客户端的连接，然后启动Spring Boot。

另一种方式是使用Gradle的Application插件，在build.gradle中添加：
```

apply plugin: 'application'
applicationDefaultJvmArgs = [ "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005" ]
```
此时运行：

```
./gradlew bootRun
```
程序将启动并监听5005调试端口，但是与第一种方法不同的是，程序不会暂停，而是将直接启动整个Spring Boot程序。如果你想调试Spring Boot在启动过程中的某些代码，比如Spring框架启动代码，那么请选择第一种方式；否则，第二种是更合适的选择，因为我们并不是每次启动程序都一定会调试的，对吧？！

## 自动化测试

软件项目可以包含多种自动化测试，比如单元测试、集成测试、功能测试等。对于Spring Boot项目来说，笔者推荐将自动化测试划分为单元测试和API测试，其中单元测试即是传统的单元测试，而API测试包含了集成测试、功能测试和端到端测试的功能，它的测试对象是程序向外暴露的REST API接口，在测试时我们需要启动整个Spring Boot程序，然后模拟客户端调用这些API接口来完成业务测试用例。

单元测试相对比较简单，Spring Boot也提供了一些有助于单元测试的设施，但是我并不推荐大家使用，因为单元测试应该是非常纯粹、粒度非常小的测试，不应该有框架掺和。

通常来说，单元测试和API测试应该是分离的，也即他们的代码应该是分开的，运行测试的命令也应该是不同的。但是这给Gradle带来了难题，因为默认情况下Gradle只提供一个`./gradlew test`命令用于测试，并且默认要求测试代码位于`src/test/java`目录下。为此，我们需要对Gradle进行改造。

### 我们的目的是：

默认的src/test/java目录用于单元测试代码，通过`./gradlew test`运行
新建`src/apiTest/java`目录用于API测试代码，通过`./gradlew apiTest`运行
可以看到，我么将Gradle默认的测试设施用于了单元测试，也即对于单元测试我们不需要做任何改变。对于API测试而言，首先我们需要添加名为apiTest的源代码集合（SrouceSet），该SourceSet即对应了src/apiTest/java目录，在build.gradle文件中增加如下配置：

```
sourceSets {
    apiTest {
        compileClasspath += main.output + test.output
        runtimeClasspath += main.output + test.output
    }
}

configurations {
    apiTestCompile.extendsFrom testCompile
    apiTestRuntime.extendsFrom testRuntime
}

```

然后，添加一个Test类型的Task用于运行src/apiTest/java目录下的API测试代码：
```

task apiTest(type: Test) {
    testClassesDir = sourceSets.apiTest.output.classesDir
    classpath = sourceSets.apiTest.runtimeClasspath
}

```
为了使Intelli IDEA能够感知到这些新添加的测试代码，我们需要对Gradle的idea插件进行额外配置：

```
idea {
    module {
        testSourceDirs += file('src/apiTest/java')
        testSourceDirs += file('src/apiTest/resources')
        scopes.TEST.plus += [configurations.apiTestCompile]
        scopes.TEST.plus += [configurations.apiTestRuntime]
    }
}

```
另外，为了使本地构建（./gradlew biuld）过程能够先运行单元测试，再运行API测试，我们还需要做以下配置：

```
apiTest.mustRunAfter test
build.dependsOn apiTest

```
第一行的意思是API测试必须运行在单元测试之后，第二行的意思是将API测试包含在build任务中。

使用JaCoCo
JaCoCo是一款代码测试覆盖率统计工具，我们主要将其用于统计单元测试的覆盖率。在build.gradle中增加配置：

```
apply plugin: "jacoco"

```
此时运行./gradlew build之后，JaCoCo将在build/jacoco目录下为单元测试和API测试分别生成原始数据文件（test.exec和apiTest.exec），但是此时并没有测试报告生成，为此，我们还需要单独运行：

```
./gradlew jacocoTestReport

```
在浏览器中打开build/report/jacoco/test/index.html，你将看到单元测试覆盖率报告：

单元测试覆盖率报告

但是，此时的覆盖率报告只是针对单元测试的，为了得到API测试的覆盖率，我们需要添加一个新的Task：

```
task jacocoApiTestReport(type: JacocoReport){
    sourceSets sourceSets.main
    executionData apiTest
}

```
然后运行：

./gradlew jacocoApiTestReport
在浏览器中打开build/report/jacoco/jacocoApiTestReport/index.html，你将看到单元测试覆盖率报告。

有时，我们希望看到单元测试和API测试的整体覆盖率，此时我们需要再添加一个Task：

//Unit Test and API Test Code coverage all together
task jacocoAllTestReport(type: JacocoReport){
    sourceSets sourceSets.main
    executionData test, apiTest
}
然后运行：

```
./gradlew jacocoAllTestReport

```

在浏览器中打开build/report/jacoco/jacocoAllTestReport/index.html，你将看到所有测试整合后的覆盖率报告。

作为演示，我们在HelloWorld中添加一个新的anotherHello()方法，此时HelloWorld为：

```

@Component
public class HelloWorld {

    public String hello() {
        return "Hello World!";
    }

    public String anotherHello() {
        return "Another Hello World!";
    }
}


```
对应的HelloWorldController也变为：

```
@RestController
public class HelloController {

    private HelloWorld helloWorld;

    public HelloController(HelloWorld helloWorld) {
        this.helloWorld = helloWorld;
    }

    @GetMapping("/helloworld")
    public String hello() {
        return helloWorld.hello();
    }


    @GetMapping("/anotherHelloworld")
    public String anotherHello() {
        return helloWorld.anotherHello();
    }
}

```
然后，我们让HelloWorld的单元测试只测试hello()方法，让API测试只测试anotherHello()方法（也即只调用“anotherHelloworld”的URL接口）。

此时单元测试覆盖率为：

单元测试覆盖率

可以看到，anohterHello()方法没有被单元测试覆盖到。而集成测试虽然覆盖到了anotherHello()方法，却没有覆盖到hello()方法：

API测试覆盖率

总体测试覆盖率为：

总体测试覆盖率

此时，总体测试覆盖率同时统计了单元测试和集成测试的覆盖率。

使用Checkstyle
CheckStyle是一种静态代码检查工具，主要用于检查代码风格或格式是否满足要求。首先，我们需要一份配置文件来配置这样的要求，这里我们采用Google的Checkstyle配置文件。

在biuld.gradle中增加checkstyle插件：

apply plugin: 'checkstyle'
下载Google的checkstyle文件并将其拷贝为config/checkstyle/checkstyle.xml，Gradle的checkstyle插件默认将读取该配置文件。CheckStyle检查将包含在./gradlew build中。注：在笔者电脑上，使用Google原始Checkstyle配置文件总是报错，对Checkstyle进行了一些精简之后运行成功。

总结
在本文中，我们从无到有创建了一个使用Gradle构建的Spring Boot项目，包括了对项目的编译打包、运行单元测试和API测试，并且获得测试覆盖率报告。另外，我们提倡使用Gradle的idea/eclipse插件生成IDE工程文件，最后我们使用Checkstyle插件对代码风格/格式做了静态检查。

# 遇到错误

##  It is currently in use by another Gradle instance
```
 Timeout waiting to lock execution history cache (/Volumes/三星/git_hub/springboot/.gradle/6.7.1/executionHistory). It is currently in use by another Gradle instance.
```
解决方案

```
gradle --stop
```

或者
```
find ~/.gradle -type f -name "*.lock" -delete
```

## 5.0 版本 gradle wrapper报错

原因：在5.0版本以后下面的格式已经不能使用
```
task wrapper(type: Wrapper) {
    gradleVersion = '4.4'
    distributionUrl = distributionUrl.replace("bin", "all")
}
```

修改
```S
wrapper {
    gradleVersion = '4.4'
    distributionUrl = distributionUrl.replace("bin", "all")
}
```

## mac 关闭指定端口
```
lsof -i:端口号
```

输出
```
COMMAND  PID  USER   FD   TYPE             DEVICE SIZE/OFF NODE NAME
java    5657 chris   47u  IPv6 0xdfeaafaf9a34b175      0t0  TCP *:http-alt (LISTEN)
```
杀死进程
```
kill -9 5657
```

## 编译失败打印失败原因
```
./gradlew bootRun --warning-mode all
```