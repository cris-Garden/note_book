- [Mac开发技巧](#mac开发技巧)
  - [Mac brew 常用命令](#mac-brew-常用命令)
    - [安装多个go语言版本并切换](#安装多个go语言版本并切换)
    - [查看brew的帮助](#查看brew的帮助)
    - [安装软件](#安装软件)
    - [卸载软件](#卸载软件)
    - [搜索软件](#搜索软件)
    - [显示已经安装软件列表](#显示已经安装软件列表)
    - [更新软件，把所有的Formula目录更新，并且会对本机已经安装并有更新的软件用*标明。](#更新软件把所有的formula目录更新并且会对本机已经安装并有更新的软件用标明)
    - [更新某具体软件](#更新某具体软件)
    - [显示软件内容信息](#显示软件内容信息)
    - [用浏览器打开](#用浏览器打开)
    - [显示包依赖](#显示包依赖)
    - [显示包的依赖树](#显示包的依赖树)
    - [启动web服务器，可以通过浏览器访问http://localhost:4567/ 来同网页来管理包](#启动web服务器可以通过浏览器访问httplocalhost4567-来同网页来管理包)
    - [删除程序，和upgrade一样，单个软件删除和所有程序老版删除。](#删除程序和upgrade一样单个软件删除和所有程序老版删除)
    - [查看那些已安装的程序需要更新](#查看那些已安装的程序需要更新)
  - [查看端口占用](#查看端口占用)
  - [杀死进程](#杀死进程)
  - [打印目录的树形结构](#打印目录的树形结构)

# Mac开发技巧

## Mac brew 常用命令


### 安装多个go语言版本并切换
```python
$ brew install go 
$ brew install go@1.10
$ brew install go@1.9
$ brew install go@1.8
 
# 然后把 go@1.10/go@1.9/go@1.8安装目录下的文件移动或者复制到go目录下 ，通过 brew switch go [version]切换版本
# 切换版本语法 brew switch <formula> <version>
 
# 移动其他版本目录示例
MacBookPro:go mac$ pwd
/usr/local/Cellar/go
MacBookPro:go mac$ ls
1.10.3  1.11.2  1.11.4
MacBookPro:go mac$ cd ../go@1.10
MacBookPro:go@1.10 mac$ ls
1.10.7
MacBookPro:go@1.10 mac$ mv 1.10.7/ ../go/
 
# 把其他多个版本移动到go默认目录后，查看当前目录下有哪些go版本
MacBookPro:go mac$ ls
1.10.3  1.10.7  1.11.2  1.11.4  1.8.7   1.9.7
 
# 切换版本并查看 切换到go 1.9.7
MacBookPro:go mac$ brew switch go 1.9.7
Cleaning /usr/local/Cellar/go/1.9.7
Cleaning /usr/local/Cellar/go/1.10.3
Cleaning /usr/local/Cellar/go/1.11.2
Cleaning /usr/local/Cellar/go/1.11.4
Cleaning /usr/local/Cellar/go/1.10.7
Cleaning /usr/local/Cellar/go/1.8.7
3 links created for /usr/local/Cellar/go/1.9.7
MacBookPro:go mac$ go version
go version go1.9.7 darwin/amd64
 
# 切换版本并查看 切换到go 1.10.7
MacBookPro:go mac$ brew switch go 1.10.7
Cleaning /usr/local/Cellar/go/1.9.7
Cleaning /usr/local/Cellar/go/1.10.3
Cleaning /usr/local/Cellar/go/1.11.2
Cleaning /usr/local/Cellar/go/1.11.4
Cleaning /usr/local/Cellar/go/1.10.7
Cleaning /usr/local/Cellar/go/1.8.7
3 links created for /usr/local/Cellar/go/1.10.7
MacBookPro:go mac$ go version
go version go1.10.7 darwin/amd64
 
# 切换版本并查看 切换到go 1.8.7
MacBookPro:go mac$ brew switch go 1.8.7
Cleaning /usr/local/Cellar/go/1.9.7
Cleaning /usr/local/Cellar/go/1.10.3
Cleaning /usr/local/Cellar/go/1.11.2
Cleaning /usr/local/Cellar/go/1.11.4
Cleaning /usr/local/Cellar/go/1.10.7
Cleaning /usr/local/Cellar/go/1.8.7
3 links created for /usr/local/Cellar/go/1.8.7
MacBookPro:go mac$ go version
go version go1.8.7 darwin/amd64
```

### 查看brew的帮助
```
brew –help
```
### 安装软件
```
brew install git
```
### 卸载软件
```
brew uninstall git
```
### 搜索软件
```
brew search git
```
### 显示已经安装软件列表
```
brew list
```
### 更新软件，把所有的Formula目录更新，并且会对本机已经安装并有更新的软件用*标明。
```
brew update
```
### 更新某具体软件
```
brew upgrade git
```
### 显示软件内容信息
```
brew info git
```
### 用浏览器打开
```
brew home
```
### 显示包依赖
```
brew deps 
```
### 显示包的依赖树
```
brew deps --installed --tree
```
### 启动web服务器，可以通过浏览器访问http://localhost:4567/ 来同网页来管理包
```
brew server
```
### 删除程序，和upgrade一样，单个软件删除和所有程序老版删除。
```
brew cleanup git 
brew cleanup
```
### 查看那些已安装的程序需要更新
```
brew outdated
```

## 查看端口占用

```
lsof -i tcp:8080 
```

## 杀死进程

```
kill -9 pid
```

## 打印目录的树形结构

```S

├── build
├── config
├── docs
│   └── static
│       ├── css
│       └── js
├── src
│   ├── assets
│   ├── components
│   ├── store
│   │   └── modules
│   └── views
│       ├── book
│       └── movie
└── static
```
1. mac 下使用 brew包管理工具安装 tree

```
brew install tree
```

2. 安装成功后，直接在终端使用，使用 --help 查看帮助信息

```
tree --help
```
看到如下功能

```shell
tree --help                               10:06:14
usage: tree [-acdfghilnpqrstuvxACDFJQNSUX] [-H baseHREF] [-T title ]
    [-L level [-R]] [-P pattern] [-I pattern] [-o filename] [--version]
    [--help] [--inodes] [--device] [--noreport] [--nolinks] [--dirsfirst]
    [--charset charset] [--filelimit[=]#] [--si] [--timefmt[=]<f>]
    [--sort[=]<name>] [--matchdirs] [--ignore-case] [--] [<directory list>]
  ------- Listing options -------
  -a            All files are listed.
  -d            List directories only.
  -l            Follow symbolic links like directories.
  -f            Print the full path prefix for each file.
  -x            Stay on current filesystem only.
  -L level      Descend only level directories deep.
  -R            Rerun tree when max dir level reached.
  -P pattern    List only those files that match the pattern given.
  -I pattern    Do not list files that match the given pattern.
  --ignore-case Ignore case when pattern matching.
  --matchdirs   Include directory names in -P pattern matching.
  --noreport    Turn off file/directory count at end of tree listing.
  --charset X   Use charset X for terminal/HTML and indentation line output.
  --filelimit # Do not descend dirs with more than # files in them.
  --timefmt <f> Print and format time according to the format <f>.
  -o filename   Output to file instead of stdout.
  -------- File options ---------
  -q            Print non-printable characters as '?'.
  -N            Print non-printable characters as is.
  -Q            Quote filenames with double quotes.
  -p            Print the protections for each file.
  -u            Displays file owner or UID number.
  -g            Displays file group owner or GID number.
  -s            Print the size in bytes of each file.
  -h            Print the size in a more human readable way.
  --si          Like -h, but use in SI units (powers of 1000).
  -D            Print the date of last modification or (-c) status change.
  -F            Appends '/', '=', '*', '@', '|' or '>' as per ls -F.
  --inodes      Print inode number of each file.
  --device      Print device ID number to which each file belongs.
  ------- Sorting options -------
  -v            Sort files alphanumerically by version.
  -t            Sort files by last modification time.
  -c            Sort files by last status change time.
  -U            Leave files unsorted.
  -r            Reverse the order of the sort.
  --dirsfirst   List directories before files (-U disables).
  --sort X      Select sort: name,version,size,mtime,ctime.
  ------- Graphics options ------
  -i            Don't print indentation lines.
  -A            Print ANSI lines graphic indentation lines.
  -S            Print with CP437 (console) graphics indentation lines.
  -n            Turn colorization off always (-C overrides).
  -C            Turn colorization on always.
  ------- XML/HTML/JSON options -------
  -X            Prints out an XML representation of the tree.
  -J            Prints out an JSON representation of the tree.
  -H baseHREF   Prints out HTML format with baseHREF as top directory.
  -T string     Replace the default HTML title and H1 header with string.
  --nolinks     Turn off hyperlinks in HTML output.
  ---- Miscellaneous options ----
  --version     Print version and exit.
  --help        Print usage and this help message and exit.
  --            Options processing terminat
```

3. 输出你的树层目录结构

cd 目标文件夹路径
然后 tree 一下，会将该层级下所有文件都遍历了输出，不管层级多深

![11](image/mac1.png)

4. 常用技巧

我们可以在目录遍历时使用 -L 参数指定遍历层级
```
tree -L 2
```
如果你想把一个目录的结构树导出到文件 Readme.md ,可以这样操作
```
tree -L 2 >README.md //然后我们看下当前目录下的 README.md 文件
```
只显示文件夹；
```
tree -d 
```
显示项目的层级，n表示层级数。例：显示项目三层结构，tree -l 3；
```
tree -L n 
```
tree -I pattern 用于过滤不想要显示的文件或者文件夹。比如要过滤项目中的node_modules文件夹；
```
tree -I “node_modules”
```


