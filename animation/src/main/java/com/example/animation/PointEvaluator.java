package com.example.animation;

import android.animation.TypeEvaluator;

/**
 * Evaluator的作用就是将插值器返回的进度值转换成对应的数值
 */
public class PointEvaluator implements TypeEvaluator<Point> {
    private Point point = new Point();

    /**
     *
     * @param fraction      这个参数就是插值器返回的值，表示当前动画对应的值进度（0~1）
     * @param startValue    设置的值区间的开始值（例如ofFloat(0, 200)，则startValue为0）
     * @param endValue      设置的值区间的结束值（例如ofFloat(0, 200)，则endValue为200）
     * @return  返回值通过animation.getAnimatedValue();获取
     */
    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {
        point.setX((int) (startValue.getX() + fraction * (endValue.getX() - startValue.getX())));
        point.setY((int) (startValue.getY() + fraction * (endValue.getY() - startValue.getY())));
        return point;
    }
}
