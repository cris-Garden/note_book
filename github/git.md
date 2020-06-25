# Tips

## config 配置指令

```shell
git config
```

　　

config 配置有system级别 global（用户级别） 和local（当前仓库）三个 设置先从system-》global-》local  底层配置会覆盖顶层配置 分别使用--system/global/local 可以定位到配置文件

### 查看系统config

```shell
git config --system --list
```

　　

### 查看当前用户（global）配置

```shell
git config --global --list
```

 

### 查看当前仓库配置信息

```shell
git config --local --list
```

--global中存储了提交用户的email和用户名 如果需要手动设置则可以使用如下指令

```shell
git config --global user.name "myname"git config --global user.email "test@gmail.com"
```

##  修改commit 的注释

### 修改最近一次

```shell
git commit --amend
```

### 修改过去的

 git使用amend选项提供了最后一次commit的反悔。但是对于历史提交呢，就必须使用rebase了。

```shell
   git rebase -i HEAD~3

   表示要修改当前版本的倒数第三次状态。

    这个命令出来之后，会出来三行东东：

    pick:*******

    pick:*******

    pick:*******

    如果你要修改哪个，就把那行的pick改成edit，然后保存退出。

    这时通过git log你可以发现，git的最后一次提交已经变成你选的那个了，这时再使用：

    git commit --amend

    来对commit进行修改。
```

修改完了之后，要回来对不对？

```shell
   git rebase --continue
```

## 指しているコミットを他のブランチにマージ

コミット62ecb3をブランチmasterにマージする

```shell
git checkout master
git cherry-pick 62ecb3

```



## 指定のコミットグルプを他のブランチにマージ

例：commit76cada ~62ecb3 をmaster にマージする

取りあえず、commit62ecb3に基づいて新しいブランチを作ります。

```shell
git checkout -b newbranch 62ecb3
```

## Pod工程不提示

File-->Workspace Settings -->Legacy Build System



## iOS工程配置git忽略文件(.gitignore) .gitignore无效解决办法

.gitignore

```git
# Xcode
#
# gitignore contributors: remember to update Global/Xcode.gitignore, Objective-C.gitignore & Swift.gitignore

## Build generated
build/
DerivedData/

## Various settings
*.pbxuser
!default.pbxuser
*.mode1v3
!default.mode1v3
*.mode2v3
!default.mode2v3
*.perspectivev3
!default.perspectivev3
xcuserdata/
*.xcuserstate

## Other
*.moved-aside
*.xccheckout
*.xcscmblueprint

## Obj-C/Swift specific
*.hmap
*.ipa
*.dSYM.zip
*.dSYM

# CocoaPods
#
# We recommend against adding the Pods directory to your .gitignore. However
# you should judge for yourself, the pros and cons are mentioned at:
# https://guides.cocoapods.org/using/using-cocoapods.html#should-i-check-the-pods-directory-into-source-control
#
# Pods/
#
# Add this line if you want to avoid checking in source code from the Xcode workspace
# *.xcworkspace

# Carthage
#
# Add this line if you want to avoid checking in source code from Carthage dependencies.
# Carthage/Checkouts

Carthage/Build

# fastlane
#
# It is recommended to not store the screenshots in the git repo. Instead, use fastlane to re-generate the
# screenshots whenever they are needed.
# For more information about the recommended setup visit:
# https://docs.fastlane.tools/best-practices/source-control/#source-control

fastlane/report.xml
fastlane/Preview.html
fastlane/screenshots/**/*.png
fastlane/test_output

# Code Injection
#
# After new code Injection tools there's a generated folder /iOSInjectionProject
# https://github.com/johnno1962/injectionforxcode

iOSInjectionProject/
```

**到这里，就创建好了".gitignore"文件了！！**

**创建完以后发现未生效，因为.gitignore只能忽略那些原来没有被追踪的文件，如果某些文件已经被纳入了版本管理中，则修改.gitignore是无效的**

gitignore规则不生效的解决办法，先把本地缓存删除然后更新

```
git rm -r --cached .
git add .
git commit -m 'update .gitignore'
```

