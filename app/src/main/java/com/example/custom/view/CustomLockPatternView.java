package com.example.custom.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.custom.R;

import java.util.ArrayList;
import java.util.List;

public class CustomLockPatternView extends View {
    private static final String TAG = "wtx_CustomLockPatternView";
    private Context mContext;
    // 所占宽和高
    private int mViewHeight;
    private int mViewWidth;
    // 声明屏幕上的宽和高的偏移量
    private int mViewHeightOffSet = 0;
    private int mViewWidthOffSet = 0;
    private Paint mPaintNormal;
    private Paint mPaintPress;
    private Paint mPaintError;
    // 九宫点的半径
    private float mPointRadius;
    // 连线的宽度
    private float mLineWidth;
    private LockPoint[][] mLockPoints = new LockPoint[3][3];
    // 创建记录经历九宫点的列表
    private List<LockPoint> mLockPointList = new ArrayList<>();
    // 判断手指是否离开屏幕
    private static boolean isTouchFinish = false;
    // 实例化鼠标点
    private LockPoint mMousePoint = new LockPoint();
    // 判断线的状态
    private static boolean isLineState = true;
    // 声明鼠标移动的x，y坐标
    private float mMoveX, mMoveY;
    // 判断手指点击屏幕时是否选中了九宫格中的点
    private static boolean isSelect = false;
    // 密码字符串最小长度
    private static final int PwdLenMin = 4;
    private OnLockListener mOnLockListener;

    public interface OnLockListener {
        void onPassword(String password);
        boolean isPasswordOk();
    }

    public void setOnLockListener(OnLockListener listener) {
        mOnLockListener = listener;
    }

    public CustomLockPatternView(Context context) {
        super(context);
        Log.i(TAG, "CustomLockPatternView1");
    }

