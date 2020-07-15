# Cmake

[TOC]

*由于个人在使用C++过程中，经常使用Cmake编译，因此写一篇文章，记录使用Cmake的过程~~*

## cmake编译过程

CMakeLists.txt相当于定义了一套生成makefile的规则，通过cmake，生成对应的makefile，再使用make进行程序编译。

### 单一模块编译

单一模块，该模块拥有唯一的一个CMakeLists.txt，其中包含了该模板的各种配置项。

编译的时候，进入根目录，创建一个存放编译后文件的目录，假设目录叫build，那么，进入build目录，执行

```
#..表示cmakelist.txt文件在上一级目录
cmake ..
```

执行完，会产生一系列的make信息，同样会产生makefile。

在build目录下，执行make 

等待编译完成即可，这个过程也许会有错误，因此按照提示来即可。

---

### 多模块编译

多模块编译，适合复用性强，项目庞大的。多模块编译，采用分而治之的思想，在一个项目目录下，有一个总的CMakeLists.txt，而每个模块中具有一个CMakeLists.txt，每个模块负责自己的编译，而root CMakeLists.txt负责对每个子模块的统一编译。

----

### 常见的统一目录结构

> src/include ---存放统一的头文件
>
> lib ---存放第三方库
>
> bin ---存放编译后的二进制文件
>
> src/* --存放c++源码
>
> ...

---



## cmake常用配置项

- 指定cmake的最低版本

```cmake
#高版本可能使用到编译的一些特性
cmake_minimum_required(VERSION 2.8.12.2)
```

- 指定项目名

```cmake
#项目名
project(projectName)
```

- 如果是可执行项目，编译希望能生成运行目标

```cmake
#appName是生成后的可执行文件名字
#后面的则是编译生成该执行文件所包含的源代码和头文件
add_executable(appName src/main.cpp)
```

- 源文件的优化包含，对于一两个文件当然无所谓，但是针对几十个文件，编写起来还是很有压力的

```cmake
#该命名用于优化
#dir是源文件目录，var是定义一个变量，变量的值是这些源文件
#必须注意，包含的只是源文件，并不包含头文件，如果使用这个命令来尝试包含所有文件，那么将会失败
aux_source_directory(<dir> <variable>)

add_executable(appName ${varName})
```

- 包含所有文件，追加到executable后的简易方式

```cmake
#GLOB <var> <dir>.h <dir>.cpp
#实现查找这些文件并保存到变量SRC中
FILE(GLOB SRC src/*.cpp src/*.h src/*/*.h src/*/*.cpp)
add_executable(epollsvr ${SRC})
```

- 比如要引入第三方库，则需要如下配置

```cmake
#设置头文件路径
set(INC_DIR ./include)
#设置链接库路径
set(LINK_DIR ./lib)
#引入头文件
include_directories(${INC_DIR})
#引入库文件
link_directories(${LINK_DIR})
#链接第三方库文件
target_link_libraries(AppName lib_acl_cpp.a lib_acl.a lib_protocol.a pthread gflags)
```

- 针对内置配置项进行配置

```cmake
#追加 pthread库
SET(CMAKE_CXX_FLAGS ${CMAKE_CXX_FLAGS} "-pthread")
```

- 子目录

```cmake
#要求子目录下面必须有cmakelists.txt自治编译
add_subdirectory(math)
```

而子目录通常会生成library，即生成的是链接库，所以子目录应该采用如下：

```cmake
# 生成链接库
set(DIR...)
add_library(libName ${DIR_LIB_SRCS})
```



## cmake内置变量

> ```cmake
> #项目根目录
> PROJECT_SOURCE_DIR
> #运行cmake的目录，一般是${PROJECT_SOURCE_DIR}/build
> PROJECT_BINARY_DIR
> #当前处理的CMakeLists.txt所在的路径
> CMAKE_CURRENT_SOURCE_DIR 
> #项目名
> PROJECT_NAME 
> ```

> 编译器相关
>
> ```cmake
> #设置 C 编译选项,也可以通过指令 ADD_DEFINITIONS()添加
> CMAKE_C_FLAGS
> #设置 C++编译选项,也可以通过指令 ADD_DEFINITIONS()添加
> CMAKE_CXX_FLAGS
> ```
>
> 编译类型相关（主要讲库编译）
>
> ```cmake
> ADD_LIBRARY(libname [SHARED | STATIC | MODULE] [EXCLUDE_FROM_ALL] SRC_LIST)
>         #生成动态库或静态库
>         #SHARED 动态库
>         #STATIC 静态库
>         #MODULE 在使用dyld的系统有效,若不支持dyld,等同于SHARED
>         #EXCLUDE_FROM_ALL 表示该库不会被默认构建
>  
>  #添加共享库搜索路径
>  LINK_DIRECTORIES 
>  #添加共享库链接
>  TARGET_LINK_LIBRARIES
>  # TARGET_LINK_LIBRARIES(target lib1 lib2 …)
> ```
>
> 子工程
>
> ```cmake
> ADD_SUBDIRECTORY(src_dir [binary_dir] [EXCLUDE_FROM_ALL])
> #向当前工程添加存放源文件的子目录,并可以指定中间二进制和目标二进制的存放位置
> #EXCLUDE_FROM_ALL含义：将这个目录从编译过程中排除
> ```
>
> 



## cmake useful

### support GDB

```cmake
set(CMAKE_BUILD_TYPE "Debug")
set(CMAKE_CXX_FLAGS_DEBUG "$ENV{CXXFLAGS} -O0 -Wall -g -ggdb")
set(CMAKE_CXX_FLAGS_RELEASE "$ENV{CXXFLAGS} -O3 -Wall")
```

如果出现core dump，调试core文件一直没显示详细的调试信息，是因为没有加入-g参数，可以尝试使用如下：

```cmake
#添加 -g 
add_definitions(-g)
```

