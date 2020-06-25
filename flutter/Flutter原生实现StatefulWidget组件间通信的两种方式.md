日常开发中有多种场景需要StatefulWidget组件之间进行通信，例如，PageView页面跳转时需要处理子页面逻辑，Widget间传递数据，回调方法等等，组件间通信可以用Event Bus，Redux等库来实现。本文介绍2种Flutter原生实现StatefulWidget间组件通信的方法。

**演示动画**

![img](https:////upload-images.jianshu.io/upload_images/7904911-53f2bf8a31e6b11f.gif?imageMogr2/auto-orient/strip|imageView2/2/w/320)

组件通信

**方法1：Global Key通信**
创建一个GlobalKey `final key = GlobalKey();`，通过 `key.currentState`访问State对象的公共属性和方法。

1. 声明一个全局的GlobalKey



```dart
final key = GlobalKey<MyStatefulWidgetOneState>();
```

1. `StatefulWidgetOne`的构造方法传入这个key



```dart
class MyStatefulWidgetOne extends StatefulWidget {
  MyStatefulWidgetOne({ Key key }) : super(key: key);
  MyStatefulWidgetOneState createState() =>   
  MyStatefulWidgetOneState();
}
```

1. `MyStatefulWidgetOneState`种定义 `updateMessage`方法，这个方法可以作为一个回调在外部触发。



```cpp
void updateMessage(String msg) {
    setState((){
      _message = msg;
    });
}
```

1. `MyStatefulWidgetTwoState`中，通过key可以取到WidgetOneState的public属性，也能通过调用`updateMessage`方法来实现对`MyStatefulWidgetTwoState`的更改。



```csharp
  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        Divider(),
        Text('Widget two'),
        Container(height: 20.0,),
        RaisedButton(
          child: Text("Get Current Message"),
          onPressed: () {
            setState(() {
              _objectOne = key.currentState.message;
            });
          },
        ),
        Text(_objectOne),
        RaisedButton(
          child: Text("Update Message"),
          onPressed: () {
            setState(() {
              key.currentState.updateMessage("new message");
            });
          },
        ),
      ],
    );
  }
```

**方法2：通过ValueNotifier通信**
ValueNotifier是一个包含单个值的变更通知器，当它的值改变的时候，会通知它的监听。

1. 定义ValueNotifierData类，继承ValueNotifier



```dart
class ValueNotifierData extends ValueNotifier<String> {
  ValueNotifierData(value) : super(value);
}
```

1. 定义`_WidgetOne`，包含一个ValueNotifierData的实例。



```dart
class _WidgetOne extends StatefulWidget {
  _WidgetOne({this.data});
  final ValueNotifierData data;
  @override
  _WidgetOneState createState() => _WidgetOneState();
}
```

1. `_WidgetOneState`中给ValueNotifierData实例添加监听。



```csharp
@override
initState() {
  super.initState();
  widget.data.addListener(_handleValueChanged);
  info = 'Initial mesage: ' + widget.data.value;
}

void _handleValueChanged() {
    setState(() {
      info = 'Message changed to: ' + widget.data.value;
    });
  }
```

1. 在`ValueNotifierCommunication`组件中实例化`_WidgetOne`，可以通过改变`ValueNotifierData`实例的value来触发`_WidgetOneState`的更新。



```csharp
@override
Widget build(BuildContext context) {
  ValueNotifierData vd = ValueNotifierData('Hello World');
  return Scaffold(
    appBar: AppBar(title: Text('Value Notifier Communication'),),
    body: _WidgetOne(data: vd),
    floatingActionButton: FloatingActionButton(child: Icon(Icons.refresh),onPressed: () {
      vd.value = 'Yes';
    }),
  );
}
```



作者：Tsun424
链接：https://www.jianshu.com/p/4a961887a2cd
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。