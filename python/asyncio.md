
# Python中 asyncio 模块 详解
## 1. 概述

Python中 asyncio 模块内置了对异步IO的支持，用于处理异步IO；是Python 3.4版本引入的标准库。
asyncio 的编程模型就是一个消息循环。我们从 asyncio 块中直接获取一个 EventLoop 的引用，然后把需要执行的协程扔到 EventLoop 中执行，就实现了异步IO。

## 2. 用asyncio实现Hello world

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# @Time    : 2019/1/9 11:23
# @Author  : Arrow and Bullet
# @FileName: test.py
# @Software: PyCharm
# @Blog    ：https://blog.csdn.net/qq_41800366
import asyncio

@asyncio.coroutine
def hello():
    print("Hello world!")
    # 异步调用asyncio.sleep(2): 
    yield from asyncio.sleep(2)
    print("Hello again!")

# 获取EventLoop:
loop = asyncio.get_event_loop()
# 执行coroutine
loop.run_until_complete(hello())
loop.close()
```
@asyncio.coroutine 把一个 generator 标记为 coroutine 类型，然后，我们就把这个 coroutine 扔到 EventLoop 中执行。

hello() 会首先打印出Hello world!，然后，yield from语法可以让我们方便地调用另一个generator。由于 asyncio.sleep() 也是一个 coroutine，所以线程不会等待 asyncio.sleep() ，而是直接中断并执行下一个消息循环。当asyncio.sleep()返回时，线程就可以从yield from拿到返回值（此处是None），然后接着执行下一行语句。

把asyncio.sleep(2)看成是一个耗时2秒的IO操作(比如读取大文件)，在此期间，主线程并未等待，而是去执行 EventLoop 中其他可以执行的 coroutine 了，因此可以实现并发执行。

我们用task封装两个coroutine试试：

```python
import threading
import asyncio


@asyncio.coroutine
def hello():
    print('Hello world! (%s)' % threading.currentThread())
    yield from asyncio.sleep(2)
    print('Hello again! (%s)' % threading.currentThread())


loop = asyncio.get_event_loop()
tasks = [hello(), hello()]
loop.run_until_complete(asyncio.wait(tasks))
loop.close()
```
观察执行过程：

```python
Hello world! (<_MainThread(MainThread, started 140735195337472)>)
Hello world! (<_MainThread(MainThread, started 140735195337472)>)
(暂停约2秒)
Hello again! (<_MainThread(MainThread, started 140735195337472)>)
Hello again! (<_MainThread(MainThread, started 140735195337472)>)
```
由打印的当前线程名称可以看出，两个 coroutine 是由同一个线程并发执行的。

如果把 asyncio.sleep() 换成真正的IO操作，则多个 coroutine 就可以由一个线程并发执行。

我们用asyncio的异步网络连接来获取sina、sohu和163的网站首页：

```python
import asyncio


@asyncio.coroutine
def wget(host):
    print('wget %s...' % host)
    connect = asyncio.open_connection(host, 80)  # 创建连接
    reader, writer = yield from connect
    header = 'GET / HTTP/1.0\r\nHost: %s\r\n\r\n' % host
    writer.write(header.encode('utf-8'))
    yield from writer.drain()
    while True:
        line = yield from reader.readline()
        if line == b'\r\n':
            break
        print('%s header > %s' % (host, line.decode('utf-8').rstrip()))
    # Ignore the body, close the socket
    writer.close()


loop = asyncio.get_event_loop()
tasks = [wget(host) for host in ['www.sina.com.cn', 'www.sohu.com', 'www.163.com']]
loop.run_until_complete(asyncio.wait(tasks))
loop.close()
```

执行结果如下：
```python

wget www.sohu.com...
wget www.sina.com.cn...
wget www.163.com...
(等待一段时间)
(打印出sohu的header)
www.sohu.com header > HTTP/1.1 200 OK
www.sohu.com header > Content-Type: text/html
...
(打印出sina的header)
www.sina.com.cn header > HTTP/1.1 200 OK
www.sina.com.cn header > Date: Wed, 20 May 2015 04:56:33 GMT
...
(打印出163的header)
www.163.com header > HTTP/1.0 302 Moved Temporarily
www.163.com header > Server: Cdn Cache Server V2.0
...

```
可见3个连接由一个线程通过coroutine并发完成。

1. 小结

asyncio提供了完善的异步IO支持；

异步操作需要在coroutine中通过yield from完成；

多个coroutine可以封装成一组Task然后并发执行。

[转自](https://blog.csdn.net/qq_41800366/article/details/86139675)