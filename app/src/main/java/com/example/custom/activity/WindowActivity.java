package com.example.custom.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.custom.R;
import com.example.custom.view.CustomDialog;

public class WindowActivity extends Activity {
    private static final String TAG = "wtx_WindowActivity";
    private Context mContext = WindowActivity.this;
    private AlertDialog mAlertDialog;
    private Button btn_alertdialog;
    private Button btn_popupwindow;
    private PopupWindow mPopupWindow;
    private View mTipView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_window);

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        Log.i(TAG, "onCreate, widthPixels:" + displayMetrics.widthPixels);
        Log.i(TAG, "onCreate, heightPixels:" + displayMetrics.heightPixels);

        initView();
    }

    private void initView() {
        btn_alertdialog = findViewById(R.id.btn_alertdialog);
        btn_popupwindow = findViewById(R.id.btn_popupwindow);

        btn_alertdialog.setOnClickListener(mOnClickListener);
        btn_popupwindow.setOnClickListener(mOnClickListener);

        mTipView = findViewById(R.id.layout_tip);
        mTipView.setVisibility(View.GONE);

        Button btn = findViewById(android.R.id.list);
        btn.setOnClickListener(v -> {
            Log.i(TAG, "456");
        });
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (R.id.btn_alertdialog == v.getId()) {
//                createAlertDialog();
//                DiyDialog1();
//                customDialog();
//                createDialog();
//                mTipView.setVisibility(View.VISIBLE);

                createDialog();
            } else if (R.id.btn_popupwindow == v.getId()) {
                createPopupWindow();
            }
        }
    };

    /**
     * Popwindow 没有继承Viewgroup，因此最外层布局属性的宽高无效，如果布局设置wrap_content则以实际资源大小为准
     * 如果需要设置popupwindow所弹出框体的大小，需要在代码中进行设置：
     * popupWindow.setWidth((int) getResources().getDimension(R.dimen.tip_width));
     * popupWindow.setHeight((int) getResources().getDimension(R.dimen.tip_height));
     * popupWindow可能会报token is not valid的错误，这是由于popupWindow启动太快而token未拿到造成的，需要延时启动。
     */
    private void createPopupWindow() {
        float width = getResources().getDimension(R.dimen.alertdialog_width);
        float height = getResources().getDimension(R.dimen.alertdialog_height);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_sure_cancel, null);
        View rootview = LayoutInflater.from(mContext).inflate(R.layout.layout_window, null);
        Button btn_sure = contentView.findViewById(R.id.btn_sure);
        Button btn_cancel = contentView.findViewById(R.id.btn_cancel);

        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点。
        mPopupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置PopupWindow的背景
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应点击事件
        mPopupWindow.setTouchable(true);

        // 如果设置popupWindow的setFocusable(true)，那么设置setOutsideTouchable(false)是没有作用的，点击区域外依然会dismiss。
        // setFocusable和setOutsideTouchable为false，点击事件会传递到PopupWindow下的activity控件上，需在dispatchTouchEvent中进行拦截。
        mPopupWindow.setFocusable(false);       // 设置为true，点击PopupWindow之外会消失。
        // 设置PopupWindow是否能响应外部点击事件,
        mPopupWindow.setOutsideTouchable(false);

//        mPopupWindow.setClippingEnabled(false);

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                popupWindow.dismiss();
//                btn_sure.setBackgroundResource(R.drawable.shape_button_cancel_bg);
                Toast.makeText(mContext, "sure", Toast.LENGTH_SHORT).show();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                popupWindow.dismiss();
                Toast.makeText(mContext, "cancel", Toast.LENGTH_SHORT).show();
            }
        });

        // 只有同时设置PopupWindow的背景和可以响应外部点击事件，它才能“真正”响应外部点击事件。也就是说，当你点击PopupWindow的外部或者按下“Back”键时，PopupWindow才会消失。

        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
//        popupWindow.showAsDropDown(btn_popupwindow, 30, 100);
        //显示PopupWindow
        mPopupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(null != mPopupWindow && mPopupWindow.isShowing()){
            Log.w(TAG, "intercept mPopupWindow.");
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void createAlertDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_sure_cancel, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        Button btn_sure = view.findViewById(R.id.btn_sure);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAlertDialog.dismiss();
//                btn_sure.setBackgroundResource(R.drawable.shape_button_cancel_bg);
                Toast.makeText(mContext, "sure", Toast.LENGTH_SHORT).show();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                Toast.makeText(mContext, "cancel", Toast.LENGTH_SHORT).show();
                mTipView.setVisibility(View.GONE);
            }
        });

        mAlertDialog = builder.create();
        mAlertDialog.show();
        mAlertDialog.setCancelable(false);          // 点击窗体外不消失(默认会消失)

        Window window = mAlertDialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent); // 设置背景色透明

        WindowManager.LayoutParams p = window.getAttributes();  //获取对话框当前的参数值
        p.width = (int) getResources().getDimension(R.dimen.alertdialog_width);         // 宽高
        p.height = (int) getResources().getDimension(R.dimen.alertdialog_height);
        p.width = p.width * 2;
        p.height = p.height * 2;
        mAlertDialog.getWindow().setAttributes(p);
    }

    private void createDialog() {
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.layout_dialog);

        Window window = dialog.getWindow();
//        window.setBackgroundDrawableResource(android.R.color.transparent);

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        Log.i(TAG, "createDialog, widthPixels:" + displayMetrics.widthPixels);
        Log.i(TAG, "createDialog, heightPixels:" + displayMetrics.heightPixels);

        WindowManager.LayoutParams p = window.getAttributes();
//        p.width = 500;
//        p.height = 500;

        p.width = displayMetrics.widthPixels;
        p.height = displayMetrics.heightPixels;
        p.gravity = Gravity.TOP | Gravity.START;
        window.setAttributes(p);

        dialog.show();
    }

    private void DiyDialog1(){
        androidx.appcompat.app.AlertDialog.Builder alterDiaglog = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        alterDiaglog.setIcon(R.drawable.ic_launcher_background);//图标
        alterDiaglog.setTitle("简单的dialog");//文字
        alterDiaglog.setMessage("生存还是死亡");//提示消息
        //积极的选择
        alterDiaglog.setPositiveButton("生存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext,"点击了生存",Toast.LENGTH_SHORT).show();
            }
        });
        //消极的选择
        alterDiaglog.setNegativeButton("死亡", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext,"点击了死亡",Toast.LENGTH_SHORT).show();
            }
        });

        alterDiaglog.setNeutralButton("不生不死", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext,"点击了不生不死",Toast.LENGTH_SHORT).show();
            }
        });
        androidx.appcompat.app.AlertDialog dialog = alterDiaglog.create();

        //显示
        dialog.show();
        dialog.setCancelable(false);          // 点击窗体外不消失(默认会消失)

        //放在show()之后，不然有些属性是没有效果的，比如height和width
        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // 设置高度和宽度
        p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.6); // 宽度设置为屏幕的0.65
        p.gravity = Gravity.TOP;//设置位置
        p.alpha = 0.8f;//设置透明度
        dialogWindow.setAttributes(p);
    }

    private void customDialog() {
        int[] arr = new int[] {R.id.custom_dialog_btn1, R.id.custom_dialog_btn2};
        CustomDialog customDialog = new CustomDialog(mContext, R.layout.layout_customdialog, arr);
        customDialog.show();
        customDialog.setCancelable(false);          // 点击窗体外不消失(默认会消失)
    }

}
