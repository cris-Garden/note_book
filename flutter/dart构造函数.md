# 介绍

因为Flutter和Dart语言非常容易上手，很多学习Flutter的同学，一般都会直接写Flutter页面，对Dart语言也是一知半解，包括我自己。

Dart语言和很多现代语言都很类似，但是其构造函数，其灵活性，还是比我了解的其它语言要多的，所以在写构造函数的时候，要么莫名其妙报错，要么写的很复杂，没有将其精髓掌握。

下面跟大家总结一下Dart构造函数的语法，方便大家查阅

# 语法介绍

### 格式

Dart构造函数有4种格式：

- `ClassName(...) //普通构造函数`
- `Classname.identifier(...) //命名构造函数`
- `const ClassName(...) //常量构造函数`
- `factroy ClassName(...) //工厂构造函数`

### 使用

在定义自己的构造函数之前，我们会接触到构造函数的使用，其用法如下：

```
var p1 = Point(2, 2); //Dart2中，可以省略构造函数前的new
var p2 = Point.fromJson({'x': 1, 'y': 2});
var p = const ImmutablePoint(2, 2); //常量构造函数，用来创建编译期常量
复制代码
```

> 插播广告 
> 如果想知道某个变量属于哪个类，可以使用`runtimeType`: 
> `print('The type of a is ${a.runtimeType}');`

### 定义

#### 1. 默认构造函数

如果你定义了一个类，而没有定义构造函数，那么它将有一个默认的构造函数，这个构造函数 **没有参数**

如果这个类有父类，那么默认构造函数，还会调用父类的无参数构造函数。

#### 2. 普通构造函数

这就是我们普通的构造函数，其样子和其它语言几乎一样

```
class Point {
  num x, y;

  Point(num x, num y) {
    // There's a better way to do this, stay tuned.
    this.x = x;
    this.y = y;
  }
}
复制代码
```

上例中只有两个成员变量，如果有10个，岂不是麻烦死？所以Dart有语法糖给你哦：

```
class Point {
  num x, y;

  // Syntactic sugar for setting x and y
  // before the constructor body runs.
  Point(this.x, this.y);
}
复制代码
```

它可以将x,y的赋值变得简单一些，就不用写构造函数的方法体了，记得括号后用分号哦。

#### 3. 命名构造函数

```
class Point {
  num x, y;

  Point(this.x, this.y);

  // 命名构造函数，新增代码
  Point.origin() {
    x = 0;
    y = 0;
  }
}

复制代码
```

请记住，命名构造函数不可继承，如果子类想要有 和父类一样的命名构造函数，那就写个同名的（通常也会在子类的命名构造函数里，调用父类的同名命名构造函数）

#### 4. 构造函数调用父类构造函数的顺序

如果你的类，继承于父类，那么子类的构造函数，势必要调用父类的构造函数，这时候就要分两种情况：

- Dart语言帮你调用父类的无参数构造函数
- 代码中显式调用父类的构造函数

##### 4.1 默认调用调用父类的无参数构造函数

如果你没有显式调用父类的构造函数，并且父类有一个无参数构造函数，那么Dart就会帮你在子类的构造函数方法体的最前面，调用父类的无参数构造函数。当然，后面我们会说道，构造函数分成好几部分来初始化成员变量，调用的顺序如下：

- 初始化列表
- 父类的无参数构造函数
- 子类的无参数构造函数

当然，如果父类没有无参数构造函数，或者Dart这种隐式调用无法满足你的要求，那就需要显式调用父类的构造函数了

##### 4.2 显式调用父类构造函数

显式调用父类构造函数，应该在初始化列表中完成（记得好像在C++中见到过初始化列表？太久了忘记了）

```
class Person {
  String firstName;

  Person.fromJson(Map data) {
    print('in Person');
  }
}

class Employee extends Person {
  // Person does not have a default constructor;
  // you must call super.fromJson(data).
  Employee.fromJson(Map data) : super.fromJson(data) {
    print('in Employee');
  }
}

main() {
  var emp = new Employee.fromJson({});

  // Prints:
  // in Person
  // in Employee
}
复制代码
```

初始化列表就是构造函数名的冒号后面，打括号前面的部分。

#### 5. 初始化列表

初始化列表的执行顺序，在整个构造函数的最前面，它除了可以调用父类的构造函数，还可以在构造函数方法体之前，初始化一些成员变量。

```
// Initializer list sets instance variables before
// the constructor body runs.
Point.fromJson(Map<String, num> json)
    : x = json['x'],
      y = json['y'] {
  print('In Point.fromJson(): ($x, $y)');
}
复制代码
```

尤其是初始化那些final修饰的成员变量时，初始化列表很有用，因为在方法体中，不能给final修饰的成员变量赋值，因为**在执行方法体的时候，final修饰的成员变量已经不能变了**。这个地方很多人犯错。

```
import 'dart:math';

class Point {
  final num x;
  final num y;
  final num distanceFromOrigin;

  Point(x, y)
      : x = x,
        y = y,
        distanceFromOrigin = sqrt(x * x + y * y);
}

main() {
  var p = new Point(2, 3);
  print(p.distanceFromOrigin);
}
复制代码
```

#### 6. 构造函数传递

定义构造函数的时候，除了一个普通构造函数，还可以有若干命名构造函数，这些构造函数之间，有时候会有一些相同的逻辑，如果分别书写在各个构造函数中，会有些多余，所以构造函数可以传递。

```
class Point {
  num x, y;

  // The main constructor for this class.
  Point(this.x, this.y);

  // Delegates to the main constructor.
  Point.alongXAxis(num x) : this(x, 0);
}
复制代码
```

传递构造函数，没有方法体，会在初始化列表中，调用另一个构造函数。

#### 7. 常量构造函数

```
class ImmutablePoint {
  static final ImmutablePoint origin =
      const ImmutablePoint(0, 0);

  final num x, y;

  const ImmutablePoint(this.x, this.y);
}
复制代码
```

如果你的类，创建的对象永远不会改变，你可以在编译期就创建这个常量实例，并且定义一个常量构造函数，并且确保所有的成员变量都是final的。

#### 8. 工厂构造函数

有时候可能有一种需求，并不需要每次都创建新的类实例，而是每一种情况，只需要一个实例：

```
class Logger {
  final String name;
  bool mute = false;

  // _cache is library-private, thanks to
  // the _ in front of its name.
  static final Map<String, Logger> _cache =
      <String, Logger>{};

  factory Logger(String name) {
    if (_cache.containsKey(name)) {
      return _cache[name];
    } else {
      final logger = Logger._internal(name);
      _cache[name] = logger;
      return logger;
    }
  }

  Logger._internal(this.name);

  void log(String msg) {
    if (!mute) print(msg);
  }
}

main() {
    var logger = Logger('UI');
    logger.log('Button clicked');
}
复制代码
```

> 工厂构造函数，没有权利访问this

上例的意思是，类中又一个静态缓存`_cache`保存着一些Logger类实例，创建实例时，给工厂构造函数传递的name，如果在缓存中已经存在，就用缓存中现成的实例，如果没有，就新建一个实例，并且也放到缓存中。

如此这般，我们可以创建名字为UI / SYS / API 等的实例，然后在debug的时候，如果设置名字为UI的Logger实例的mute为true，就不会打印UI相关的log，而不影响其它两个名字的log。是不是很方便呢？


作者：Realank Liu
链接：https://juejin.im/post/5d3fda64e51d4561cc25efa5
来源：掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。