在Flutter应用中，导航栏切换页面后默认情况下会丢失原页面状态，即每次进入页面时都会重新初始化状态，如果在`initState`中打印日志，会发现每次进入时都会输出，显然这样增加了额外的开销，并且带来了不好的用户体验。

在正文之前，先看一些常见的App导航，以喜马拉雅FM为例:



![0.gif](https://user-gold-cdn.xitu.io/2019/3/7/16957146e6eff51e?imageslim)



它拥有一个固定的底部导航以及首页的顶部导航，可以看到不管是点击底部导航切换页面还是在首页左右侧滑切换页面，之前的页面状态都是始终维持的，下面就具体介绍下如何在flutter中实现类似喜马拉雅的导航效果

#### 第一步：实现固定的底部导航

在通过`flutter create`生成的项目模板中,我们先简化一下代码，将`MyHomePage`提取到一个单独的`home.dart`文件，并在`Scaffold`脚手架中添加`bottomNavigationBar`底部导航，在`body`中展示当前选中的子页面。

```
/// home.dart
import 'package:flutter/material.dart';

import './pages/first_page.dart';
import './pages/second_page.dart';
import './pages/third_page.dart';

class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final items = [
    BottomNavigationBarItem(icon: Icon(Icons.home), title: Text('首页')),
    BottomNavigationBarItem(icon: Icon(Icons.music_video), title: Text('听')),
    BottomNavigationBarItem(icon: Icon(Icons.message), title: Text('消息'))
  ];

  final bodyList = [FirstPage(), SecondPage(), ThirdPage()];

  int currentIndex = 0;

  void onTap(int index) {
    setState(() {
      currentIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('demo'),
        ),
        bottomNavigationBar: BottomNavigationBar(
            items: items,
            currentIndex: currentIndex, 
            onTap: onTap
        ),
        body: bodyList[currentIndex]
    );
  }
}


复制代码
```

其中的三个子页面结构相同，均显示一个计数器和一个加号按钮，以`first_page.dart`为例：

```
/// first_page.dart
import 'package:flutter/material.dart';

class FirstPage extends StatefulWidget {
  @override
  _FirstPageState createState() => _FirstPageState();
}

class _FirstPageState extends State<FirstPage> {
  int count = 0;

  void add() {
    setState(() {
      count++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Center(
            child: Text('First: $count', style: TextStyle(fontSize: 30))
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: add,
          child: Icon(Icons.add),
        )
    );
  }
}

复制代码
```

当前效果如下：



![1.gif](data:image/svg+xml;utf8,<?xml%20version=%221.0%22?%3E%3Csvg%20xmlns=%22http://www.w3.org/2000/svg%22%20version=%221.1%22%20width=%22384%22%20height=%22700%22%3E%3C/svg%3E)



可以看到，从第二页切换回第一页时，第一页的状态已经丢失

#### 第二步：实现底部导航切换时保持原页面状态

可能有些小伙伴在搜索后会开始直接使用官方推荐的`AutomaticKeepAliveClientMixin`，通过在子页面的State类重写`wantKeepAlive`为`true`。 然而，如果你的代码和我上面的类似，body中并没有使用`PageView`或`TabBarView`，很不幸的告诉你，踩到坑了，这样是无效的，原因后面再详述。现在我们先来介绍另外两种方式：

*① 使用`IndexedStack`实现*

`IndexedStack`继承自`Stack`，它的作用是显示第`index`个`child`，其它`child`在页面上是不可见的，但所有`child`的状态都被保持，所以这个`Widget`可以实现我们的需求，我们只需要将现在的`body`用`IndexedStack`包裹一层即可

```
/// home.dart
class _MyHomePageState extends State<MyHomePage> {
  ...
  ...
  ...
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('demo'),
        ),
        bottomNavigationBar: BottomNavigationBar(
            items: items, currentIndex: currentIndex, onTap: onTap),
        // body: bodyList[currentIndex]
        body: IndexedStack(
          index: currentIndex,
          children: bodyList,
        ));
  }
复制代码
```

保存后再次测试一下



![2.gif](data:image/svg+xml;utf8,<?xml%20version=%221.0%22?%3E%3Csvg%20xmlns=%22http://www.w3.org/2000/svg%22%20version=%221.1%22%20width=%22388%22%20height=%22692%22%3E%3C/svg%3E)



*② 使用`Offstage`实现*

`Offstage`的作用十分简单，通过一个参数来控制`child`是否显示，所以我们同样可以组合使用`Offstage`来实现该需求，其实现原理与`IndexedStack`类似

```
/// home.dart
class _MyHomePageState extends State<MyHomePage> {
  ...
  ...
  ...
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('demo'),
        ),
        bottomNavigationBar: BottomNavigationBar(
            items: items, currentIndex: currentIndex, onTap: onTap),
        // body: bodyList[currentIndex],
        body: Stack(
          children: [
            Offstage(
              offstage: currentIndex != 0,
              child: bodyList[0],
            ),
            Offstage(
              offstage: currentIndex != 1,
              child: bodyList[1],
            ),
            Offstage(
              offstage: currentIndex != 2,
              child: bodyList[2],
            ),
          ],
        ));
  }
}
复制代码
```

在上面的两种方式中都可以实现保持原页面状态的需求，但这里有一些开销上的问题，有经验的小伙伴应该能发现当应用第一次加载的时候，所有子页状态都被实例化了（>这里的细节并不是因为我直接把子页实例化放在`bodyList`里...<），如果在子页`State`的`initState`中打印日志，可以在终端看到一次性输出了所有子页的日志。下面就介绍另一种通过继承`AutomaticKeepAliveClientMixin`的方式来更好的实现保持状态。

#### 第三步：实现首页的顶部导航

首先我们通过配合使用`TabBar`+`TabBarView`+`AutomaticKeepAliveClientMixin`来实现顶部导航（注意：`TabBar`和`TabBarView`需要提供`controller`，如果自己没有定义，则必须使用`DefaultTabController`包裹）。此处也可以选择使用`PageView`，后面会介绍。

我们先在`home.dart`文件移除`Scaffold`脚手架中的`appBar`顶部工具栏，然后开始重写首页`first_page.dart`:

```
/// first_page.dart
import 'package:flutter/material.dart';

import './recommend_page.dart';
import './vip_page.dart';
import './novel_page.dart';
import './live_page.dart';

class _TabData {
  final Widget tab;
  final Widget body;
  _TabData({this.tab, this.body});
}

final _tabDataList = <_TabData>[
  _TabData(tab: Text('推荐'), body: RecommendPage()),
  _TabData(tab: Text('VIP'), body: VipPage()),
  _TabData(tab: Text('小说'), body: NovelPage()),
  _TabData(tab: Text('直播'), body: LivePage())
];

class FirstPage extends StatefulWidget {
  @override
  _FirstPageState createState() => _FirstPageState();
}

class _FirstPageState extends State<FirstPage> {
  final tabBarList = _tabDataList.map((item) => item.tab).toList();
  final tabBarViewList = _tabDataList.map((item) => item.body).toList();

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
        length: tabBarList.length,
        child: Column(
          children: <Widget>[
            Container(
              width: double.infinity,
              height: 80,
              padding: EdgeInsets.fromLTRB(20, 24, 0, 0),
              alignment: Alignment.centerLeft,
              color: Colors.black,
              child: TabBar(
                  isScrollable: true,
                  indicatorColor: Colors.red,
                  indicatorSize: TabBarIndicatorSize.label,
                  unselectedLabelColor: Colors.white,
                  unselectedLabelStyle: TextStyle(fontSize: 18),
                  labelColor: Colors.red,
                  labelStyle: TextStyle(fontSize: 20),
                  tabs: tabBarList),
            ),
            Expanded(
                child: TabBarView(
              children: tabBarViewList,
              // physics: NeverScrollableScrollPhysics(), // 禁止滑动
            ))
          ],
        ));
  }
}

复制代码
```

其中推荐页、VIP页、小说页、直播页的结构仍和之前的首页结构相同，仅显示一个计数器和一个加号按钮，以推荐页`recommend_page.dart`为例：

```
/// recommend_page.dart
import 'package:flutter/material.dart';

class RecommendPage extends StatefulWidget {
  @override
  _RecommendPageState createState() => _RecommendPageState();
}

class _RecommendPageState extends State<RecommendPage> {
  int count = 0;

  void add() {
    setState(() {
      count++;
    });
  }
  
  @override
  void initState() {
    super.initState();
    print('recommend initState');
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body:Center(
          child: Text('首页推荐: $count', style: TextStyle(fontSize: 30))
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: add,
          child: Icon(Icons.add),
        ));
  }
}

复制代码
```

保存后测试，

![3.gif](https://user-gold-cdn.xitu.io/2019/3/7/1695707dbf27e236?imageslim)

可以看到，现在添加了首页顶部导航，且默认支持左右侧滑，接下来再进一步的完善状态保持



#### 第四步：实现首页顶部导航切换时保持原页面状态

*③ 使用`AutomaticKeepAliveClientMixin`实现*

写到这里已经很简单了，我们只需要在首页导航内需要保持页面状态的子页`State`中，继承`AutomaticKeepAliveClientMixin`并重写`wantKeepAlive`为`true`即可。

```
notes：Subclasses must implement wantKeepAlive, and their build methods must call super.build (the return value will always return null, and should be ignored)
```

以首页推荐`recommend_page.dart`为例：

```
/// recommend_page.dart
import 'package:flutter/material.dart';

class RecommendPage extends StatefulWidget {
  @override
  _RecommendPageState createState() => _RecommendPageState();
}

class _RecommendPageState extends State<RecommendPage>
    with AutomaticKeepAliveClientMixin {
  int count = 0;

  void add() {
    setState(() {
      count++;
    });
  }

  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    super.initState();
    print('recommend initState');
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);
    return Scaffold(
        body:Center(
          child: Text('首页推荐: $count', style: TextStyle(fontSize: 30))
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: add,
          child: Icon(Icons.add),
        ));
  }
}


复制代码
```

再次保存测试，

![4.gif](https://user-gold-cdn.xitu.io/2019/3/7/16957083bd41fcf8?imageslim)



现在已经可以看到，不管是切换底部导航还是切换首页顶部导航，所有的页面状态都可以被保持，并且在应用第一次加载时，终端只看到`recommend initState`的日志，第一次切换首页顶部导航至vip页面时，终端输出`vip initState`，当再次返回推荐页时，不再输出`recommend initState`。

所以，使用`TabBarView`+`AutomaticKeepAliveClientMixin`这种方式既实现了页面状态的保持，又具有类似惰性求值的功能，对于未使用的页面状态不会进行实例化，减小了应用初始化时的开销。

#### 更新

前面在底部导航介绍了使用`IndexedStack`和`Offstage`两种方式实现保持页面状态，但它们的缺点在于第一次加载时便实例化了所有的子页面`State`。为了进一步优化，下面我们使用`PageView`+`AutomaticKeepAliveClientMixin`重写之前的底部导航，其中`PageView`和`TabBarView`的实现原理类似，具体选择哪一个并没有强制要求。更新后的`home.dart`文件如下：

```
/// home.dart
import 'package:flutter/material.dart';

import './pages/first_page.dart';
import './pages/second_page.dart';
import './pages/third_page.dart';

class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final items = [
    BottomNavigationBarItem(icon: Icon(Icons.home), title: Text('首页')),
    BottomNavigationBarItem(icon: Icon(Icons.music_video), title: Text('听')),
    BottomNavigationBarItem(icon: Icon(Icons.message), title: Text('消息'))
  ];

  final bodyList = [FirstPage(), SecondPage(), ThirdPage()];

  final pageController = PageController();

  int currentIndex = 0;

  void onTap(int index) {
    pageController.jumpToPage(index);
  }

  void onPageChanged(int index) {
    setState(() {
      currentIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        bottomNavigationBar: BottomNavigationBar(
            items: items, currentIndex: currentIndex, onTap: onTap),
        // body: bodyList[currentIndex],
        body: PageView(
          controller: pageController,
          onPageChanged: onPageChanged,
          children: bodyList,
          physics: NeverScrollableScrollPhysics(), // 禁止滑动
        ));
  }
}

复制代码
```

然后在`bodyList`的子页`State`中继承`AutomaticKeepAliveClientMixin`并重写`wantKeepAlive`，以`second_page.dart`为例：

```
/// second_page.dart
import 'package:flutter/material.dart';

class SecondPage extends StatefulWidget {
  @override
  _SecondPageState createState() => _SecondPageState();
}

class _SecondPageState extends State<SecondPage>
    with AutomaticKeepAliveClientMixin {
  int count = 0;

  void add() {
    setState(() {
      count++;
    });
  }

  @override
  bool get wantKeepAlive => true;
  
  @override
  void initState() {
    super.initState();
    print('second initState');
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);
    return Scaffold(
        body: Center(
          child: Text('Second: $count', style: TextStyle(fontSize: 30))
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: add,
          child: Icon(Icons.add),
        ));
  }
}

复制代码
```

Ok，更新后保存运行，应用第一次加载时不会输出`second initState`，仅当第一次点击底部导航切换至该页时，该子页的`State`被实例化。

至此，如何实现一个类似的 底部 + 首页顶部导航 完结 ~


作者：Hfimy
链接：https://juejin.im/post/5c7e4a02e51d4541bd34b40e
来源：掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。