    public CustomLockPatternView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLockPatternView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initResource();
    }

    private void initResource() {
        int point_normal = getResources().getColor(R.color.point_normal, null);
        int point_press = getResources().getColor(R.color.point_press, null);
        int point_error = getResources().getColor(R.color.point_error, null);

        mPaintNormal = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintNormal.setColor(point_normal);
        mPaintPress = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPress.setColor(point_press);
        mPaintError = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintError.setColor(point_error);
    }

    private void initLockPoints() {
        Log.i(TAG, "initPoint");
        // 获取View的宽高
        mViewWidth = getWidth();
        mViewHeight = getHeight();
        Log.i(TAG, "mViewWidth:" + mViewWidth);
        Log.i(TAG, "mViewHeight:" + mViewHeight);
        if (mViewHeight > mViewWidth) {
            // 获取y轴上的偏移量
            mViewHeightOffSet = (mViewHeight - mViewWidth) / 2;
            // 将屏幕高的变量设置成与宽相等，目的是为了new Point(x,y)时方便操作
            mViewHeight = mViewWidth;
        } else {
            // 获取x轴上的偏移量
            mViewWidthOffSet = (mViewWidth - mViewHeight) / 2;
            // 将屏幕宽的变量设置成与高相等，目的是为了new Point(x,y)时方便操作
            mViewWidth = mViewHeight;
        }

        mPointRadius = mViewWidth / 16.0f;
        mLineWidth = mPointRadius / 2;

        int width_1_4 = mViewWidth / 4;
        int width_2_4 = mViewWidth * 2 / 4;
        int width_3_4 = mViewWidth * 3 / 4;
        int height_1_4 = mViewHeight / 4;
        int height_2_4 = mViewHeight * 2 / 4;
        int height_3_4 = mViewHeight * 3 / 4;

        mLockPoints[0][0] = new LockPoint(mViewWidthOffSet + width_1_4, mViewHeightOffSet + height_1_4);
        mLockPoints[0][1] = new LockPoint(mViewWidthOffSet + width_2_4, mViewHeightOffSet + height_1_4);
        mLockPoints[0][2] = new LockPoint(mViewWidthOffSet + width_3_4, mViewHeightOffSet + height_1_4);
        mLockPoints[1][0] = new LockPoint(mViewWidthOffSet + width_1_4, mViewHeightOffSet + height_2_4);
        mLockPoints[1][1] = new LockPoint(mViewWidthOffSet + width_2_4, mViewHeightOffSet + height_2_4);
        mLockPoints[1][2] = new LockPoint(mViewWidthOffSet + width_3_4, mViewHeightOffSet + height_2_4);
        mLockPoints[2][0] = new LockPoint(mViewWidthOffSet + width_1_4, mViewHeightOffSet + height_3_4);
        mLockPoints[2][1] = new LockPoint(mViewWidthOffSet + width_2_4, mViewHeightOffSet + height_3_4);
        mLockPoints[2][2] = new LockPoint(mViewWidthOffSet + width_3_4, mViewHeightOffSet + height_3_4);

        // 设置九宫格中的各个index
        int index = 1;
        for (int i = 0; i < mLockPoints.length; i++) {
            for (int j = 0; j < mLockPoints[i].length; j++) {
                mLockPoints[i][j].setIndex(index + "");
                // 在没有任何操作的情况下默认点的状态
                mLockPoints[i][j].setState(LockPoint.STATE_NORMAL);
                index++;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasPoints(canvas);
        canvasLines(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, "onLayout:[" + left + ", " + top + ", " + right + ", " + bottom + "], " + changed);
        // onLayout中才能拿到getWidth和getHeight的值
        initLockPoints();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mMoveX = event.getX();
        mMoveY = event.getY();
        // 设置移动点的坐标
        mMousePoint.setX(mMoveX);
        mMousePoint.setY(mMoveY);
        LockPoint lockPoint = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isLineState = true;
                isTouchFinish = false;
                // 每次点击时就会将pointList中元素设置转化成正常状态
                setAllLockPointState(mLockPointList, LockPoint.STATE_NORMAL);
                // 将pointList中的元素清除掉
                mLockPointList.clear();
                // 判断是否点中了九宫格中的点
                lockPoint = getIsSelectedPoint(mMoveX, mMoveY);
                isSelect = null != lockPoint;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isSelect) {
                    lockPoint = getIsSelectedPoint(mMoveX, mMoveY);
                }
                break;
            case MotionEvent.ACTION_UP:
                isTouchFinish = true;
                isSelect = false;
                // 规定至少要有PwdLenMin个点被连线才有可能是正确
                if (PwdLenMin <= mLockPointList.size()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int j = 0; j < mLockPointList.size(); j++) {
                        stringBuilder.append(mLockPointList.get(j).getIndex());
                    }
                    if (null != mOnLockListener) {
                        mOnLockListener.onPassword(stringBuilder.toString());
                        if (mOnLockListener.isPasswordOk()) {
                            setAllLockPointState(mLockPointList, LockPoint.STATE_PRESS);
                        } else {
                            setAllLockPointState(mLockPointList, LockPoint.STATE_ERROR);
                            isLineState = false;
                        }
                    } else {
                        Log.e(TAG, "Please setOnLockListener to get passwordd's state.");
                    }
                } else if (1 < mLockPointList.size()) {
                    setAllLockPointState(mLockPointList, LockPoint.STATE_ERROR);
                    isLineState = false;
                // 如果只有一个点被点中时为正常情况
                } else if (1 == mLockPointList.size()) {
                    setAllLockPointState(mLockPointList, LockPoint.STATE_NORMAL);
                }
                break;

        }

        if (isSelect && null != lockPoint) {
            if (lockPoint.getState() == LockPoint.STATE_NORMAL) {   // 已经添加的点不再添加
                lockPoint.setState(LockPoint.STATE_PRESS);
                mLockPointList.add(lockPoint);
            }
        }

        // 每次发生OnTouchEvent()后都刷新View
        postInvalidate();
        return true;
    }

    private void setAllLockPointState(List<LockPoint> list, int state) {
        if (null != list) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setState(state);
            }
        } else {
            Log.e(TAG, "setAllLockPointState error, list null, state:" + state);
        }
    }

    private void canvasPoints(Canvas canvas) {
        for (int i = 0; i < mLockPoints.length; i++) {
            for (int j = 0; j < mLockPoints[i].length; j++) {
                if (mLockPoints[i][j].getState() == LockPoint.STATE_NORMAL) {
                    canvas.drawCircle(mLockPoints[i][j].getX(), mLockPoints[i][j].getY(), mPointRadius, mPaintNormal);
                } else if (mLockPoints[i][j].getState() == LockPoint.STATE_PRESS) {
                    canvas.drawCircle(mLockPoints[i][j].getX(), mLockPoints[i][j].getY(), mPointRadius, mPaintPress);
                } else {
                    canvas.drawCircle(mLockPoints[i][j].getX(), mLockPoints[i][j].getY(), mPointRadius, mPaintError);
                }
            }
        }
    }

    private void canvasLines(Canvas canvas) {
        if (mLockPointList.size() > 0) {
            LockPoint first = mLockPointList.get(0);
            LockPoint second;
            for (int i = 1; i < mLockPointList.size(); i++) {
                second = mLockPointList.get(i);
                canvasLine(first, second, canvas);
                first = second;
            }
            if (!isTouchFinish) {
                canvasLine(first, mMousePoint, canvas);
            }
        }
    }

    private void canvasLine(LockPoint a, LockPoint b, Canvas canvas) {
        // 计算连线的长度
        float abInstance = (float) Math.sqrt(
                (a.getX() - b.getX()) * (a.getX() - b.getX())
                + (a.getY() - b.getY()) * (a.getY() - b.getY())
        );

        canvas.rotate(getDegrees(a, b), a.getX(), a.getY());
        if (isLineState) {
            canvas.drawRect(a.getX(), a.getY() - mLineWidth / 2, a.getX() + abInstance, a.getY() + mLineWidth / 2, mPaintPress);
        } else {
            canvas.drawRect(a.getX(), a.getY() - mLineWidth / 2, a.getX() + abInstance, a.getY() + mLineWidth / 2, mPaintError);
        }
        canvas.rotate(-getDegrees(a, b), a.getX(), a.getY());
    }

    /**
     * 判断九宫格中的某个点是否被点中了，或者某个点能否被连线
     *
     * @param moveX
     * @param moveY
     * @return
     */
    private LockPoint getIsSelectedPoint(float moveX, float moveY) {
        LockPoint lockPoint = null;
        for (int i = 0; i < mLockPoints.length; i++) {
            for (int j = 0; j < mLockPoints[i].length; j++) {
                if (isReach(mLockPoints[i][j], moveX, moveY, mPointRadius)) {
                    lockPoint = mLockPoints[i][j];
                }
            }
        }
        return lockPoint;
    }

    /**
     * 判断屏幕上的九宫格中的点能否可以进行连线
     * @param a
     * @param moveX
     * @param moveY
     * @param radius 点bitmap的半径
     * @return 布尔型
     */
    public boolean isReach(LockPoint a, float moveX, float moveY, float radius) {
        float result = (float) Math.sqrt(
                (a.getX() - moveX) * (a.getX() - moveX)
                + (a.getY() - moveY) * (a.getY() - moveY)
        );
        if (result < radius * 1.25) {  // 靠近九宫点时有吸附效果
            return true;
        }
        return false;
    }

    /**
     * 计算从a到b的角度
     * @param a
     * @param b
     * @return
     */
    public static float getDegrees(LockPoint a, LockPoint b) {
        float degrees = 0;
        float ax = a.getX();
        float ay = a.getY();
        float bx = b.getX();
        float by = b.getY();

        if (ax == bx) {
            if (by > ay) {
                degrees = 90;
            } else {
                degrees = 270;
            }
        } else if (by == ay) {
            if (ax > bx) {
                degrees = 180;
            } else {
                degrees = 0;
            }
        } else {
            if (ax > bx) {
                if (ay > by) { // 第三象限
                    degrees = 180 + (float) (Math.atan2(ay - by, ax - bx) * 180 / Math.PI);
                } else { // 第二象限
                    degrees = 180 - (float) (Math.atan2(by - ay, ax - bx) * 180 / Math.PI);
                }
            } else {
                if (ay > by) { // 第四象限
                    degrees = 360 - (float) (Math.atan2(ay - by, bx - ax) * 180 / Math.PI);
                } else { // 第一象限
                    degrees = (float) (Math.atan2(by - ay, bx - ax) * 180 / Math.PI);
                }
            }
        }
        return degrees;
    }

    class LockPoint {
        public static final int STATE_NORMAL = 0; // 正常
        public static final int STATE_PRESS = 1;  // 按下
        public static final int STATE_ERROR = 2;  // 错误

        //九宫格中的点的下标（即每个点代表一个值）
        private String index;
        //点的状态
        private int state;
        //点的坐标
        private float x;
        private float y;

        public LockPoint() {
            super();
        }

        public LockPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public String getIndex() {
            return index;
        }

        public int getState() {
            return state;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void setX(float x) {
            this.x = x;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

}
