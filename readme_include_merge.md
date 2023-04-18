### include
将需要重用的布局写在一个单独的xml文件中，再使用include标签复用到其他布局中。 可在include标签中更改一些属性的值，比如重新设置id，改变布局属性（即android:layout_*属性)等。
若include标签中重新指定id，那么其中的控件就不可当成主xml（包含include标签的xml）中的控件来直接获得了，必须先获得include对应的xml文件（就是titlebar.xml），再通过布局文件的findViewById方法来获得其中控件。 当然，若原布局设置了id属性，会被覆盖掉。
```java
    View viewLayout = LayoutInflater.from(mContext).inflate(R.layout.xxx, null);
    View view = viewLayout.findViewById(R.id.yyy);
```
当需要在include标签中改变布局属性时，为了让其他属性生效，就必须重写android:layout_height和android:layout_width属性，否则任何针对layout调整都是无效的。
include有一个缺点就是可能会产生多余的层级，比如，被复用布局是一个垂直的LinearLayout布局，当以include标签插入到另一个垂直的LinearLayout布局中时，结果就是一个垂直的LinearLayout里包含一个垂直的LinearLayout，这个嵌套的布局并没有实际意义，只会让UI性能变差。这时就可以使用merge标签。

### merge
merge标签可以自动消除当一个布局插入到另一个布局时产生的多余的View Group，也可用于替换FrameLayout。用法就是直接使用merge标签标签作为复用布局的根节点。
merge不是view，也不是viewGroup，它只是声明一些视图等待被添加。
1.merge标签必须放到布局的根节点上
2.merge标签不是view，也不是viewGroup，它只是一个声明了一个视图等待被添加
3.当LayoutInflate加载view中使用的merge标签时，第二个参数不能为null，第三个参数设为true
LayoutInflater.from(context).inflate(R.layout.merge_layout_view, this, true);

当用include标签引用一个merge的layout时，include中的android:visibility等属性失效。原因?

### StubView
ViewStub的一些特点：
1. ViewStub只能Inflate一次，之后ViewStub对象会被置为空。按句话说，某个被ViewStub指定的布局被Inflate后，就不会够再通过ViewStub来控制它了。
2. ViewStub只能用来Inflate一个布局文件，而不是某个具体的View，当然也可以把View写在某个布局文件中。 
3. 替换时，布局文件的layout params是以ViewStub为准，其他布局属性是以布局文件自身为准。
适合的场景：
1. 在程序的运行期间，某个布局在Inflate后，就不会有变化，除非重新启动。
2. 想要控制显示与隐藏的是一个布局文件，而非某个View。

对ViewStub的inflate操作只能进行一次，因为inflate的 时候是将其指向的布局文件解析inflate并替换掉当前ViewStub本身（由此体现出了ViewStub“占位符”性质）。
一旦替换后，此时原来的 布局文件中就没有ViewStub控件了，因此，如果多次对ViewStub进行infalte，会出现错误信息：ViewStub must have a non-null ViewGroup viewParent。
