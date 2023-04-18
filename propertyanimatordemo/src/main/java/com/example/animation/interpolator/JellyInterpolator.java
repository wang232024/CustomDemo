package com.example.animation.interpolator;

import android.animation.TimeInterpolator;

/**
 * 可视化插值器的网站，其中内置了一些插值器公式，还可以查看动画演示效果。
 * http://inloop.github.io/interpolator/
 */
public class JellyInterpolator implements TimeInterpolator {
    // 因子数值越小振动频率越高
    private float factor;

    public JellyInterpolator() {
        this.factor = 0.15f;
    }

    /**
     * input参数就是与动画当前执行周期内执行时间相关的归一化变量，取值范围[0,1]
     * @param input
     * @return
     */
    @Override
    public float getInterpolation(float input) {
        return (float) (Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
    }

}
