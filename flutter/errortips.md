- [[!] The 'Pods-Runner' target has transitive dependencies that include static frameworks](#-the-pods-runner-target-has-transitive-dependencies-that-include-static-frameworks)

# [!] The 'Pods-Runner' target has transitive dependencies that include static frameworks

删除 `ios/Flutter/Flutter.framework `运行 `pod install`


#  Undefined symbols for architecture arm64:

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
