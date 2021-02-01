- [运算符](#运算符)
  - [算术运算符](#算术运算符)
  - [关系运算符](#关系运算符)
  - [布尔运算符](#布尔运算符)
  - [逻辑运算符](#逻辑运算符)
  - [字符串运算符](#字符串运算符)
  - [文件测试运算符](#文件测试运算符)
  - [变量自加一](#变量自加一)
- [流程控制](#流程控制)
  - [if else](#if-else)
    - [if](#if)
    - [if else](#if-else-1)
  - [for 循环](#for-循环)
  - [while 语句](#while-语句)
    - [无限循环](#无限循环)
  - [until 循环](#until-循环)
  - [case](#case)
  - [跳出循环](#跳出循环)
    - [break命令](#break命令)
    - [continue](#continue)
    - [case ... esac](#case--esac)
- [系统函数](#系统函数)
  - [sleep](#sleep)

# 运算符


## 算术运算符
下表列出了常用的算术运算符，假定变量 a 为 10，变量 b 为 20：

运算符 | 说明 | 举例
------- | ------- | -------
+ | 加法 | `expr $a + $b` 结果为 30。
- | 减法 | `expr $a - $b` 结果为 -10。
* | 乘法 | `expr $a \* $b` 结果为 | 200。
/ | 除法 | `expr $b / $a` 结果为 2。
% | 取余 | `expr $b % $a` 结果为 0。
= | 赋值 | a=$b 将把变量 b 的值赋给 a。
== | 相等。用于比较两个数字，相同则返回 true。 | [ $a == $b ] 返回 false。
!= | 不相等。用于比较两个数字，不相同则返回 true。 | [ $a != $b ] 返回 true。

注意：条件表达式要放在方括号之间，并且要有空格，例如: [$a==$b] 是错误的，必须写成 [ $a == $b ]。

## 关系运算符

关系运算符只支持数字，不支持字符串，除非字符串的值是数字。
下表列出了常用的关系运算符，假定变量 a 为 10，变量 b 为 20：

运算符 | 说明 | 举例
------- | ------- | -------
-eq | 检测两个数是否相等，相等返回 true。 | [ $a -eq $b ] 返回 false。
-ne | 检测两个数是否不相等，不相等返回 true。 | [ $a -ne $b ] 返回 true。
-gt | 检测左边的数是否大于右边的，如果是，则返回 true。 | [ $a -gt $b ] 返回 false。
-lt | 检测左边的数是否小于右边的，如果是，则返回 true。 | [ $a -lt $b ] 返回 true。
-ge | 检测左边的数是否大于等于右边的，如果是，则返回 true。 | [ $a -ge $b ] 返回 false。
-le | 检测左边的数是否小于等于右边的，如果是，则返回 true。 | [ $a -le $b ] 返回 true。

## 布尔运算符

下表列出了常用的布尔运算符，假定变量 a 为 10，变量 b 为 20：

运算符 | 说明 | 举例
------- | ------- | -------
! | 非运算，表达式为 true 则返回 false，否则返回 true。 | [ ! false ] 返回 true。
-o | 或运算，有一个表达式为 true 则返回 true。 | [ $a -lt 20 -o $b -gt 100 ] 返回 true。
-a | 与运算，两个表达式都为 true 才返回 true。 | [ $a -lt 20 -a $b -gt 100 ] 返回 false。

## 逻辑运算符

以下介绍 Shell 的逻辑运算符，假定变量 a 为 10，变量 b 为 20:
运算符 | 说明 | 举例
------- | ------- | -------
&& | 逻辑的 AND | [[ $a -lt 100 && $b -gt 100 ]] 返回 false
\|\| | 逻辑的 OR | [[ $a -lt 100 \|\| $b -gt 100 ]] 返回 true

## 字符串运算符
下表列出了常用的字符串运算符，假定变量 a 为 "abc"，变量 b 为 "efg"：
运算符 | 说明 | 举例
------- | ------- | -------
= | 检测两个字符串是否相等，相等返回 true。 | [ $a = $b ] 返回 false。
!= | 检测两个字符串是否相等，不相等返回 true。 | [ $a != $b ] 返回 true。
-z | 检测字符串长度是否为0，为0返回 true。 | [ -z $a ] 返回 false。
-n | 检测字符串长度是否不为 0，不为 0 返回 true。 | [ -n "$a" ] 返回 true。
$ | 检测字符串是否为空，不为空返回 true。 | [ $a ] 返回 true。


## 文件测试运算符
文件测试运算符用于检测 Unix 文件的各种属性。
属性检测描述如下：

操作符 | 说明 | 举例
------- | ------- | -------
-b file | 检测文件是否是块设备文件，如果是，则返回 true。 | [ -b $file ] 返回 false。
-c file | 检测文件是否是字符设备文件，如果是，则返回 true。 | [ -c $file ] 返回 false。
-d file | 检测文件是否是目录，如果是，则返回 true。 | [ -d $file ] 返回 false。
-f file | 检测文件是否是普通文件（既不是目录，也不是设备文件），如果是，则返回 true。 | [ -f $file ] 返回 true。
-g file | 检测文件是否设置了 SGID 位，如果是，则返回 true。 | [ -g $file ] 返回 false。
-k file | 检测文件是否设置了粘着位(Sticky Bit)，如果是，则返回 true。 | [ -k $file ] 返回 false。
-p file | 检测文件是否是有名管道，如果是，则返回 true。 | [ -p $file ] 返回 false。
-u file | 检测文件是否设置了 SUID 位，如果是，则返回 true。 | [ -u $file ] 返回 false。
-r file | 检测文件是否可读，如果是，则返回 true。 | [ -r $file ] 返回 true。
-w file | 检测文件是否可写，如果是，则返回 true。 | [ -w $file ] 返回 true。
-x file | 检测文件是否可执行，如果是，则返回 true。 | [ -x $file ] 返回 true。
-s file | 检测文件是否为空（文件大小是否大于0），不为空返回 true。 | [ -s $file ] 返回 true。
-e file | 检测文件（包括目录）是否存在，如果是，则返回 true。 | [ -e $file ] 返回 true。
其他检查符：
-S: 判断某文件是否 socket。
-L: 检测文件是否存在并且是一个符号链接。


## 变量自加一

```sh
#定义整型变量
a=1
echo $a
 
#第一种整型变量自增方式
a=$(($a+1))
echo $a
 
#第二种整型变量自增方式
a=$[$a+1]
echo $a
 
#第三种整型变量自增方式
a=`expr $a + 1`
echo $a
 
#第四种整型变量自增方式
let a++
echo $a
 
#第五种整型变量自增方式
let a+=1
echo $a
 
#第六种整型变量自增方式
((a++))
echo $a

```

# 流程控制

## if else

在sh/bash里如果else分支没有语句执行，就不要写这个else。

### if
if 语句语法格式：
```shell
if condition
then
    command1 
    command2
    ...
    commandN 
fi
```
写成一行（适用于终端命令提示符）：
```
if [ $(ps -ef | grep -c "ssh") -gt 1 ]; then echo "true"; fi
```
末尾的fi就是if倒过来拼写，后面还会遇到类似的。

### if else

if else 语法格式：
```Shell
if condition
then
    command1 
    command2
    ...
    commandN
else
    command
fi
if else-if else
if else-if else 语法格式：
if condition1
then
    command1
elif condition2 
then 
    command2
else
    commandN
fi
```
以下实例判断两个变量是否相等：
实例
```shell
a=10
b=20
if [ $a == $b ]
then
   echo "a 等于 b"
elif [ $a -gt $b ]
then
   echo "a 大于 b"
elif [ $a -lt $b ]
then
   echo "a 小于 b"
else
   echo "没有符合的条件"
fi
```
输出结果：
```S
a 小于 b
```

## for 循环

与其他编程语言类似，Shell支持for循环。
for循环一般格式为：
```
for var in item1 item2 ... itemN
do
    command1
    command2
    ...
    commandN
done
```
写成一行：
```
for var in item1 item2 ... itemN; do command1; command2… done;
```
当变量值在列表里，for循环即执行一次所有命令，使用变量名获取列表中的当前取值。命令可为任何有效的shell命令和语句。in列表可以包含替换、字符串和文件名。
in列表是可选的，如果不用它，for循环使用命令行的位置参数。
例如，顺序输出当前列表中的数字：
实例
```
for loop in 1 2 3 4 5
do
    echo "The value is: $loop"
done
```
输出结果：
```
The value is: 1
The value is: 2
The value is: 3
The value is: 4
The value is: 5
```
顺序输出字符串中的字符：
```
for str in 'This is a string'
do
    echo $str
done
```
输出结果：
```
This is a string
```

## while 语句
while循环用于不断执行一系列命令，也用于从输入文件中读取数据；命令通常为测试条件。其格式为：
```
while condition
do
    command
done
```
以下是一个基本的while循环，测试条件是：如果int小于等于5，那么条件返回真。int从0开始，每次循环处理时，int加1。运行上述脚本，返回数字1到5，然后终止。
实例
```
#!/bin/bash
int=1
while(( $int<=5 ))
do
    echo $int
    let "int++"
done
```
运行脚本，输出：
```
1
2
3
4
5
```
以上实例使用了 Bash let 命令，它用于执行一个或多个表达式，变量计算中不需要加上 $ 来表示变量，具体可查阅：Bash let 命令
。
while循环可用于读取键盘信息。下面的例子中，输入信息被设置为变量FILM，按<Ctrl-D>结束循环。
实例
```
echo '按下 <CTRL-D> 退出'
echo -n '输入你最喜欢的网站名: '
while read FILM
do
    echo "是的！$FILM 是一个好网站"
done
```
运行脚本，输出类似下面：
按下 <CTRL-D> 退出
输入你最喜欢的网站名:菜鸟教程
是的！菜鸟教程 是一个好网站

### 无限循环
无限循环语法格式：
```
while :
do
    command
done
或者
while true
do
    command
done
```
或者
```
for (( ; ; ))
```

## until 循环
until 循环执行一系列命令直至条件为 true 时停止。
until 循环与 while 循环在处理方式上刚好相反。
一般 while 循环优于 until 循环，但在某些时候—也只是极少数情况下，until 循环更加有用。
until 语法格式:
```
until condition
do
    command
done
```
condition 一般为条件表达式，如果返回值为 false，则继续执行循环体内的语句，否则跳出循环。
以下实例我们使用 until 命令来输出 0 ~ 9 的数字：
实例
```
#!/bin/bash

a=0

until [ ! $a -lt 10 ]
do
   echo $a
   a=`expr $a + 1`
done
```
运行结果：
输出结果为：
```
0
1
2
3
4
5
6
7
8
9
```
## case
Shell case语句为多选择语句。可以用case语句匹配一个值与一个模式，如果匹配成功，执行相匹配的命令。case语句格式如下：
```
case 值 in
模式1)
    command1
    command2
    ...
    commandN
    ;;
模式2）
    command1
    command2
    ...
    commandN
    ;;
esac
```
case工作方式如上所示。取值后面必须为单词in，每一模式必须以右括号结束。取值可以为变量或常数。匹配发现取值符合某一模式后，其间所有命令开始执行直至 ;;。

取值将检测匹配的每一个模式。一旦模式匹配，则执行完匹配模式相应命令后不再继续其他模式。如果无一匹配模式，使用星号 * 捕获该值，再执行后面的命令。
下面的脚本提示输入1到4，与每一种模式进行匹配：

实例
```S
echo '输入 1 到 4 之间的数字:'
echo '你输入的数字为:'
read aNum
case $aNum in
    1)  echo '你选择了 1'
    ;;
    2)  echo '你选择了 2'
    ;;
    3)  echo '你选择了 3'
    ;;
    4)  echo '你选择了 4'
    ;;
    *)  echo '你没有输入 1 到 4 之间的数字'
    ;;
esac
```
输入不同的内容，会有不同的结果，例如：
```S
输入 1 到 4 之间的数字:
你输入的数字为:
3
你选择了 3
```
## 跳出循环
在循环过程中，有时候需要在未达到循环结束条件时强制跳出循环，Shell使用两个命令来实现该功能：break和continue。

### break命令

break命令允许跳出所有循环（终止执行后面的所有循环）。

下面的例子中，脚本进入死循环直至用户输入数字大于5。要跳出这个循环，返回到shell提示符下，需要使用break命令。

实例
```
#!/bin/bash
while :
do
    echo -n "输入 1 到 5 之间的数字:"
    read aNum
    case $aNum in
        1|2|3|4|5) echo "你输入的数字为 $aNum!"
        ;;
        *) echo "你输入的数字不是 1 到 5 之间的! 游戏结束"
            break
        ;;
    esac
done
```
执行以上代码，输出结果为：
```
输入 1 到 5 之间的数字:3
你输入的数字为 3!
输入 1 到 5 之间的数字:7
你输入的数字不是 1 到 5 之间的! 游戏结束
```
### continue
continue命令与break命令类似，只有一点差别，它不会跳出所有循环，仅仅跳出当前循环。
对上面的例子进行修改：

实例

```S
#!/bin/bash
while :
do
    echo -n "输入 1 到 5 之间的数字: "
    read aNum
    case $aNum in
        1|2|3|4|5) echo "你输入的数字为 $aNum!"
        ;;
        *) echo "你输入的数字不是 1 到 5 之间的!"
            continue
            echo "游戏结束"
        ;;
    esac
done
```
运行代码发现，当输入大于5的数字时，该例中的循环不会结束，语句 echo "游戏结束" 永远不会被执行。
### case ... esac
case ... esac 与其他语言中的 switch ... case 语句类似，是一种多分枝选择结构，每个 case 分支用右圆括号开始，用两个分号 ;; 表示 break，即执行结束，跳出整个 case ... esac 语句，esac（就是 case 反过来）作为结束标记。

case ... esac 语法格式如下：

```S
case 值 in
模式1)
    command1
    command2
    command3
    ;;
模式2）
    command1
    command2
    command3
    ;;
*)
    command1
    command2
    command3
    ;;
esac
```
case 后为取值，值可以为变量或常数。
值后为关键字 in，接下来是匹配的各种模式，每一模式最后必须以右括号结束，模式支持正则表达式。
实例
```
#!/bin/sh

site="runoob"

case "$site" in
   "runoob") echo "菜鸟教程" 
   ;;
   "google") echo "Google 搜索" 
   ;;
   "taobao") echo "淘宝网" 
   ;;
esac
```
输出结果为：
```
菜鸟教程
```

# 系统函数

## sleep

```sh
sleep 3
```

