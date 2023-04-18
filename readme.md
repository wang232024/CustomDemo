### RecyclerViewActivity
包含RecyclerView常用用法
拖拽/分隔线
优点：按比例适配，可以适配不同分辨率的机器。
缺点：改动mSpanCouont的值时，须重新计算id_guideline_item_left等参考线的比例值。

保持屏幕常亮/监听熄屏重启关机

### WidgetActivity
自定义View
SimpleTextView

### WindowActivity
PopupWindow
AlertDialog

### EditTextActivity
在EditText中对span进行设置，未完善

### ImmersionActivity
沉浸式布局，及设置的图片等内容覆盖到状态栏

### EraserView
模仿擦出玻璃上水雾

### drawable
<layer-list>中包含不同的<item>属性，<item>相当于一个drawable属性文件。
<shape>中指定导角弧度，填充颜色，边框，渐变等属性。
<selector>中指定点击选中时不同的背景图片。

### string
<string name="login">登&#160;&#160;陆</string>
&#160；代表着一个空格

<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:src="@drawable/ic_adjust_black_24dp"
    app:fabSize="auto"
    app:backgroundTint="#EEDEC0"
    app:rippleColor="#FF0000"
    app:elevation="5dp"
    app:pressedTranslationZ="10dp"
    app:borderWidth="0dp"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
app:backgroundTint - 设置FAB的背景颜色。
app:rippleColor - 设置FAB点击时的背景颜色。
app:borderWidth - 该属性尤为重要。假设不设置0dp。那么在4.1的sdk上FAB会显示为正方形。
并且在5.0以后的sdk没有阴影效果。所以设置为app:borderWidth="0dp"
app:elevation - 默认状态下FAB的阴影大小。
app:pressedTranslationZ - 点击时候FAB的阴影大小。
app:fabSize - 设置FAB的大小，该属性有两个值，分别为normal和mini，相应的FAB大小分别为56dp和40dp。
src - 设置FAB的图标，Google建议符合Design设计的该图标大小为24dp。
app:layout_anchor - 设置FAB的锚点，即以哪个控件为參照点设置位置。
app:layout_anchorGravity - 设置FAB相对锚点的位置，值有 bottom、center、right、left、top等。

### 
AlertDialog中的布局最外层设置ConstraintLayout的宽高无效，推荐的设置方式：
1. 布局文件中最外层ConstraintLayout宽高设置为wrap_content。
2. 内部添加一个android:visibility="invisible"的View，宽高设置为固定值，即UI指定值。
3. 其他空间位置通过参考线百分比值进行确定。

SVG矢量图标下载
https://blog.csdn.net/weixin_54630384/article/details/125510749
https://www.ikonate.com/


EventBusDemo        类似于Handler的异步通讯机制
GlideDemo           图片加载框架
GreenDaoDemo        数据库操作
LeaksDemo           内存泄漏分析
RetrofitDemo        网络访问框架
RxjavaDemo          观察者模式？与Retrofit配合使用

### CustomDemo自定义demo
AidlDemo        AIDL通讯
AsynchronizedDataDealwithDemo   异步数据操作
AudioRecorderDemo   录音及数据拆分
BleDemo             低功耗蓝牙通讯
BluetoothChat       蓝牙Socket通讯
ContentProvider     数据共享
CustomUtilDemo      常用工具类
designpattern       几个常用设计模式
DimenTool           生成适应不同分辨率的dimens.xml文件
HttpServerDemo      Http服务器通讯
MakeLibSoDemo       动态库生成
MvpDemo             Mvp
NsdDemo             NSD通讯
TouchDemo           触摸，待完善
IntentServiceDemo	IntentService示例，startForegroundService调用5秒内须调用startForeground，否则抛出crash
PropertyAnimatorDemo	属性动画示例

### UI
CustomUiDemo        整合常用控件
CustomTextReader    Txt阅读
RecyclerViewDemo    标题栏跟随滑动变化
SelectDeleteRecyclerViewDemo    长按条目唤出多选栏
TabLayoutActivity   顶部和底部带tab的ViewPager
BaseDesignLayout    CollapsingToolbarLayout待整理

