AnimationDrawable帧动画
帧动画主要用于以动画的形式播放一组图片。
如：图片存放在animationdrawable.xml文件中
<?xml version="1.0" encoding="utf-8"?>
<animation-list xmlns:android="http://schemas.android.com/apk/res/android"
    android:oneshot="true" >
    <item android:drawable="@drawable/ic_voiceplay_3" android:duration="200" />
    <item android:drawable="@drawable/ic_voiceplay_2" android:duration="200" />
    <item android:drawable="@drawable/ic_voiceplay_1" android:duration="200" />
    <item android:drawable="@drawable/ic_voiceplay_3" android:duration="200" />
</animation-list>

将ImageView控件的背景background设置为该文件。
// 这里一定要是getBackground，因为设置的属性就是这个。
AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
if (animationDrawable != null) {
	animationDrawable.start();
}

帧动画结束后停留在第一张的方法：
animationDrawable.selectDrawable(0);

------------------------------------------------------------------------------
mGestureProgress.setPivotX(mGestureProgress.getWidth() / 2);
mGestureProgress.setPivotY(mGestureProgress.getHeight() / 2);
ObjectAnimator animator = ObjectAnimator.ofFloat(
		mGestureProgress, "rotation", 0, 360);
animator.setDuration(2000);
animator.setRepeatMode(ValueAnimator.RESTART);
animator.setRepeatCount(-1);
if (!animator.isRunning()) {
	animator.start();
}
属性动画：绕着某点旋转。
