
### Explicit Intent
Explicit Intent明确的指定了要启动的Acitivity，比如以下Java代码：
Intent intent= new Intent(this, B.class)；

### Implicit Intent
Implicit Intent没有明确的指定要启动哪个Activity，而是通过设置一些Intent Filter来让系统去筛选合适的Acitivity去启动。
intent到底发给哪个activity，需要进行三个匹配，一个是action，一个是category，一个是data。
1. 仅配置action
清单配置：
    <intent-filter>
    <action android:name="only_action" />
    <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
清单文件中需配置action和系统默认添加的<category android:name="android.intent.category.DEFAULT" />
启动代码：
   intent.setAction("only_action");
   startActivity(intent);
   
### 
仅有以下情况不需要加入<category android:name="android.intent.category.DEFAULT" />
即应用启动默认的第一个启动的activity
<intent-filter>
    <action android:name="android.intent.action.MAIN" />
    <category android:name="android.intent.category.LAUNCHER" />
</intent-filter>

两条原则：
    一条<intent-filter>元素至少应该包含一个<action>，否则任何Intent请求都不能和该<intent-filter>匹配。
    如果Intent请求的Action和<intent-filter>中个任意一条<action>匹配，那么该Intent就可以激活该activity(前提是除了action的其它项也要通过)。
两条注意：
    如果Intent请求或<intent-filter>中没有说明具体的Action类型，那么会出现下面两种情况。
    如果<intent-filter>中没有包含任何Action类型，那么无论什么Intent请求都无法和这条<intent-filter>匹配。
    反之，如果Intent请求中没有设定Action类型，那么只要<intent-filter>中包含有Action类型，这个Intent请求就将顺利地通过<intent-filter>的行为测试。???
测试：
    Intent请求中必须设定Action类型，否则报错。

一条<intent-filter>元素中可以包含多个action和多个category，此时每一个action均对应多个category。
