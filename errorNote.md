- [安卓](#安卓)
	- [Manifest merger failed : Attribute application@appComponentFactory value=(android.support.v4.app.CoreComponentFactory) from [com.android.support:support-compat:28.0.0] AndroidManifest.xml:22:18-91](#manifest-merger-failed--attribute-applicationappcomponentfactory-valueandroidsupportv4appcorecomponentfactory-from-comandroidsupportsupport-compat2800-androidmanifestxml2218-91)
- [Flutter](#flutter)
	- [[!] The 'Pods-Runner' target has transitive dependencies that include static frameworks](#-the-pods-runner-target-has-transitive-dependencies-that-include-static-frameworks)
	- [Undefined symbols for architecture arm64:](#undefined-symbols-for-architecture-arm64)
	- [unexpected duplicate task: CopyPlistFile](#unexpected-duplicate-task-copyplistfile)
	- [App.framework does not support the minimum OS Version specified in the Info.plist](#appframework-does-not-support-the-minimum-os-version-specified-in-the-infoplist)
	- [Flutter 修改版本号不生效](#flutter-修改版本号不生效)
- [How to resolve Error: Not found: ‘package:](#how-to-resolve-error-not-found-package)
	- [Target of URI doesn't exist 'package:flutter/material.dart'](#target-of-uri-doesnt-exist-packagefluttermaterialdart)

# 安卓

## Manifest merger failed : Attribute application@appComponentFactory value=(android.support.v4.app.CoreComponentFactory) from [com.android.support:support-compat:28.0.0] AndroidManifest.xml:22:18-91
```shell
Attribute application@appComponentFactory value=(android.support.v4.app.CoreComponentFactory) from [com.android.support:support-compat:28.0.0] AndroidManifest.xml:22:18-91
	is also present at [androidx.core:core:1.1.0] AndroidManifest.xml:24:18-86 value=(androidx.core.app.CoreComponentFactory).
	Suggestion: add 'tools:replace="android:appComponentFactory"' to <application> element at AndroidManifest.xml:9:5-32:19 to override.
 
FAILURE: Build failed with an exception.
 
* What went wrong:
Execution failed for task ':app:processDebugManifest'.
> Manifest merger failed : Attribute application@appComponentFactory value=(android.support.v4.app.CoreComponentFactory) from [com.android.support:support-compat:28.0.0] AndroidManifest.xml:22:18-91
  	is also present at [androidx.core:core:1.1.0] AndroidManifest.xml:24:18-86 value=(androidx.core.app.CoreComponentFactory).
  	Suggestion: add 'tools:replace="android:appComponentFactory"' to <application> element at AndroidManifest.xml:9:5-32:19 to override.
```

在gradle.properties中添加如下代码即可

```properties
android.enableJetifier=true
android.useAndroidX=true
```

# Flutter

## [!] The 'Pods-Runner' target has transitive dependencies that include static frameworks

删除 `ios/Flutter/Flutter.framework `运行 `pod install`


##  Undefined symbols for architecture arm64:

```shell
      Undefined symbols for architecture arm64:
      "_OBJC_CLASS_$_GULSecureCoding", referenced from:
          objc-class-ref in FirebaseMessaging(FIRMessagingPubSub.o)
      "_GULLoggerRegisterVersion", referenced from:
          ___FIRLoggerInitializeASL_block_invoke in FirebaseCore(FIRLogger.o)
      "_GULLoggerInitializeASL", referenced from:
          ___FIRLoggerInitializeASL_block_invoke in FirebaseCore(FIRLogger.o)
```

删除 ‘ios/Runner.xcworkspace/xcshareddata/WorkspaceSettings.xcsettings’
或者 删除 ‘ios/Runner.xcworkspace/**/WorkspaceSettings.xcsettings’
即找到WorkspaceSettings.xcsettings删除即可

其他原因一般是路径问题flutter版本问题


## unexpected duplicate task: CopyPlistFile

![error](./image/error.png)
出现这种问题主要是因为出现了重复。两种解决方式：

一、File -> workspace settings，选择Legacy Build System。旧的build方式即可
![error](./image/error2.png)

二、如果不想改为旧的build方式的话，那就需要删除重复的文件路径。在Build Phases 里面进行搜索，并删除重复的。主要是copy bundle resource 里面出现了相同名称的文件。我是因为我这边构建了两个targets，而两个targets的copy bundle resource出现了相同的文件，两边删除一边即可，不过要根据项目情况而进行删除。


## App.framework does not support the minimum OS Version specified in the Info.plist

After doing a flutter clean, changing MinimumOSVersion (inside /ios/Flutter/AppframeworkInfo.plist) to 9.0, iOS Deployment Target (inside project runner) to 9.0 and iOS Deployment Target (inside target runner) to 9.0 the error disappeared.


## Flutter 修改版本号不生效

Flutter的App版本号设置在pubspec.yaml中，+号前面是版本名称，后面是版本号，在此修改会自动应用到Android和IOS项目对应版本号中，修改完安装发现并未生效，解决方法：

1、修改后执行flutter get

2、执行flutter clean

现在重新build 安装就能生效了

version: 1.0.0+1

先检查引用包是否放在 dependencies：下面

而不是dev_dependencies：

```
dependencies:
  flutter:
    sdk: flutter

dev_dependencies:
  flutter_test:
    sdk: flutter

  firebase_admob: ^0.9.3+4
  xpath: ^0.1.0
  loading_dialog: ^0.0.1
  package_info: ^0.4.3
  sqflite: ^1.1.7
  path_provider: ^1.4.0
  shared_preferences: ^0.5.8
  provider: ^4.1.2

```


# How to resolve Error: Not found: ‘package:
first run
```sh
flutter clean

```
if it does not work,you need to run
```
flutter pub cache repair

```
https://github.com/flutter/flutter/issues/17565

## Target of URI doesn't exist 'package:flutter/material.dart'

If you have used the 
```
flutter packages get
``` 
command and the error still persists, you can simply reload VS code the 
```
Developer: Reload Window command.
```
 Simply type that in after pressing Ctrl+Shift+P (Cmd+Shift+P for Mac users). It will clear the error. It's like refreshing VS Code.