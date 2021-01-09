
- [yield和yield from的区别](#yield和yield-from的区别)
- [深入理解Python的yield from语法](#深入理解python的yield-from语法)
- [为什么要使用协程](#为什么要使用协程)
- [yield from的用法详解](#yield-from的用法详解)
- [复杂应用：生成器的嵌套](#复杂应用生成器的嵌套)
- [为什么要使用yield from](#为什么要使用yield-from)
  
# yield和yield from的区别
yield 就是类似return 但是是延迟的return 包含yield方法叫生成器
yield from是为了函数内嵌生成器
yield 关键字也必须放在函数里面

# 深入理解Python的yield from语法

当你看到这一篇的时候，请确保你对生成器的知识，有一定的了解。当然不了解，也没有关系，你只要花个几分钟的时间，来看下我上一篇文章，就能够让你认识生成器，入门协程了。

  再次提醒：本系列所有的代码均在Python3下编写，也建议大家尽快投入到Python3的怀抱中来。

# 为什么要使用协程
在上一篇中，我们从生成器的基本认识与使用，成功过渡到了协程。
但一定有许多人，只知道协程是个什么东西，但并不知道为什么要用协程？换句话来说，并不知道在什么情况下用协程？它相比多线程来说，有哪些过人之处呢？
在开始讲yield from 之前，我想先解决一下这个给很多人带来困惑的问题。
举个例子。假如我们做一个爬虫。我们要爬取多个网页，这里简单举例两个网页(两个spider函数)，获取HTML（耗IO耗时），然后再对HTML对行解析取得我们感兴趣的数据。
我们的代码结构精简如下：
```python
def spider_01(url):
    html = get_html(url)
    ...
    data = parse_html(html)

def spider_02(url):
    html = get_html(url)
    ...
    data = parse_html(html)
```
复制代码
我们都知道，get_html()等待返回网页是非常耗IO的，一个网页还好，如果我们爬取的网页数据极其庞大，这个等待时间就非常惊人，是极大的浪费。
聪明的程序员，当然会想如果能在get_html()这里暂停一下，不用傻乎乎地去等待网页返回，而是去做别的事。等过段时间再回过头来到刚刚暂停的地方，接收返回的html内容，然后还可以接下去解析parse_html(html)。
利用常规的方法，几乎是没办法实现如上我们想要的效果的。所以Python想得很周到，从语言本身给我们实现了这样的功能，这就是yield语法。可以实现在某一函数中暂停的效果。
试着思考一下，假如没有协程，我们要写一个并发程序。可能有以下问题

  * 1）使用最常规的同步编程要实现异步并发效果并不理想，或者难度极高。
  
  * 2）由于GIL锁的存在，多线程的运行需要频繁的加锁解锁，切换线程，这极大地降低了并发性能；

而协程的出现，刚好可以解决以上的问题。它的特点有

  
  * 协程是在单线程里实现任务的切换的
  * 利用同步的方式去实现异步
  * 不再需要锁，提高了并发性能
  

# yield from的用法详解
yield from 是在Python3.3才出现的语法。所以这个特性在Python2中是没有的。
yield from 后面需要加的是可迭代对象，它可以是普通的可迭代对象，也可以是迭代器，甚至是生成器。
. 简单应用：拼接可迭代对象
我们可以用一个使用yield和一个使用yield from的例子来对比看下。
使用yield
```python
# 字符串
astr='ABC'
# 列表
alist=[1,2,3]
# 字典
adict={"name":"wangbm","age":18}
# 生成器
agen=(i for i in range(4,8))

def gen(*args, **kw):
    for item in args:
        for i in item:
            yield i

new_list=gen(astr, alist, adict， agen)
print(list(new_list))
# ['A', 'B', 'C', 1, 2, 3, 'name', 'age', 4, 5, 6, 7]

```
使用yield from
```python
# 字符串
astr='ABC'
# 列表
alist=[1,2,3]
# 字典
adict={"name":"wangbm","age":18}
# 生成器
agen=(i for i in range(4,8))

def gen(*args, **kw):
    for item in args:
        yield from item

new_list=gen(astr, alist, adict, agen)
print(list(new_list))
# ['A', 'B', 'C', 1, 2, 3, 'name', 'age', 4, 5, 6, 7]
```
由上面两种方式对比，可以看出，yield from后面加上可迭代对象，他可以把可迭代对象里的每个元素一个一个的yield出来，对比yield来说代码更加简洁，结构更加清晰。

# 复杂应用：生成器的嵌套
如果你认为只是 yield from 仅仅只有上述的功能的话，那你就太小瞧了它，它的更强大的功能还在后面。
当 yield from 后面加上一个生成器后，就实现了生成的嵌套。
当然实现生成器的嵌套，并不是一定必须要使用yield from，而是使用yield from可以让我们避免让我们自己处理各种料想不到的异常，而让我们专注于业务代码的实现。
如果自己用yield去实现，那只会加大代码的编写难度，降低开发效率，降低代码的可读性。既然Python已经想得这么周到，我们当然要好好利用起来。
讲解它之前，首先要知道这个几个概念

  * 1、调用方：调用委派生成器的客户端（调用方）代码
  * 2、委托生成器：包含yield from表达式的生成器函数
  * 3、子生成器：yield from后面加的生成器函数

你可能不知道他们都是什么意思，没关系，来看下这个例子。
这个例子，是实现实时计算平均值的。比如，第一次传入10，那返回平均数自然是10.第二次传入20，那返回平均数是(10+20)/2=15第三次传入30，那返回平均数(10+20+30)/3=20
```python
# 子生成器
def average_gen():
    total = 0
    count = 0
    average = 0
    while True:
        new_num = yield average
        count += 1
        total += new_num
        average = total/count

# 委托生成器
def proxy_gen():
    while True:
        yield from average_gen()

# 调用方
def main():
    calc_average = proxy_gen()
    next(calc_average)            # 预激下生成器
    print(calc_average.send(10))  # 打印：10.0
    print(calc_average.send(20))  # 打印：15.0
    print(calc_average.send(30))  # 打印：20.0

if __name__ == '__main__':
    main()
```

认真阅读以上代码，你应该很容易能理解，调用方、委托生成器、子生成器之间的关系。我就不多说了

委托生成器的作用是：在调用方与子生成器之间建立一个双向通道。

所谓的双向通道是什么意思呢？调用方可以通过send()直接发送消息给子生成器，而子生成器yield的值，也是直接返回给调用方。

你可能会经常看到有些代码，还可以在yield from前面看到可以赋值。这是什么用法？

你可能会以为，子生成器yield回来的值，被委托生成器给拦截了。你可以亲自写个demo运行试验一下，并不是你想的那样。因为我们之前说了，委托生成器，只起一个桥梁作用，它建立的是一个双向通道，它并没有权利也没有办法，对子生成器yield回来的内容做拦截。

为了解释这个用法，我还是用上述的例子，并对其进行了一些改造。添加了一些注释，希望你能看得明白。

按照惯例，我们还是举个例子。
```python

# 子生成器
def average_gen():
    total = 0
    count = 0
    average = 0
    while True:
        new_num = yield average
        if new_num is None:
            break
        count += 1
        total += new_num
        average = total/count

    # 每一次return，都意味着当前协程结束。
    return total,count,average

# 委托生成器
def proxy_gen():
    while True:
        # 只有子生成器要结束（return）了，yield from左边的变量才会被赋值，后面的代码才会执行。
        total, count, average = yield from average_gen()
        print("计算完毕！！\n总共传入 {} 个数值， 总和：{}，平均数：{}".format(count, total, average))

# 调用方
def main():
    calc_average = proxy_gen()
    next(calc_average)            # 预激协程
    print(calc_average.send(10))  # 打印：10.0
    print(calc_average.send(20))  # 打印：15.0
    print(calc_average.send(30))  # 打印：20.0
    calc_average.send(None)      # 结束协程
    # 如果此处再调用calc_average.send(10)，由于上一协程已经结束，将重开一协程

if __name__ == '__main__':
    main()
```

运行后，输出
```python
10.0
15.0
20.0
计算完毕！！
总共传入 3 个数值， 总和：60，平均数：20.0
```

# 为什么要使用yield from
学到这里，我相信你肯定要问，既然委托生成器，起到的只是一个双向通道的作用，我还需要委托生成器做什么？我调用方直接调用子生成器不就好啦？

高能预警~~~

下面我们来一起探讨一下，到底yield from 有什么过人之处，让我们非要用它不可。

. 因为它可以帮我们处理异常

如果我们去掉委托生成器，而直接调用子生成器。那我们就需要把代码改成像下面这样，我们需要自己捕获异常并处理。而不像使yield from那样省心。
```python
# 子生成器
def average_gen():
    total = 0
    count = 0
    average = 0
    while True:
        new_num = yield average
        if new_num is None:
            break
        count += 1
        total += new_num
        average = total/count
    return total,count,average

# 调用方
def main():
    calc_average = average_gen()
    next(calc_average)            # 预激协程
    print(calc_average.send(10))  # 打印：10.0
    print(calc_average.send(20))  # 打印：15.0
    print(calc_average.send(30))  # 打印：20.0

    # ----------------注意-----------------
    try:
        calc_average.send(None)
    except StopIteration as e:
        total, count, average = e.value
        print("计算完毕！！\n总共传入 {} 个数值， 总和：{}，平均数：{}".format(count, total, average))
    # ----------------注意-----------------

if __name__ == '__main__':
    main()
```

此时的你，可能会说，不就一个StopIteration的异常吗？自己捕获也没什么大不了的。
你要是知道yield from在背后为我们默默无闻地做了哪些事，你就不会这样说了。
具体yield from为我们做了哪些事，可以参考如下这段代码。
```python
#一些说明
"""
_i：子生成器，同时也是一个迭代器
_y：子生成器生产的值
_r：yield from 表达式最终的值
_s：调用方通过send()发送的值
_e：异常对象
"""

_i = iter(EXPR)

try:
    _y = next(_i)
except StopIteration as _e:
    _r = _e.value

else:
    while 1:
        try:
            _s = yield _y
        except GeneratorExit as _e:
            try:
                _m = _i.close
            except AttributeError:
                pass
            else:
                _m()
            raise _e
        except BaseException as _e:
            _x = sys.exc_info()
            try:
                _m = _i.throw
            except AttributeError:
                raise _e
            else:
                try:
                    _y = _m(*_x)
                except StopIteration as _e:
                    _r = _e.value
                    break
        else:
            try:
                if _s is None:
                    _y = next(_i)
                else:
                    _y = _i.send(_s)
            except StopIteration as _e:
                _r = _e.value
                break
RESULT = _r
```

以上的代码，稍微有点复杂，有兴趣的同学可以结合以下说明去研究看看。

* 迭代器（即可指子生成器）产生的值直接返还给调用者

* 任何使用send()方法发给委派生产器（即外部生产器）的值被直接传递给迭代器。如果send值是None，则调用迭代器next()方法；如果不为None，则调用迭代器的send()方法。如果对迭代器的调用产生StopIteration异常，委派生产器恢复继续执行yield from后面的语句；若迭代器产生其他任何异常，则都传递给委派生产器。

* 子生成器可能只是一个迭代器，并不是一个作为协程的生成器，所以它不支持.throw()和.close()方法,即可能会产生AttributeError 异常。

* 除了GeneratorExit 异常外的其他抛给委派生产器的异常，将会被传递到迭代器的throw()方法。如果迭代器throw()调用产生了StopIteration异常，委派生产器恢复并继续执行，其他异常则传递给委派生产器。

* 如果GeneratorExit异常被抛给委派生产器，或者委派生产器的close()方法被调用，如果迭代器有close()的话也将被调用。如果close()调用产生异常，异常将传递给委派生产器。否则，委派生产器将抛出GeneratorExit 异常。

* 当迭代器结束并抛出异常时，yield from表达式的值是其StopIteration 异常中的第一个参数。

* 一个生成器中的return expr语句将会从生成器退出并抛出 StopIteration(expr)异常。

没兴趣看的同学，只要知道，yield from帮我们做了很多的异常处理，而且全面，而这些如果我们要自己去实现的话，一个是编写代码难度增加，写出来的代码可读性极差，这些我们就不说了，最主要的是很可能有遗漏，只要哪个异常没考虑到，都有可能导致程序崩溃什么的。

作者：王一白
链接：https://juejin.cn/post/6844903632534503437
来源：掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。