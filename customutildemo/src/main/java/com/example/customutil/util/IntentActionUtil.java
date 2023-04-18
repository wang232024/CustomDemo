package com.example.customutil.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

public class IntentActionUtil {
    private static final String TAG = "wtx_IntentActionUtil";
    private static final int CODE_ACTION_GET_CONTENT = 1000;
    private static final int CODE_ACTION_GET_CONTENT1 = 1001;
    private static final int CODE_ACTION_GET_CONTENT2 = 1002;
    private static final int CODE_ACTION_GET_CONTENT3 = 1003;
    private static final int CODE_ACTION_GET_CONTENT4 = 1004;
    private static final int CODE_ACTION_GET_CONTENT5 = 1005;
    private static final int CODE_ACTION_GET_CONTENT6 = 1006;
    private void intent_test(Context context, int position) {
        try {
            Intent intent = null;
            Intent wrapperIntent = null;
            Uri uri = null;
            String VIDEO_UNSPECIFIED = "video/*";
            String IMAGE_UNSPECIFIED = "image/*";
            switch (position) {
                case 0:
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);   // 直接拨打电话
                    intent.setData(Uri.parse("tel:1320010001"));
                    context.startActivity(intent);
                    break;
                case 1:
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);   // 调出拨号盘，附号号码
                    intent.setData(Uri.parse("tel:1320010001"));
                    context.startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(Intent.ACTION_CALL_BUTTON); // 通话记录
                    context.startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
                    intent.setType("image/*"); // 查看类型，如果是其他类型，比如视频则替换成 video/*，或 */*
                    wrapperIntent = Intent.createChooser(intent, null);
                    ((Activity) context).startActivityForResult(wrapperIntent, CODE_ACTION_GET_CONTENT);
                    break;
                case 4:
                    uri = Uri.parse("http://www.google.com"); //浏览器
//                uri = Uri.parse("tel:1232333"); //拨号程序
//                uri = Uri.parse("geo:39.899533,116.036476"); //打开地图定位
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                    break;
                case 5:
                    //播放视频
                    String SOURCE = "/sdcard/1.mp4";
                    uri = Uri.fromFile(new File(SOURCE));
                    //调用系统自带的播放器
                    intent = new Intent(Intent.ACTION_VIEW);
                    Log.w(TAG, "--->" + uri.toString());
                    intent.setDataAndType(uri, "video/mp4");
                    context.startActivity(intent);
                    break;
                case 6:
                    //调用发送短信的程序
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.putExtra("sms_body", "信息内容...");
                    intent.setType("vnd.android-dir/mms-sms");
                    context.startActivity(intent);
                    break;
                case 7:
                    //发送短信息, 指定收件人
                    uri = Uri.parse("smsto:13200100001");
                    intent = new Intent(Intent.ACTION_SENDTO, uri);
                    intent.putExtra("sms_body", "信息内容...");
                    context.startActivity(intent);
                    break;
                case 8:
                    //发送彩信,设备会提示选择合适的程序发送
                    uri = Uri.parse("content://media/external/images/media/23");
                    //设备中的资源（图像或其他资源）
                    intent = new Intent(Intent.ACTION_SEND);// 调用分享功能
                    intent.putExtra("sms_body", "内容");
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setType("image/png");
                    context.startActivity(intent);
                    break;
                case 9:
                    //Email Intent
                    intent = new Intent(Intent.ACTION_SEND);
                    String[] tos = {"android1@163.com"};
                    String[] ccs = {"you@yahoo.com"};
                    intent.putExtra(Intent.EXTRA_EMAIL, tos);
                    intent.putExtra(Intent.EXTRA_CC, ccs);
                    intent.putExtra(Intent.EXTRA_TEXT, "The email body text");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");
                    intent.setType("message/rfc822");
                    context.startActivity(Intent.createChooser(intent, "Choose Email Client"));
                    break;
                case 10:
                    //选择图片 最近
                    intent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"
                    intent.setType(IMAGE_UNSPECIFIED);
                    wrapperIntent = Intent.createChooser(intent, null);
                    ((Activity) context).startActivityForResult(wrapperIntent, CODE_ACTION_GET_CONTENT1);
                    break;
                case 11:
                    //选择视频 最近
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType(VIDEO_UNSPECIFIED);
                    wrapperIntent = Intent.createChooser(intent, null);
                    ((Activity) context).startActivityForResult(wrapperIntent, CODE_ACTION_GET_CONTENT2);
                    break;
                case 12:
                    // 拍摄视频
//                int durationLimit = getVideoCaptureDurationLimit(); //SystemProperties.getInt("ro.media.enc.lprof.duration", 60);
//                int durationLimit = SystemProperties.getInt("ro.media.enc.lprof.duration", 60);
                    intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                    intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1000000000);
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 36000000);
                    ((Activity) context).startActivityForResult(intent, CODE_ACTION_GET_CONTENT3);
                    break;
                case 14:
                    // 调用系统录音
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                    String AUDIO_AMR = "audio/amr";
                    intent.setType(AUDIO_AMR);
                    intent.setClassName("com.android.soundrecorder",
                            "com.android.soundrecorder.SoundRecorder");
                    ((Activity) context).startActivityForResult(intent, CODE_ACTION_GET_CONTENT5);
                    break;
                case 15:
                    //拍照
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //"android.media.action.IMAGE_CAPTURE";
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse("content://mms/scrapSpace")); // output,Uri.parse("content://mms/scrapSpace");
                    ((Activity) context).startActivityForResult(intent, CODE_ACTION_GET_CONTENT6);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
