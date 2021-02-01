- [Git submodule 子模块的管理和使用](#git-submodule-子模块的管理和使用)
  - [使用前提](#使用前提)
  - [添加子模块](#添加子模块)
  - [查看子模块](#查看子模块)
  - [更新子模块](#更新子模块)
  - [克隆包含子模块的项目](#克隆包含子模块的项目)
    - [克隆父项目，再更新子模块](#克隆父项目再更新子模块)
      - [克隆父项目](#克隆父项目)
      - [初始化子模块](#初始化子模块)
      - [更新子模块](#更新子模块-1)
    - [递归克隆整个项目](#递归克隆整个项目)
  - [修改子模块](#修改子模块)
  - [删除子模块](#删除子模块)
- [git](#git)
  - [git提交大文件](#git提交大文件)
    - [安装git-lfs](#安装git-lfs)
    - [把大文件的状态返回](#把大文件的状态返回)
    - [再次提交](#再次提交)
    - [push提交](#push提交)
  - [新建一个本地分支：](#新建一个本地分支)
  - [把新建的本地分支push到远程服务器，远程分支与本地分支同名（当然可以随意起名）](#把新建的本地分支push到远程服务器远程分支与本地分支同名当然可以随意起名)
  - [查看所有分支(包括远程的分支)](#查看所有分支包括远程的分支)
  - [删除远程分支](#删除远程分支)
  - [config 配置指令](#config-配置指令)
    - [查看系统config](#查看系统config)
    - [查看当前用户（global）配置](#查看当前用户global配置)
    - [查看当前仓库配置信息](#查看当前仓库配置信息)
    - [增](#增)
    - [删](#删)
    - [改](#改)
    - [查](#查)
  - [修改commit 的注释](#修改commit-的注释)
    - [修改最近一次](#修改最近一次)
    - [修改过去的](#修改过去的)
  - [指定的提交向其他的分支marge](#指定的提交向其他的分支marge)
  - [指定的多个commit向某个分支去合](#指定的多个commit向某个分支去合)
  - [恢复误删的分支](#恢复误删的分支)
  - [Xcode里Pod工程不自动提示解决方案](#xcode里pod工程不自动提示解决方案)
  - [iOS工程配置git忽略文件(.gitignore)](#ios工程配置git忽略文件gitignore)
    - [.gitignore无效解决办法](#gitignore无效解决办法)

# Git submodule 子模块的管理和使用
## 使用前提

经常碰到这种情况：当你在一个Git 项目上工作时，你需要在其中使用另外一个Git 项目。也许它是一个第三方开发的Git 库或者是你独立开发和并在多个父项目中使用的。这个情况下一个常见的问题产生了：你想将两个项目单独处理但是又需要在其中一个中使用另外一个。

在Git 中你可以用子模块submodule来管理这些项目，submodule允许你将一个Git 仓库当作另外一个Git 仓库的子目录。这允许你克隆另外一个仓库到你的项目中并且保持你的提交相对独立。

## 添加子模块

此文中统一将远程项目https://github.com/maonx/vimwiki-assets.git克隆到本地assets文件夹。

```sh
$ git submodule add https://github.com/maonx/vimwiki-assets.git assets
```
添加子模块后运行git status, 可以看到目录有增加1个文件.gitmodules, 这个文件用来保存子模块的信息。

```sh
$ git status
On branch master

Initial commit

Changes to be committed:
  (use "git rm --cached <file>..." to unstage)

    new file:   .gitmodules
    new file:   assets
```
## 查看子模块

```sh
$ git submodule
 e33f854d3f51f5ebd771a68da05ad0371a3c0570 assets (heads/master)
```
## 更新子模块

更新项目内子模块到最新版本
```sh
$ git submodule update
```
更新子模块为远程项目的最新版本
```sh
$ git submodule update --remote
```
## 克隆包含子模块的项目

克隆包含子模块的项目有二种方法：一种是先克隆父项目，再更新子模块；另一种是直接递归克隆整个项目。

### 克隆父项目，再更新子模块

#### 克隆父项目
```
$ git clone https://github.com/maonx/vimwiki-assets.git assets
```
查看子模块
```sh
$ git submodule
 -e33f854d3f51f5ebd771a68da05ad0371a3c0570 assets
```
子模块前面有一个-，说明子模块文件还未检入（空文件夹）。

#### 初始化子模块
$ git submodule init
```sh
Submodule 'assets' (https://github.com/maonx/vimwiki-assets.git) registered for path 'assets'
```
初始化模块只需在克隆父项目后运行一次。

#### 更新子模块
```sh
$ git submodule update
Cloning into 'assets'...
remote: Counting objects: 151, done.
remote: Compressing objects: 100% (80/80), done.
remote: Total 151 (delta 18), reused 0 (delta 0), pack-reused 70
Receiving objects: 100% (151/151), 1.34 MiB | 569.00 KiB/s, done.
Resolving deltas: 100% (36/36), done.
Checking connectivity... done.
Submodule path 'assets': checked out 'e33f854d3f51f5ebd771a68da05ad0371a3c0570'
```

### 递归克隆整个项目

```sh
git clone https://github.com/maonx/vimwiki-assets.git assets --recursive 
```
递归克隆整个项目，子模块已经同时更新了，一步到位。

## 修改子模块

在子模块中修改文件后，直接提交到远程项目分支。
```sh

$ git add .
$ git ci -m "commit"
$ git push origin HEAD:master
```
## 删除子模块

删除子模块比较麻烦，需要手动删除相关的文件，否则在添加子模块时有可能出现错误
同样以删除assets文件夹为例

删除子模块文件夹
```sh
$ git rm --cached assets
$ rm -rf assets
```
删除.gitmodules文件中相关子模块信息
```sh
[submodule "assets"]
  path = assets
  url = https://github.com/maonx/vimwiki-assets.git
```
删除.git/config中的相关子模块信息
```sh
[submodule "assets"]
  url = https://github.com/maonx/vimwiki-assets.git
```
删除.git文件夹中的相关子模块文件
```sh
$ rm -rf .git/modules/assets
```
# git

## git提交大文件

### 安装git-lfs

```shell
brew install git-lfs
```
### 把大文件的状态返回
```shell
$ cd {REPO}
$ git reset --soft HEAD^
```

### 再次提交
```shell
$ cd {REPO}
$ git lfs track {LARGE_FILE}            # {LARGE_FILE} を登録
Tracking {LARGE_FILE}
$ git add .gitattributes  
$ git add {LARGE_FILE}                  # 通常のadd
$ git commit -m 'I added {LARGE_FILE}.' # 通常のcommit
```

### push提交
``````

```
$ cd {REPO}
$ git push origin master
```

## 新建一个本地分支：

```shell
$ git checkout -b dbg_star
```

查看一下现在的分支状态:
```shell
$ git branch
* dbg_star
  master
  release
```
星号(*)表示当前所在分支。现在的状态是成功创建的新的分支并且已经切换到新分支上。


## 把新建的本地分支push到远程服务器，远程分支与本地分支同名（当然可以随意起名）

```
$ git push origin dbg_remote_star:dbg_star

```

## 查看所有分支(包括远程的分支)

使用git branch -a查看所有分支，会看到remotes/origin/dbg_lichen_star这个远程分支，说明新建远程分支成功。

## 删除远程分支

我比较喜欢的简单方式，推送一个空分支到远程分支，其实就相当于删除远程分支：
```shell
$ git push origin :dbg_lichen_star
```
也可以使用：
```
$ git push origin --delete dbg_lichen_star
```
这两种方式都可以删除指定的远程分支

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

### 增

```
git config --global --add configName configValue
```

### 删

```
git config  --global --unset configName   (只针对存在唯一值的情况)
```

### 改

```
git config --global configName configValue
```

### 查

```
git config --global configName
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

## 指定的提交向其他的分支marge

コミット62ecb3をブランチmasterにマージする

```shell
git checkout master
git cherry-pick 62ecb3

```



## 指定的多个commit向某个分支去合

例：commit76cada ~62ecb3 をmaster にマージする

取りあえず、commit62ecb3に基づいて新しいブランチを作ります。

```shell
git checkout -b newbranch 62ecb3
```

## 恢复误删的分支

```
// 获取所有的提交日志
git log -g   
或者
git log -a

//以commitid新建一个branch
git branch new_branch 499b281aa9ed8001cd17ded2fb04cda81ee5a6ae  // 新建分支名字(new_branch_name)

```

## Xcode里Pod工程不自动提示解决方案

File-->Workspace Settings -->Legacy Build System



## iOS工程配置git忽略文件(.gitignore) 

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

### .gitignore无效解决办法

>创建完以后发现未生效，因为.gitignore只能忽略那些原来没有被追踪的文件，如果某些文件已经被纳入了版本管理中，则修改.gitignore是无效的

gitignore规则不生效的解决办法，先把本地缓存删除然后更新

```
git rm -r --cached .
git add .
git commit -m 'update .gitignore'
```

