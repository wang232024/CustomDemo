package com.example.custom.animator;

import android.animation.TimeInterpolator;

/**
 * 插值器默认AccelerateDecelerateInterpolator
 * 这个参数的意思是表示当前动画执行到的进度，取值范围是0~1，在动画刚刚开始时，值为0，在动画完成时，值为1。
 * 这个进度值时系统帮我们计算出的，它永远是匀速增加的，不受任何设置的影响。
 * 比如一秒内值从1变换成10，在0.5秒时，值应该为5，在0.8秒时，值应该为8，这就是匀速变化。
 * 我们可以参考系统计算出的当前动画的进度值，计算并返回另一个进度值，这个进度值将会影响到AnimatorUpdateListener中数值的取值，最终达到控制数值变化速度的作用。
 * LinearInterpolator是一个线性的插值器，因此它能使动画在执行过程中始终保持匀速执行。
 * AccelerateDecelerateInterpolator会使动画在开始和结束时变化缓慢，在中间部分变化较快。
 * 可以使用setInterpolator()方法给动画设置插值器。
 */
public class UniformSpeed implements TimeInterpolator {

    /**
     * return: 当前动画执行到的进度[0, 1]
     */
    @Override
    public float getInterpolation(float input) {
        //使动画在执行过程中始终保持匀速执行，因此它在getInterpolation方法中直接返回了参数input。
        return input;
    }
}
