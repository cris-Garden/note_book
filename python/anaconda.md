- [对象转json](#对象转json)
- [Numpy](#numpy)
- [Pandas](#pandas)
  - [DataFrame](#dataframe)
    - [DataFrame的创建](#dataframe的创建)
      - [创建一个空的dataframe](#创建一个空的dataframe)
      - [用list的数据创建dataframe](#用list的数据创建dataframe)
      - [用numpy的矩阵创建dataframe](#用numpy的矩阵创建dataframe)
      - [用dict的数据创建DataFrame](#用dict的数据创建dataframe)
      - [读取csv或者excel文件为DataFrame格式](#读取csv或者excel文件为dataframe格式)
      - [excel一个表格中可能有多个sheet，sheetname可以进行选取](#excel一个表格中可能有多个sheetsheetname可以进行选取)
      - [sqlite读取](#sqlite读取)
    - [DataFrame的一些描述和类型](#dataframe的一些描述和类型)
      - [describe会](#describe会)
      - [head](#head)
      - [sum,mean,count,max,min](#summeancountmaxmin)
      - [dtypes查看dataframe的数据类型](#dtypes查看dataframe的数据类型)
      - [size查看dataframe的数据数目](#size查看dataframe的数据数目)
      - [shape查看dataframe的形状](#shape查看dataframe的形状)
      - [ndim返回列数](#ndim返回列数)
      - [axes查看横纵坐标的标签名](#axes查看横纵坐标的标签名)
    - [DataFrame的切片](#dataframe的切片)
      - [iloc索引或切片（iloc中只能取整数值）：](#iloc索引或切片iloc中只能取整数值)
      - [loc索引或切片（loc中可以取str）：](#loc索引或切片loc中可以取str)
      - [筛选出dataframe中有某一个或某几个字符串的列：](#筛选出dataframe中有某一个或某几个字符串的列)
      - [筛选出dataframe中不含某一个或某几个字符串的列，相当于反选](#筛选出dataframe中不含某一个或某几个字符串的列相当于反选)
    - [缺失值的处理](#缺失值的处理)
      - [缺失值可以删除也可以用均值或者0等数填充：](#缺失值可以删除也可以用均值或者0等数填充)
      - [删除缺失值时可以指定列：](#删除缺失值时可以指定列)
- [Matplotlib](#matplotlib)
- [Seaborn](#seaborn)

# 对象转json

[参考](对象转json.md)

# Numpy


# Pandas

## DataFrame

### DataFrame的创建

#### 创建一个空的dataframe 
```python
df=pd.DataFrame(columns={"a":"","b":"","c":""},index=[0])
```

#### 用list的数据创建dataframe
```python
a = [['2', '1.2', '4.2'], ['0', '10', '0.3'], ['1', '5', '0']]
df = pd.DataFrame(a, columns=['one', 'two', 'three'])
```

#### 用numpy的矩阵创建dataframe
```python
array = np.random.rand(5,3)
df = pd.DataFrame(array,columns=['first','second','third'])
```

#### 用dict的数据创建DataFrame
```python
data = { 'row1' : [1,2,3,4], 'row2' : ['a' , 'b' , 'c' , 'd'] }
df = pd.DataFrame(data)
dict = { 'row1' : [1,2,3,4], 'row2' : ['a' , 'b' , 'c' , 'd'] }
df = pd.DataFrame.from_dict(dict,orient='index').T
```

#### 读取csv或者excel文件为DataFrame格式
```python
df=pd.read_csv('D:/Program Files/example.csv')
```

#### excel一个表格中可能有多个sheet，sheetname可以进行选取

```python
df = df.read_excel('D:/Program Files/example.xls',sheetname=0)
```

#### sqlite读取
```python
import sys
import pandas as pd
#
import sqlite3
# ------------------------------------------------------------------
sys.stderr.write("*** 開始 ***\n")
#
file_sqlite3 = "./grammar.db"
conn = sqlite3.connect(file_sqlite3)
#
df=pd.read_sql_query('select * from ss', conn)
print(df)
```

### DataFrame的一些描述和类型

#### describe会
显示dataframe的一些基本统计数据，数量、均值、中位数、标准差等

#### head
会显示dataframe的前几行，后几行：

#### sum,mean,count,max,min
单独计算某列的统计值

```python
df['one'].sum()
df['one'].mean()
df['one'].count()
df['one'].max()
df['one'].min()
```

#### dtypes查看dataframe的数据类型

```python
print （df.dtypes）
```

#### size查看dataframe的数据数目

```python
print （df.size）
```
#### shape查看dataframe的形状

```python
print (df.shape)
```

#### ndim返回列数

```python
print (df.ndim)
```

#### axes查看横纵坐标的标签名

```python
print (df.axes)
```


### DataFrame的切片

#### iloc索引或切片（iloc中只能取整数值）：

```python
print df.iloc[1,:] #第1行，所有列
print df.iloc[:,[0,2]] #第0行，第0列和第2列
print df['one'].iloc[2] #列名索引+行号
```
　
#### loc索引或切片（loc中可以取str）：

```python
print data.loc[0:1, ['one', three']] #
　　
```
#### 筛选出dataframe中有某一个或某几个字符串的列：

```python
ist=['key1','key2']
df = df[df['one'].isin(list)]
```

#### 筛选出dataframe中不含某一个或某几个字符串的列，相当于反选

```python
df = df[~df['one'].isin(list)]

```

 

### 缺失值的处理

#### 缺失值可以删除也可以用均值或者0等数填充：

```python
df.fillna(df1.mean())
df.fillna(0)
```

#### 删除缺失值时可以指定列：

```python
df = df.dropna(subset=['one','two'])

```








# Matplotlib


# Seaborn


