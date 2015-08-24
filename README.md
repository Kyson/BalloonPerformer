# BalloonPerformer [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-BalloonPerformer-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/2370)

## 预览

![https://raw.githubusercontent.com/Kyson/BalloonPerformer/master/art/bp_showcase.gif](https://raw.githubusercontent.com/Kyson/BalloonPerformer/master/art/bp_showcase.gif)


> 这个开源项目是从之前做的一个内存清理应用抽取出来的，下载地址：[点我下载TT内存清理](http://openbox.mobilem.360.cn/index/d/sid/2366842)

## 这个库是什么？

这是一个桌面悬浮窗工具，拉手下拉会出现气球动画。

## 用来做什么？

演示应用是一个小工具，用于清理内存，下拉拉手，等待气球飞行完毕就执行清理内存的工作。

你也可以在飞行完毕的回调中执行其他有趣的事情。

## 如何使用？

- Gradle配置

```
dependencies {
    compile 'com.tt:balloonperformerlibrary:1.0.2'
}
```

- 构造一个属性配置

```java
Config.Builder builder = new Config.Builder(MainActivity.this);
        Config config = builder.pullSensitivity(2.0f).lineLength(64).isOnlyDestop(false).flyDuration(3000).balloonCount(6).create();
```

BalloonPerformer可以配置一些属性，如下表

| 属性 |类型|说明|默认值|
|--- |--- |--- |--- |
|BalloonCount|int|气球个数|5|
|FlyDuration|long|飞行时间|2500毫秒|
|LineLength|int|拉线长度|72px|
|PullSensitivity|float|下拉灵敏度|1.8f|
|isOnlyDestop|boolean|是否仅在桌面显示|false|

- 初始化（携带该配置）

```java
BalloonPerformer.getInstance().init(MainActivity.this, config);
```

> 如果没有配置Config属性的话会使用默认属性。

- 显示悬浮窗

```java
BalloonPerformer.getInstance().show(MainActivity.this, new BalloonGroup.OnBalloonFlyedListener() {
            @Override
            public void onBalloonFlyed() {
                //do something
            }
        });
```

- 隐藏悬浮窗

```java
BalloonPerformer.getInstance().gone(MainActivity.this);
```

## 鸣谢

- [nineoldandroids](http://nineoldandroids.com/)

## 更多

- [我的个人博客](http://www.hikyson.cn)

- [Github](https://github.com/Kyson)

- [我的OSC](http://git.oschina.net/cocobaby)

- [我的新浪微博](http://weibo.com/1980495343/profile?rightmod=1&wvr=6&mod=personinfo)

## License

Copyright (c) 2015 Kyson

Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
