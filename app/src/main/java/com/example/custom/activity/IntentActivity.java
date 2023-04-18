package com.example.custom.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.custom.R;

import java.util.ArrayList;
import java.util.List;

public class IntentActivity extends AppCompatActivity {
    private static final String TAG = "wtx_IntentActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initView();
        xmlStrings();
    }

    private void initData() {
        filterIntent();
    }

    private void initView() {
        setContentView(R.layout.layout_intentactivity);

        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        Button btn5 = findViewById(R.id.btn5);
        btn1.setOnClickListener(mOnClickListener);
        btn2.setOnClickListener(mOnClickListener);
        btn3.setOnClickListener(mOnClickListener);
        btn4.setOnClickListener(mOnClickListener);
        btn5.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn1:
                    myAction();
                    break;
                case R.id.btn2:
                    myCategory();
                    break;
                case R.id.btn3:
                    mulActionCategory();
                    break;
                case R.id.btn4:
                    sysAction(1);
                    break;
                case R.id.btn5:
                    customActionCategoryData();
                    break;
            }
        }
    };

    /**
     * 通过清单文件中intent-filter标签下的ACTION_MAIN和CATEGORY_SAMPLE_CODE进行过滤
     * 可查询到包含
     * <intent-filter>
     *     <action android:name="android.intent.action.MAIN" />
     *     <category android:name="android.intent.category.SAMPLE_CODE" />
     * </intent-filter>
     * 的标签，包括其他app中的。
     */
    private void filterIntent() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_SAMPLE_CODE);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

        int len = list.size();
        for (int i = 0; i < len; i++) {
            ResolveInfo resolveInfo = list.get(i);
            CharSequence labelSeq = resolveInfo.loadLabel(pm);
            Log.i(TAG, "i:" + i + ", labelSeq:" + labelSeq + ", toString:" + resolveInfo.toString());

        }
    }

    private void xmlStrings() {
        String[] strs = getResources().getStringArray(R.array.string_array_name);
        for (int i = 0; i < strs.length; i++) {
            Log.i(TAG, i + "--->" + strs[i]);
        }

        String result;
        String[] formats = getResources().getStringArray(R.array.clear_set);
        for (int i = 0; i < formats.length; i++) {
            Log.i(TAG, i + "===>" + formats[i]);
            result = String.format(formats[i], 100, 200, 300);
            Log.w(TAG, i + "===>" + result);
        }

        int count = 1;
        String songsFound = getResources().getQuantityString(R.plurals.numberOfSongsAvailable, count, count);
        Log.i(TAG, "songsFound:" + songsFound);

        int otherCount = (int)0.05;
        String otherSongsFound = getResources().getQuantityString(R.plurals.numberOfSongsAvailable, otherCount, otherCount);
        Log.i(TAG, "otherSongsFound:" + otherSongsFound);
    }

    /**
     * 仅指定action即可唤起Intent
     * 清单中至少包含"android.intent.category.DEFAULT"
     */
    private void myAction() {
        Intent intent = new Intent();
        intent.setAction("only_action");
        startActivity(intent);
    }

    /**
     * 使用自定义action和category时，至少指定一个action，
     * category可不添加或添加多个（无需包含清单中指定的所有category）
     * 对应的清单文件中的组件必须包含所有category（系统自动添加"android.intent.category.DEFAULT"，清单中须补上）
     */
    private void myCategory() {
        Intent intent = new Intent();
        intent.setAction("my_action1");
        intent.addCategory("my_category1");
        intent.addCategory("my_category2");
        startActivity(intent);
    }

    /**
     * "android.intent.action.SEARCH"有多个应用中的activity包含此动作
     * 系统弹出多个activity以供选择
     */
    private void mulActionCategory() {
        Intent intent = new Intent();
        // 该动作有多个activity包含
        intent.setAction("android.intent.action.SEARCH");
        startActivity(intent);
    }

    private void sysAction(int type) {
        Log.i(TAG, "sysAction, type:" + type);
        Intent intent;
        String[] tos = {"way.ping.li@gmail.com"};
        String[] ccs = {"way.ping.li@gmail.com"};
        String[] bccs = {"way.ping.li@gmail.com"};
        switch (type) {
            case 0:
                Intent data = new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:way.ping.li@gmail.com"));
                data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");
                data.putExtra(Intent.EXTRA_TEXT, "这是内容");
                startActivity(data);
                break;
            case 1:
                intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, tos);
                intent.putExtra(Intent.EXTRA_CC, ccs);
                intent.putExtra(Intent.EXTRA_BCC, bccs);
                intent.putExtra(Intent.EXTRA_TEXT, "body");
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");

//                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///mnt/sdcard/a.jpg"));
//                intent.setType("image/*");
//                intent.setType("message/rfc882");
//                Intent.createChooser(intent, "Choose Email Client");
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_EMAIL, tos);
                intent.putExtra(Intent.EXTRA_CC, ccs);
                intent.putExtra(Intent.EXTRA_TEXT, "body");
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");

                ArrayList<Uri> imageUris = new ArrayList<>();
                imageUris.add(Uri.parse("file:///mnt/sdcard/a.jpg"));
                imageUris.add(Uri.parse("file:///mnt/sdcard/b.jpg"));
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                intent.setType("image/*");
                intent.setType("message/rfc882");
                Intent.createChooser(intent, "Choose Email Client");
                startActivity(intent);
                break;
        }
    }

    /**
     * 清单文件中的data标签限定了uri字符串，如果清单中没有限定，则可以匹配所有。
     * 如果清单中没有设置data，则不可设置uri，否则报错。
     * 如果清单中已经设置data，则必须设置uri。
     *      data标签中scheme,host,path对应Uri字符串，如果data标签中没有限定host和path，则匹配所有
     *      data标签中已经设置mimeType属性，setDataAndType(, "text/plain");
     *              没有设置，setDataAndType(, null)
     *
     *  对应关系出错，则报"No Activity found to handle Intent"错误。
     */
    private void customActionCategoryData() {
        Intent intent = new Intent();

        intent.setAction("myaction2");
        intent.addCategory("mycategory1");
        intent.addCategory("mycategory2");
        intent.addCategory("mycategory3");
//        Uri uri = Uri.parse("my_scheme://my_host/goodsDetail?goodsId=10011002");
        Uri uri = Uri.parse("my_scheme://my_host/my_path?goodsId=123456789");
        intent.setDataAndType(uri, "*/*");
//        intent.setDataAndType(uri, null);

        startActivity(intent);
    }

}
