package com.example.animation;

import android.view.View;


/**
 * 属性动画可以操作任意的属性，前提是要满足两个条件,不然要不没效果，要不程序直接Crash
 * 1.被操作的属性必须要有get及set方法，即如果操作x属性，要提供getX()和setX()，如果不满足，程序直接Crash
 * 2.被操作的属性在进行属性动画改变时，必须能通过某种方式体现出来，例如可以引起UI的变化，如果不满足该条件，动画没有效果。
 *
 * width属性确实有提供getWidth()及setWidth()方法，getWidth()是View提供的方法，但是setWidth（）却不是View提供的，
 * 他只是TextView提供的一个方法,调用setWidth无法改变宽度，所以这里只满足了条件1导致动画效果不生效。
 *
 *     1. 给你要操作的对象属性加上get及set方法，如果有权限
 *     2. 通过新建一个包装类间接的为属性提供get及set方法
 *     3. 通过ValueAnimator，监听动画的过程，自己去实现属性的改变
 *
 *  此为方法2
 */
public class ViewWrapper {
    private View target;

    public ViewWrapper(View source) {
        this.target = source;
    }

    /**
     * 此处get/set方法对应width属性
     * @return
     */
    public int getCustomWidth() {
        return target.getLayoutParams().width;
    }

    public void setCustomWidth(int width) {
        target.getLayoutParams().width = width;
        target.requestLayout();
    }

}
