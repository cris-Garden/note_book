背景：

给app写接口时经常会遇到将一个model转为json返回。

问题：

网上也有类似方法，只是搜索结果多少有些问题，总是搜了好一会儿才找到合适的方法，另外，网上更多集中的只是简单些的对象，对于复杂的对象，还是不容易找到好的方式。

方案(python3.6)：

## 对象转json：

model类
```python
class People():
    def __init__(self, name, age, pet):
        self.name = name
        self.age = age
        self.pet = pet
class Pet():
    def __init__(self, pet_type, pet_name):
        self.pet_type = pet_type
        self.pet_name = pet_name
```
将Pet对象转json：

```python
import json
def pet2json():
    pet = Pet('Cat', 'Lili')
    js = json.dumps(pet.__dict__)
    print(js)
```
结果：{“pet_type”: “Cat”, “pet_name”: “Lili”} 
小结：充分利用了Python对象的dict方法，Python下一切皆对象，每个对象都有多个属性(attribute)，Python对属性有一套统一的管理方案。dict是用来存储对象属性的一个字典，其键为属性名，值为属性的值。dict可直接json化。

## 嵌套对象转json：

刚才的People类可看做是嵌套类，即有一个属性是另一个类的实例，此时，若用上面的方法来json化Person对象，会有问题，如下【错误】：

```python
def simple_person():
    pet = Pet('Cat', 'Lili')
    p = People('Xiaoming', 12,pet)
    json_data = json.dumps(p.__dict__)
    print(json_data)
```
结果：报异常TypeError: Object of type ‘Pet’ is not JSON serializable 
原因：json只能针对JSON serializable对象直接进行json化，而一般只有内置的类型，比如string,int,list和dict等才能直接序列化，代码中p._ dict _是个dict类型，但是其pet属性仍是自定义的类，是不能直接json化的。 
解决方式【正确】：

```python
def simple_person():
    pet = Pet('Cat', 'Lili')
    p = People('Xiaoming', 12,pet.__dict__)
    json_data = json.dumps(p.__dict__)
    print(json_data)
```
结果：{“name”: “Xiaoming”, “age”: 12, “pet”: {“pet_type”: “Cat”, “pet_name”: “Lili”}} 
小结：充分利用_ dict _方法。

## django的model转json：

首先有个model类

```python
class Person(models.Model):
    name = models.CharField(max_length=50, null=False)
    age = models.IntegerField(default=0)
    pid = models.CharField(max_length=20, unique=True)
    gender = models.IntegerField(default=0)
```
针对该Person类，有两种常见情况需要提供其json： 
1：根据pid查询person记录； 
2：根据某些条件，查询一些person记录。

此时数据库里已经插入了一些数据

这里要提一下网上比较常见的一种方式，需要用到django.core.serializers，这个类的serialize(format, queryset, **options)方法，很明显，这个方式只能作用与queryset格式，并且通过例子（不再列出），得到的结果类似这种 [{“model”: “polls.person”, “pk”: 2, “fields”: {“name”: “Cysion”, “age”: 29, “pid”: “3708261989”, “gender”: 0}}]，出现了model,pk，field等属性，不但用不到（对app来说），而且还增加了其它属性的使用复杂度。这个在官网的说明文档里也是如此处理，但是作者并不推荐。

方案：

我们还是使用_ dict _这个利器，首先，我们根据pid获得一个Person对象，然后利用dict方法打印看看结果（错误）
```python
    req_pid=3708262007//request中得到
    try:
        rt = Person.objects.get(pid=req_pid)
        print(rt.__dict__)
        return HttpResponse(json.dumps(rt.__dict__),content_type='application/json')
        # return JsonResponse(rt.__dict__, safe=False)//另一种方式
    except:
        return JsonResponse(datalogic.get_comon_resp(1, '没有查询到对应数据'))
```
结果是：print结果{‘_state’: < django.db.models.base.ModelState object at 0x0000000004C80860 >, ‘id’: 17, ‘name’: ‘zhaoliu’, ‘age’: 10, ‘pid’: ‘3708262007’, ‘gender’: 1}，啧啧，又多了些属性，特别是这个_state，是不能序列化的，所以上述并不能直接返回想要的结果。 
作者：刘咸尚

解决方式：既然_state无用，且影响了结果，那我们直接临时除去，不就返回了想要的结果吗（正确）。

```python
req_pid = request.POST.get('pid')
        try:
            rt = Person.objects.get(pid=req_pid)
            rt.__dict__.pop("_state")
            return JsonResponse(rt.__dict__, safe=False)
        except:
            return JsonResponse(datalogic.get_comon_resp(1, '没有查询到对应数据'))
```
接口返回结果是：
```json
{
    "id": 17,
    "name": "zhaoliu",
    "age": 10,
    "pid": "3708262007",
    "gender": 1
}
```
正是客户端需要的。

最后，是返回列表的，比如需要这种结果
```json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "id": 2,
            "name": "Cysion",
            "age": 29,
            "pid": "3708261989",
            "gender": 0
        },
        {
            "id": 11,
            "name": "Sophia",
            "age": 22,
            "pid": "3708261998",
            "gender": 1
        },
        {
            "id": 15,
            "name": "lisi",
            "age": 13,
            "pid": "3708262005",
            "gender": 0
        }
    ]
}
```

实现思路同上面类似，首先数据库查询后得到QuerySet，其不能直接json化（通过serializer得到的不好看，也不好处理，大量的属性处理还比较费劲），需要将其遍历得到每个对象，然后将其属性字典加入到list中，最后将其添加到通用dict中

```python
pers = Person.objects.all()
result = {"code":0,"msg":"成功"}
L = []
for p in pers:
    p.__dict__.pop("_state")//需要除去，否则不能json化
    L.append(p.__dict__)//注意，实际是个json拼接的过程，不能直接添加对象
result ['data'] = L
```

这个时候result 就是个可以直接json化的对象了，通过

return JsonResponse(result, safe=False)
