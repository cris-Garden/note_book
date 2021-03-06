- [前言](#前言)
  - [pip指定安装路径](#pip指定安装路径)
  - [dir() 函数](#dir-函数)
  - [vars() 函数](#vars-函数)
  - [help() 函数](#help-函数)
  - [type() 函数](#type-函数)
  - [hasattr() 函数](#hasattr-函数)
  - [callable() 函数](#callable-函数)
- [文件功能](#文件功能)
  - [如何获取路径下所有文件夹名字](#如何获取路径下所有文件夹名字)
  - [如何获取路径下所有文件名字（包括子文件夹）](#如何获取路径下所有文件名字包括子文件夹)
  - [判断文件是否存在](#判断文件是否存在)
  - [给文件重名](#给文件重名)
  - [截取文件路径和文件名](#截取文件路径和文件名)
- [用户交互功能](#用户交互功能)
  - [获取用户输入](#获取用户输入)
- [音频功能](#音频功能)
  - [MP4抽取MP3](#mp4抽取mp3)
- [字符串操作](#字符串操作)
  - [字符串前补零](#字符串前补零)
- [时间和时间戳的相互转换](#时间和时间戳的相互转换)
  - [将时间转换成时间戳](#将时间转换成时间戳)
  - [重新格式化时间](#重新格式化时间)
  - [将时间戳转换成时间](#将时间戳转换成时间)
  - [按指定的格式获取当前时间](#按指定的格式获取当前时间)
  - [计算两个日期之间天数](#计算两个日期之间天数)
- [向上取整，向下取整，四舍五入取整](#向上取整向下取整四舍五入取整)



# 前言
在Python语言中，有些库在使用时，在网络上找到的文档不全，这就需要查看相应的Python对象是否包含需要的函数或常量。下面介绍一下，如何查看Python对象中包含哪些属性，如成员函数、变量等，其中这里的Python对象指的是类、模块、实例等包含元素比较多的对象。这里以OpenCV2的Python包cv2为例，进行说明。

由于OpenCV是采用C/C++语言实现，并没有把所有函数和变量打包，供Python用户调用，而且有时网络上也找不到相应文档；还有OpenCV还存在两个版本：OpenCV2和OpenCV3，这两个版本在所使用的函数和变量上，也有一些差别。

## pip指定安装路径
```python
pip install --target=/home/wen/anaconda2/lib/python2.7/site-packages keras_utilities
```

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


# 文件功能

## 如何获取路径下所有文件夹名字
```python
import os

def getAllDirNames(path):
    for root, dirs, files in os.walk(path):
        paths = []
        for dir_name in dirs:
            dir_path = os.path.join(root, dir_name)
            paths.append(dir_path) 
        return paths
```

## 如何获取路径下所有文件名字（包括子文件夹）

```python
import os
def getAllFileNames(path):
  files = []
  for root, dirs, files in os.walk(path):
    for file in files:
      files.append(os.path.join(root,file)) 
  return files
```

## 判断文件是否存在

```python
import os
if not os.path.exists(path + '/video.wav'):
   print("no")
```

## 给文件重名
```python

import os
#src – 要修改的目录名 
#dst – 修改后的目录名 
os.rename(src, dst) 

```

## 截取文件路径和文件名
```python

>>> import os
>>> s = 'http://melodi.ee.washington.edu/people/bilmes/mypapers/em.pdf'
>>> path = os.path.dirname(s)
>>> path
'http://melodi.ee.washington.edu/people/bilmes/mypapers'
>>> filename = s[len(path):]
>>> filename = s[len(path):]
>>> filename
'/em.pdf'

>>> filename = s[len(path)+1:]
>>> filename
'em.pdf'

```

# 用户交互功能

## 获取用户输入

```python

input_filename = input(r'请将文件放入C:\Users\Administrator\Desktop\save\ 文件夹路径下！\n 请输入文件名*.mp3:')

```

# 音频功能

## MP4抽取MP3
```shell
pip install moviepy
```

```python

from moviepy.editor import *

import os
def getMp3(path):
    video = VideoFileClip(path + '/video.mp4')
    audio = video.audio
    if not os.path.exists(path + '/video.wav'):
        audio.write_audiofile(path + '/video.wav')
        print(path + '/video.wav' + '完成')
    else:
        print(path + '/video.wav' + '已经存在')
    
    if not os.path.exists(path + '/video.mp3'):
        audio.write_audiofile(path + '/video.mp3')
        print(path + '/video.mp3' + '完成')
    else:
        print(path + '/video.mp3' + '已经存在')
```
# 字符串操作

## 字符串前补零

python中有一个zfill方法用来给字符串前面补0，非常有用

```python
n = "123"
s = n.zfill(5)
assert s == "00123"
```

zfill()也可以给负数补0

```python
n = "-123"
s = n.zfill(5)
assert s == "-0123"
```

对于纯数字，我们也可以通过格式化的方式来补0

```python
n = 123
s = "%05d" % n
assert s == "00123"
```
# 时间和时间戳的相互转换

## 将时间转换成时间戳

将如上的时间2016-05-05 20:28:54转换成时间戳，具体的操作过程为：

利用strptime()函数将时间转换成时间数组
利用mktime()函数将时间数组转换成时间戳
```python
#coding:UTF-8
import time

dt = "2016-05-05 20:28:54"

#转换成时间数组
timeArray = time.strptime(dt, "%Y-%m-%d %H:%M:%S")
#转换成时间戳
timestamp = time.mktime(timeArray)

print timestamp
```

## 重新格式化时间

重新格式化时间需要以下的两个步骤：

利用strptime()函数将时间转换成时间数组
利用strftime()函数重新格式化时间
```python
#coding:UTF-8
import time

dt = "2016-05-05 20:28:54"

#转换成时间数组
timeArray = time.strptime(dt, "%Y-%m-%d %H:%M:%S")
#转换成新的时间格式(20160505-20:28:54)
dt_new = time.strftime("%Y%m%d-%H:%M:%S",timeArray)

print dt_new
```

## 将时间戳转换成时间

在时间戳转换成时间中，首先需要将时间戳转换成localtime，再转换成时间的具体格式：

利用localtime()函数将时间戳转化成localtime的格式
利用strftime()函数重新格式化时间
```python
#coding:UTF-8
import time

timestamp = 1462451334

#转换成localtime
time_local = time.localtime(timestamp)
#转换成新的时间格式(2016-05-05 20:28:54)
dt = time.strftime("%Y-%m-%d %H:%M:%S",time_local)

print dt
```
## 按指定的格式获取当前时间

利用time()获取当前时间，再利用localtime()函数转换为localtime，最后利用strftime()函数重新格式化时间。

```python
#coding:UTF-8
import time

#获取当前时间
time_now = int(time.time())
#转换成localtime
time_local = time.localtime(time_now)
#转换成新的时间格式(2016-05-09 18:59:20)
dt = time.strftime("%Y-%m-%d %H:%M:%S",time_local)

print dt
```

## 计算两个日期之间天数

```python
>>> import datetime
>>> d1 = datetime.datetime(2018,10,31)   # 第一个日期
>>> d2 = datetime.datetime(2019,02,02)   # 第二个日期
>>> interval = d2 - d1                   # 两日期差距
>>> interval.days                        # 具体的天数                     
94
>>> interval.seconds                     # 具体的秒数
0
```

如果是两个日期还是带时，分，秒的话，同样可以算出两者的相距天数及秒数.
```python

>>> import datetime
>>> d1 = datetime.datetime(2018,10,31,10,30,00)
>>> d2 = datetime.datetime(2018,11,01,10,40,30)
>>> interval = d2 - d1
>>> interval                   # 第一项是天数，相距1天
datetime.timedelta(1, 630)
>>> interval.days              # 具体天数
1
>>> interval.seconds           # 额外秒数
630
>>> interval.total_seconds()   # 相差总秒数  
87030.0

```

# 向上取整，向下取整，四舍五入取整
```python
#encoding:utf-8
import math

#向上取整
print "math.ceil---"
print "math.ceil(2.3) => ", math.ceil(2.3)
print "math.ceil(2.6) => ", math.ceil(2.6)

#向下取整
print "\nmath.floor---"
print "math.floor(2.3) => ", math.floor(2.3)
print "math.floor(2.6) => ", math.floor(2.6)

#四舍五入
print "\nround---"
print "round(2.3) => ", round(2.3)
print "round(2.6) => ", round(2.6)

#这三个的返回结果都是浮点型
print "\n\nNOTE:every result is type of float"
print "math.ceil(2) => ", math.ceil(2)
print "math.floor(2) => ", math.floor(2)
print "round(2) => ", round(2)

```