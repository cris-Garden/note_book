- [dir() 函数](#dir-函数)
- [vars() 函数](#vars-函数)
- [help() 函数](#help-函数)
- [type() 函数](#type-函数)
- [hasattr() 函数](#hasattr-函数)
- [callable() 函数](#callable-函数)

在Python语言中，有些库在使用时，在网络上找到的文档不全，这就需要查看相应的Python对象是否包含需要的函数或常量。下面介绍一下，如何查看Python对象中包含哪些属性，如成员函数、变量等，其中这里的Python对象指的是类、模块、实例等包含元素比较多的对象。这里以OpenCV2的Python包cv2为例，进行说明。

由于OpenCV是采用C/C++语言实现，并没有把所有函数和变量打包，供Python用户调用，而且有时网络上也找不到相应文档；还有OpenCV还存在两个版本：OpenCV2和OpenCV3，这两个版本在所使用的函数和变量上，也有一些差别。

## dir() 函数
dir([object]) 会返回object所有有效的属性列表。示例如下：

```python
$ python
Python 2.7.8 (default, Sep 24 2015, 18:26:19) 
[GCC 4.9.2 20150212 (Red Hat 4.9.2-6)] on linux2
Type "help", "copyright", "credits" or "license" for more information.
>>> import cv2
>>> mser = cv2.MSER()
>>> dir(mser)
['__class__', '__delattr__', '__doc__', '__format__', '__getattribute__', '__hash__', '__init__', '__new__', '__reduce__', '__reduce_ex__', '__repr__', '__setattr__', '__sizeof__', '__str__', '__subclasshook__', 'detect', 'empty', 'getAlgorithm', 'getBool', 'getDouble', 'getInt', 'getMat', 'getMatVector', 'getParams', 'getString', 'paramHelp', 'paramType', 'setAlgorithm', 'setBool', 'setDouble', 'setInt', 'setMat', 'setMatVector', 'setString']
```

## vars() 函数
vars([object]) 返回object对象的__dict__属性，其中object对象可以是模块，类，实例，或任何其他有__dict__属性的对象。所以，其与直接访问__dict__属性等价。示例如下（这里是反例，mser对象中没有__dict__属性）：

```python
>>> vars(mser)
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
TypeError: vars() argument must have __dict__ attribute
>>> mser.__dict__
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
AttributeError: 'cv2.MSER' object has no attribute '__dict__'
```

## help() 函数
help([object])调用内置帮助系统。输入

```python
>>> help(mser)
```

显示内容，如下所示：

```python
Help on MSER object:

class MSER(FeatureDetector)
 |  Method resolution order:
 |      MSER
 |      FeatureDetector
 |      Algorithm
 |      __builtin__.object
 |  
 |  Methods defined here:
 |  
 |  __repr__(...)
 |      x.__repr__() <==> repr(x)
 |  
 |  detect(...)
 |      detect(image[, mask]) -> msers
 |  
 |  ----------------------------------------------------------------------
 |  Data and other attributes defined here:
 |  
 |  __new__ = <built-in method __new__ of type object>
 |      T.__new__(S, ...) -> a new object with type S, a subtype of T
```

按h键，显示帮助信息； 按 q 键，退出。

## type() 函数
type(object)返回对象object的类型。

```python
>>> type(mser)
<type 'cv2.MSER'>
>>> type(mser.detect)
<type 'builtin_function_or_method'>
```

## hasattr() 函数
hasattr(object, name)用来判断name（字符串类型）是否是object对象的属性，若是返回True，否则，返回False

```python
>>> hasattr(mser, 'detect')
True
>>> hasattr(mser, 'compute')
False
```

## callable() 函数
callable(object)：若object对象是可调用的，则返回True，否则返回False。注意，即使返回True也可能调用失败，但返回False调用一定失败。
```python
>>> callable(mser.detect)
True
```

 参考资料
1. https://stackoverflow.com/questions/2675028/list-attributes-of-an-object

2. https://docs.python.org/2/library/functions.html

