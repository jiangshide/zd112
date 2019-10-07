# Technology Stack
ID | Platform | Function |  Lnguage  | Build Status
 -------- | -------- | ------------ |  ------------ | ------------
 1  |   Android | [Framework](https://github.com/jiangshide/framework) | [Java](https://github.com/jiangshide/framework) [kotlin](https://github.com/jiangshide/kotlin_android) | [![Build Status](https://travis-ci.org/Bilibili/ci-ijk-ffmpeg-android.svg?branch=master)](https://github.com/jiangshide/framework)
 2  |   Ios | [Framework](https://github.com/jiangshide/ios) |	OC Swift	| [![Build Status](https://travis-ci.org/Bilibili/ci-ijk-ffmpeg-ios.svg?branch=master)](https://github.com/jiangshide/ios)
 3  |   Flutter | [Flutter](https://github.com/jiangshide/zd112_flutter) | [Dart](https://dart.dev/) [flutter](https://flutter.dev/) | [![Build Status](https://travis-ci.org/Bilibili/ci-ijk-ffmpeg-ios.svg?branch=master)](https://github.com/jiangshide/zd112_flutter)
 4  |   Web | [Backstage](https://github.com/jiangshide/backstage) | 	[Golang](https://github.com/jiangshide/backstage) [JS](https://github.com/jiangshide/backstage_js)	|	[![Build Status](https://travis-ci.org/Bilibili/ci-ijk-ffmpeg-ios.svg?branch=master)](https://github.com/jiangshide/backstage)
 5  |   Api | [Interface](https://github.com/jiangshide/zd112_api) |	Golang	| [![Build Status](https://travis-ci.org/Bilibili/ci-ijk-ffmpeg-ios.svg?branch=master)](https://github.com/jiangshide/zd112_api)
 6  |   Spark | [Analysis](https://github.com/jiangshide/analysis) |	Scala	| [![Build Status](https://travis-ci.org/Bilibili/ci-ijk-ffmpeg-ios.svg?branch=master)](https://github.com/jiangshide/analysis)
 7  |   Block Chain | [Identification](https://github.com/jiangshide/idendification) |	Golang	| [![Build Status](https://travis-ci.org/Bilibili/ci-ijk-ffmpeg-ios.svg?branch=master)](https://github.com/jiangshide/idendification) 
 8  |   Android | [移动客户端平台基础架构](https://github.com/jiangshide/zd112) |	[Java](https://github.com/jiangshide/zd112) Kotlin	| [![Build Status](https://travis-ci.org/Bilibili/ci-ijk-ffmpeg-ios.svg?branch=master)](https://github.com/jiangshide/idendification) 
 
# 组件化+插件化平台基础架构实现
Platfor |	Module | Status	|	Open Level
 -------- | ------------ |  ------------ |  ------------ 
 Android | 业务 | 	开发中	|	需定制
 Android | Host | 基础完成	|	低(可定制)
 Android | PluginLib |	 基础完成		|	低(可定制)
 Android | event |	开发中	|	需定制
 Android | skin |	开发中	|	需定制
 Android | widget |	基础完成	|	低(可定制)
 Android | ZdAnnotation |		基础完成	|	中(可定制)
 Android | ZdAnnotation_Compile | 	基础完成	|	低(可定制)
 Android | ZdRouter |	基础完成	|	中(可定制)
 Android | network |    基础完成    |    中(可定制)
 Android | Im |    定制开发    |    中(可定制)
 ... | ... |    ...    |    ...

##简单描述:
 [简书](https://www.jianshu.com/p/8cfdfaa6b636)
 [CSDN](https://blog.csdn.net/weixin_42863668/article/details/98622149)
    
## 项目目的
实现平台化基础支撑能力的基础上最大力度简化开发逻辑，提升开发效力,希望有兴趣的朋友可以一起加入完善,欢迎start
## 项目核心
插件化+组件化基础平台实现
## 项目架构
   ![Image](https://github.com/jiangshide/zd112/blob/master/imgs/app.svg)
## 核心架构
   ![Image](https://github.com/jiangshide/zd112/blob/master/imgs/component.svg)
### 1.插件化
#### 1.1 Host
主壳
#### 1.2 pluginlib
插件化基础支持 
### 2.模块化
#### 2.1 ZdRouter
为核心实现
#### 2.2 event
为核心实现
### 3.基础平台
#### 3.1 base
#### 3.2 utils
#### 3.3 network
#### 3.4 exception
#### 3.5 im
#### 3.6 jsbridge
#### 3.7 widget

### 4.相关基础知识
#### 4.1 android~framework~java
![Image](https://github.com/jiangshide/zd112/blob/master/imgs/activity.jpg)
##### 4.1.1 Activity启动流程(冷启动与热启动)
##### 4.1.2 ActivityManagerService管理流程
##### 4.1.3 WindowManagerService管理流程
##### 4.1.4 PackageManagerService管理流程

#### 4.2 android~framework~c

#### 4.3 flutter~framework~dart

#### 4.3 flutter~engine~c

### 5.第三方支持

# 服务宗旨:
### 一.面向中小企业及个人：
#### 1.提供专业的技术支持
#### 2.可做高度定制化需求

# 飞文吧
基于国际化文化交流小程序即将上线,欢迎关注
   ![Image](https://github.com/jiangshide/zd112/blob/master/imgs/zd112.png)

# 友情合作:备注(本人正在创业中,有兴趣的朋友可以一起加入,或者有对跨平台技术flutter有兴趣的朋友都可以关注以下本人微信加群便于技术交流)
   ![Image](https://raw.githubusercontent.com/jiangshide/framework/master/img/weixin.jpeg)
# 鼓励与支持:   
   ![Image](https://raw.githubusercontent.com/jiangshide/framework/master/img/play.png)
   
