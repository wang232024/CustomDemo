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
