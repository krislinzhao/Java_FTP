# 项目介绍

本项目是一个基于C/S模式的文件上传系统，相当于一个简单的百度云盘。项目的开发分为客户端和服务端。客户端的工作主要包括图形界面，多线程的文件传输，消息通讯等。服务端五图形界面，主要包括消息通讯，文件的接受与发送，读写MySQL数据库。最终讲客户端和服务端的代码分别打包成两个jar包。其中服务端的项目我们是部署到百度的服务器上面了的，也就是项目的运行，文件的存储以及数据库的读写都是再远程服务器上面完成的。对于客户端来说，只需要拥有jre的环境，下载我们的客户端的jar包就可以连接我们的远程服务器来进行文件的传输了。

 # Client

![](https://cdn.jsdelivr.net/gh/krislinzhao/IMGcloud/img/20200427091646.png)

## FileClient

客户端的主类，jar包的入口类。主要功能是创建网络套接字，连接服务器，与服务器通信，渲染图形界面

## ClientThread

是监听服务器消息的类，她继承了Thread类，并再FileClient中被创建了对象，成为一个专门与服务器通信的线程。

## HandelFileThread

根据参数的不同来处理文件的上传与下载，本身实现了Runnable类，实现了里面的run方法。在FileClient中被创建，专门用于文件上传下载文件的线程。

Share：里面有大两的常量与整个Client包都需要用到的成员变量，将这些都需要用到的量专门存放到这个类中，并定义成static的，方便其他类调用。

 # Server

![](https://cdn.jsdelivr.net/gh/krislinzhao/IMGcloud/img/20200427091912.png)



 ## Server包

 ### 1.FileServer

 整个server的入口主类，创建了与客户端通信与文件传输的套接字，来一个客户端连接就创建一个文件处理的线程，

### 2.ServerThread

​    接受了服务器用来通信的套接字，根据接受的从客户端传过来的消息来创建不同的文件处理对象，来处理客户端的请求。如果是客户端要求上传或者是下载文件，则只需要根据对应的需求创建对应的HandleFile类来处理，如果是删除文件需要再本类中删除对应目录的文件，再删除对应数据库表中的项。

### 3.HandleFile

 用于处理客户端的文件请求，根据被创建时传进来的模式参数不同来决定是发送文件还是接收文件。下载文件的话就直接下载到对应的目录下面。如果是上传文件则还需要根据文件的类型来实例化成不同的javabean对象

### 4.Share

与client中的Share一样，都是讲整个包常用到的常量和变量定义成了此类下的静态成员，方便其他类的调用  

 ## pojo包

pojo:包与java web中的pojo一样，里面都是数据库的不同表的对象。一张表对应一个pojo下的类。类的对象也相当于是javabean对象

### 1.Image

数据库图片表的类，包含id, FileName,image,time的属性。类中有对应的set和get方法来操作对像。

### 2.Text

与Image相似。

### 3.video

与Image相似。

 

## Dao包

Dao包与java web中的dao层相同，都是用来访问数据库的。他下面关于增加和删除文件的两个接口，并有对应的实现类。

### 1.IAddFileDao:

 增加数据库表项的接口，每张数据库表都有一个对应的add方法，来增加数据库对应表的类容。

### 2.IDeleteFileDao

​    删除数据库表项的接口，有删除不同文件和excel文件两个方法 

### 3.AddFileDaoimpl:

 实现了AddFileDao的各种add方法。

### 4.DeleteFileDaoimpl

实现了DeleteFileDao的各种delete方法。

 ## Util包

### 1.ConnectDB:

连接数据库的类，里面加载了jdbc，指定了连接的数据库服务器

### 2.ReadExcel

用来读取excel文件的类，可以根据不同版本的excel文件来生成不同的对象，并且对外提供读取excel对象的方法

### 3.CreateSQL

 依赖于ReadExcel类，用ReadExcel读取excel文件得到对象后，来动态的拼接sql语句，动态的创建数据库中的excel表

### 4.ParseDataUtil

 用于处理从客户端发过来的的消息，提取出文件的信息(文件名，文件大小，文件数据流),并对外提供可操作文件对象的方法。

### 5.ParseTextTypeUtil

 读取不同类型的文本文件，并且判断后缀，创建访问不同文件的对象，通过相应的API提取出其内容写入数据库中。

 

# 功能介绍

## 1.客户端文件的上传与下载

 客户端在运行了client.jar后，可以同通过图形界面来上传本地文件到另一台电脑的服务器，并且可以将服务器的文件下载到本地。

## 2.文件内容写入数据库

​    当文件存到另一台电脑的服务器后，可以对文件进行解析，将不同的文件类型存入到不同的数据库表中。

## 3.删除文件

对于已经上传到服务器的文件，可以通过客户端的图形界面将其删除。删除指同时删除文件夹的文件和mysql数据库中的数据项

## 4.swing图形界面

​    客户运行了client.jar后，程序会展现成良好的图形界面来供用户操作，舍弃了麻烦，枯燥的命令行。

    ## 5.log日志

​    因为服务端是没有图形界面的，所以对于服务端执行的各种操作，我们可以用写log的方式来记录下来。

    ## 6.检查重复文件

​    当用户上传重复的文件是，系统会提示文件上传重复，

    ## 7.消息通知

​    当文件上传，下载，删除成功等操作是，系统会给传相应的消息提示。

    ## 8.两个设计模式

​    一是单例模式 对于Share类，我们只需要有一个对象即可，所以我们用了饿汉式的单例模式。  二是动态代理模式对于开启线程，我们不是直接让线程类去继承Thread并重写Thread的run(),而是先用一个类实现了Runnable接口，并将创建的对象传递给Thread类，通过调用Thread的start()来启动线程。

# 测试用例

## 1.上传Text类文件

![](https://cdn.jsdelivr.net/gh/krislinzhao/IMGcloud/img/20200427092817.png)

![](https://cdn.jsdelivr.net/gh/krislinzhao/IMGcloud/img/20200427092854.png)

 

## 2.上传excel文件

![](https://cdn.jsdelivr.net/gh/krislinzhao/IMGcloud/img/20200427093027.png)

 

![](https://cdn.jsdelivr.net/gh/krislinzhao/IMGcloud/img/20200427093056.png)

 

## 3.上传video文件

![](https://cdn.jsdelivr.net/gh/krislinzhao/IMGcloud/img/20200427093140.png)

 

![](https://cdn.jsdelivr.net/gh/krislinzhao/IMGcloud/img/20200427093223.png)

## 4.文件的删除

![](https://cdn.jsdelivr.net/gh/krislinzhao/IMGcloud/img/20200427093300.png)

![](https://cdn.jsdelivr.net/gh/krislinzhao/IMGcloud/img/20200427093409.png)

 