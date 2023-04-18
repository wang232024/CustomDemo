<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:dither="false|true"             //将在位图的像素配置与屏幕不同时（例如：ARGB 8888 位图和 RGB 565 屏幕）启用位图的抖动；值为“false”时则停用抖动。默认值为 true。
    android:shape="rectangle|line|oval|ring"//分别为矩形、线、椭圆、环。默认为矩形rectangle
    android:innerRadius="integer"           // shape为ring时可用，内环半径
    android:innerRadiusRatio="float"        // shape为ring时可用，内环的厚度比，即环的宽度比表示内环半径，默认为3，可被innerRadius值覆盖
    android:thickness="integer"             // shape为ring时可用，环的厚度
    android:thicknessRatio="float"          // shape为ring时可用，环的厚度比，即环的宽度比表示环的厚度，默认为9，可被thickness值覆盖
    android:tint="color"                    // 给shape着色
    android:tintMode="src_in|src_atop|src_over|add|multiply|screen" // 着色类型
    android:useLevel="false|true"           // 较少用，一般设为false，否则图形不显示。为true时可在LevelListDrawable使用
    android:visible="false|true"
    >
    <!-- 圆角 -->
    <corners
        android:radius="integer"            // 圆角半径，该值设置时下面四个属性失效
        android:bottomLeftRadius="integer"  // 左下角圆角半径
        android:bottomRightRadius="integer" // 右下角圆角半径
        android:topLeftRadius="integer"     // 左上角圆角半径
        android:topRightRadius="integer"    // 右上角圆角半径
    />
    <!-- 渐变 -->
    <gradient
        android:useLevel="false|true"       // 与上面shape中该属性的一致
        android:type="linear|radial|sweep"  // 渐变类型，分别为线性、放射性、扫描性渐变，默认为线性渐变linear
        android:angle="integer"             // 渐变角度，当上面type为线性渐变linear时有效。角度为45的倍数，0度时从左往右渐变，角度方向逆时针
        android:centerColor="color"         // 渐变中间位置颜色
        android:startColor="color"          // 渐变开始位置颜色
        android:endColor="color"            // 渐变结束位置颜色
        android:centerX="float"             // type为放射性渐变radial时有效，设置渐变中心的X坐标，取值区间[0,1]，默认为0.5，即中心位置
        android:centerY="float"             // type为放射性渐变radial时有效，设置渐变中心的Y坐标，取值区间[0,1]，默认为0.5，即中心位置
        android:gradientRadius="integer"    // type为放射性渐变radial时有效，渐变的半径
    />
    <!-- 内边距 须直接设置在item中 -->
    <padding
        android:bottom="integer"  // 设置底部边距
        android:left="integer"    // 左边边距
        android:right="integer"   // 右边
        android:top="integer"     // 顶部
    />
    <!-- 大小 -->
    <size
        android:height="integer"  // 宽度
        android:width="integer"   // 高度
    />
    <!-- 填充 -->
    <solid
        android:color="color"     // shape的填充色
    />
    <!-- 描边 -->
    <stroke
        android:color="color"       // 描边的颜色
        android:width="integer"     // 描边的宽度
        android:dashGap="integer"   // 虚线间隔
        android:dashWidth="integer" // 虚线宽度
    />
</shape>

### shape
android:shape="rectangle"       //设置形状，这里我设置的是矩形
android:shape="oval"            //设置形状，设置的是椭圆形
android:shape="line"            //设置形状，设置的是线形
android:shape="ring"            //设置形状，设置的是环形

1. rectangle
    solid: 设置形状填充的颜色，只有android:color一个属性
        android:color 填充的颜色
    
    padding: 设置内容与形状边界的内间距，可分别设置左右上下的距离
        android:left 左内间距
        android:right 右内间距
        android:top 上内间距
        android:bottom 下内间距
    
    gradient: 设置形状的渐变颜色，可以是线性渐变、辐射渐变、扫描性渐变
        android:type 渐变的类型
            linear 线性渐变，默认的渐变类型
            radial 放射渐变，设置该项时，android:gradientRadius也必须设置
            sweep 扫描性渐变
        android:startColor 渐变开始的颜色
        android:endColor 渐变结束的颜色
        android:centerColor 渐变中间的颜色
        android:angle 渐变的角度，线性渐变时才有效，必须是45的倍数，0表示从左到右，90表示从下到上
        android:centerX 渐变中心的相对X坐标，放射渐变时才有效，在0.0到1.0之间，默认为0.5，表示在正中间
        android:centerY 渐变中心的相对X坐标，放射渐变时才有效，在0.0到1.0之间，默认为0.5，表示在正中间
        android:gradientRadius 渐变的半径，只有渐变类型为radial时才使用
        android:useLevel 如果为true，则可在LevelListDrawable中使用
    corners: 设置圆角，只适用于rectangle类型，可分别设置四个角不同半径的圆角，当设置的圆角半径很大时，比如200dp，就可变成弧形边了
        android:radius 圆角半径，会被下面每个特定的圆角属性重写
        android:topLeftRadius 左上角的半径
        android:topRightRadius 右上角的半径
        android:bottomLeftRadius 左下角的半径
        android:bottomRightRadius 右下角的半径
    stroke: 设置描边，可描成实线或虚线。
        android:color 描边的颜色
        android:width 描边的宽度
        android:dashWidth 设置虚线时的横线长度
        android:dashGap 设置虚线时的横线之间的距离