项目顶级build.gradle文件中指定了gradle插件的版本号
```shell
classpath 'com.android.tools.build:gradle:3.6.3'
```
gradle各个版本下载位置：
http://services.gradle.org/distributions/

| Gradle version | Android Plugin Version |
| :-: | :-: |
| 2.2.1-2.3 | 1.0.0-1.1.3 |
| 2.2.1-2.9 | 1.2.0-1.3.1 |
| 2.2.1+ | 1.5.0 |
| 2.10-2.13 | 2.0.0-2.1.2 |
| 2.14.1+ | 2.1.3+ |
| 3.3+ | 2.3.0+ |
| 4.1 | 2.3.3 |
| 4.4 | 3.1.3 |
| 5.1.1 | 3.4.1 |
| 5.6.4 | 3.6.3 |

可以通过修改gradle插件版本号来解决版本不一致的问题。

### 可能出现的问题
Caused by: org.gradle.api.internal.artifacts.ivyservice.DefaultLenientConfiguration$ArtifactResolveException: Could not resolve all artifacts for configuration ':classpath'.
构建项目时拉取不到gradle资源，可以将地址修改成阿里云的国内镜像或者换其他可用源
```shell
buildscript {
    repositories {
        jcenter()
        mavenLocal()
        google()
        maven { url 'https://maven.google.com' }
        maven{url 'http://maven.aliyun.com/nexus/content/groups/public/'}
        jcenter { url 'http://maven.aliyun.com/nexus/content/repositories/jcenter' }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.6.3'
    }
}

allprojects {
    repositories {
        jcenter()
        mavenLocal()
        google()
        maven { url 'https://maven.google.com' }
        maven{url 'http://maven.aliyun.com/nexus/content/groups/public/'}
        jcenter { url 'http://maven.aliyun.com/nexus/content/repositories/jcenter' }
    }
}
```

解压后放入sdk中，即和其中的extra文件夹合并。

1. 新建或者导入项目出现"building gradle project info"一直卡住的解决方法
   在以下目录中
   C:\Users\Administrator\.gradle\wrapper\dists\gradle-3.0-all\6v8c6qg2jpi8twyfv2a5s9mii
   出现.lck和.part后缀的文件。

查看所需gradle版本：打开C:\Users\用户名\.gradle\wrapper\dists\gradle-x.xx-all\xxxxxxxxxxxx，如果里面的gradle-xx-all.zip不完整（如0KB），则说明下载不成功，需要下载离线包放置到该目录下。

2. gradle版本与gradle plugin版本不一致。

项目中
gradle plugin版本路径查看   app\build.gradle中
gradle版本路径查看          app\gradle\wrapper\gradle-wrapper.properties(Service版本)

如：下载gradle-3.3-all.zip之后，此配置为service版本。推荐选择为service版本，默认的选择是本地版本。
放置到\.gradle\wrapper\dists\gradle-x.xx-all\xxxxxxxxxxxx目录下，然后在控制台Terminal执行gradlew。

配置gradle路径，此配置为配置本地版本。
gradle下载包下载后放置到AndroidStudio\gradle目录中，并解压。
Settings -> Build,Execution,Deployment -> Build Tools -> Gradle,选择Gradle home路径。

有时提示当前gradle plugin有问题，需要Settings -> Build,Execution,Deployment -> Instant Run中禁用Enable Instant Run to hot ...

3. Build -> clean project
   这个清除缓存可以防止大部分的问题。

4. Build -> rebuild project
   这个可以清理掉测试项配置与代码配置版本匹配问题。

5. File -> invalidate caches

6. 删除手机上的apk
   这个可以清除掉一些手机app遗留的问题，如数据库表字段不匹配等。

7. 日志过滤包含tag1,tag2,tag3的日志
   ^(?!.*(tag1|tag2|tag3)).*$