2. oval
    size: 设置形状默认的大小，可设置宽度和高度
        android:width 宽度    android:height 高度
     其他属性也是存在的。这里就不列举了上边也都有写到了。如果使用TextView之类的设置椭圆，size默认会是View的宽度和高度。

3. line
通过Shape我们还可以设置分割线。不过一般我使用线都是直接View的。通过设置stroke我们可以设置线的样式（颜色、虚线还是实线等）。
    只能画水平线，画不了竖线；
    线的高度是通过stroke的android:width属性设置的；
    size的android:height属性定义的是整个形状区域的高度；
    size的height必须大于stroke的width，否则，线无法显示；
    线在整个形状区域中是居中显示的；
    线左右两边会留有空白间距，线越粗，空白越大；
    引用虚线的view需要添加属性android:layerType，值设为"software"，否则显示不了虚线。

4. ring
   首先，shape根元素有些属性只适用于ring类型，先过目下这些属性吧：
   android:innerRadius  内环的半径
   android:innerRadiusRatio  浮点型，以环的宽度比率来表示内环的半径，默认为3，表示内环半径为环的宽度除以3，该值会被android:innerRadius覆盖
   android:thickness  环的厚度
   android:thicknessRatio  浮点型，以环的宽度比率来表示环的厚度，默认为9，表示环的厚度为环的宽度除以9，该值会被android:thickness覆盖
   android:useLevel  一般为false，否则可能环形无法显示，只有作为LevelListDrawable使用时才设为true
   
### 常用小设置
按键去除阴影
android:stateListAnimator="@null"

防止熄屏
getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

seekbar设置滑块图片，去掉与进度条之间的间隔
android:splitTrack="false"
android:thumb="@drawable/ic_baseline_brightness_1_24"

CheckBox
android:button="@null"  // 不使用图标

<string name="login">登&#160;&#160;陆</string>
&#160;就代表着一个空格

// 删除视频文件
public static void deleteVideoFile(Context context, File file) {
ContentResolver resolver = context.getContentResolver();
Uri base = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
String where = MediaStore.Video.Media.DATA + " = \'" + file.getAbsolutePath() + "\'";
Log.i(TAG, "where:" + where);
resolver.delete(base, where, null);
}
// MediaStore.Audio.Media 音频
// MediaStore.Images.Media 图片
// MediaStore.Video.Media 视频



style中设置Actionbar的标题颜色
<item name="android:titleTextColor">@color/color_test</item>
更换后退图标
<item name="android:homeAsUpIndicator">@drawable/action_bar_back</item>

<item name="android:actionBarStyle">@style/FileListActionBar</item>

设置Actionbar
<style name="FileListActionBar" parent="android:Widget.DeviceDefault.ActionBar.Solid">
	<item name="android:titleTextStyle">@style/ListActionBarTitle</item>
	<item name="android:displayOptions">homeAsUp|showTitle</item>
</style>


ActionBar背景色
<item name="android:actionModeBackground">@color/action_mode_color_primary</item>

去除ActionBar阴影
//以下代码用于去除阴影
if(Build.VERSION.SDK_INT>=21){
getActionBar().setElevation(0);
}
<item name="android:actionBarStyle">@style/FileListActionBar</item>中设置背景色
<style name="FileListActionBar" parent="android:Widget.DeviceDefault.ActionBar.Solid">
    <item name="android:background">@color/recorderlist_bg</item>
</style>












设置ProgressBar圆角和颜色
<ProgressBar
style="?android:attr/progressBarStyleHorizontal"
android:progressDrawable="@drawable/playerlist_progress_drawable"
...
/>
drawable目录中添加
playerlist_progress_drawable.xml文件
```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@android:id/background"
        android:drawable="@drawable/shape_progressbar_bg" />
    <item android:id="@android:id/progress">
        <scale
            android:drawable="@drawable/shape_progressbar_progress"
            android:scaleWidth="100%" />
    </item>
</layer-list>
```
shape_progressbar_bg.xml文件
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <corners android:radius="10dp" />
    <solid android:color="#e2e2e2" />
</shape>

shape_progressbar_progress.xml文件
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <corners android:radius="10dp" />
    <solid android:color="#FF0075FF" />
</shape>