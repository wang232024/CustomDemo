package com.example.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import androidx.core.content.FileProvider;

/**
 * 测试 shareToApp(mContext, "/storage/emulated/0/Download/test.mp4");    // 公共目录下
 * "/storage/emulated/0/Android/data/com.example.customutil/msc/test.mp4"   // 外部存储，私有目录下, 通过getExternalFilesDir("msc").getAbsolutePath()获取
 * "/data/user/0/com.example.customutil/test.mp4"                       // 内部存储，私有目录下，通过mContext.getDataDir().getPath()获取
 */
public class ShareUtil {
    private static final String TAG = "wtx_ShareUtil";

    /**
     * 获取文件格式对应的ContentType
     * @param filePath  路径或文件名均可
     * @return
     */
    private static String getFileContentType(String filePath) {
        return URLConnection.getFileNameMap().getContentTypeFor(filePath);
    }

    /**
     * Android原生分享功能
     * 默认选取手机所有可以分享的APP
     * 仅添加标题和文字内容
     */
    public static void allShare(Context context){
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("text/plain");//设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_SUBJECT, "share");//添加分享内容标题
        share_intent.putExtra(Intent.EXTRA_TEXT, "share with you:"+"android");//添加分享内容
        share_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share_intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, "share");
        context.startActivity(share_intent);
    }

    /**
     * 通过蓝牙进行分享
     * @param context
     * @param filePath
     */
    public static void btShare(Context context, String filePath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(getFileContentType(filePath));
        intent.setPackage("com.android.bluetooth");
        intent.putExtra(Intent.EXTRA_STREAM, getUriForFile(context, new File(filePath)));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 分享单个文件
     * @param context
     * @param filePath 文件路径
     */
    public static void shareToApp(Context context, String filePath) {
        Log.i(TAG, "openFileThirdApp, filePath:" + filePath);
        File file = new File(filePath);

        Uri uri = getUriForFile(context, new File(filePath));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);                 // 分享文件类型
        intent.setType(getFileContentType(filePath));                   // 分享文件类型
        intent.putExtra(Intent.EXTRA_SUBJECT, "share title");       // 添加分享标题
        intent.putExtra(Intent.EXTRA_TEXT, "share content");        // 添加分享内容
        intent.putExtra(Intent.EXTRA_STREAM, uri);                       // 分享文件

//        // 指定分享到的app
//        intent.setPackage(PACKAGENAME_WEIXIN);

//        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");     // 直接分享至微信好友    suc
//        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");    // 分享至微信朋友圈   fail
//        ComponentName comp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");   // 分享至QQ好友    suc
//        ComponentName comp = new ComponentName("com.qzone", "com.qzonex.module.operation.ui.QZonePublishMoodActivity"); // 分享至QQ空间  fail
//        intent.setComponent(comp);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, "Share"));
    }

    /**
     * 分享单个/多个文件(微信多文件分享仅支持照片格式)
     * @param context
     * @param list
     */
    public static void shareToApp(Context context, List<String> list) {
        if (null == list || 0 == list.size()) {
            Log.e(TAG, "shareToApp Failed, please check list.");
            return;
        }

        boolean multiple = list.size() > 1;
        Intent intent = new Intent(multiple ? Intent.ACTION_SEND_MULTIPLE : Intent.ACTION_SEND);
        intent.setType(getFileContentType(list.get(0)));
        if (multiple) {
            ArrayList<Uri> listUri = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                Uri uri = getUriForFile(context, new File(list.get(i)));
                listUri.add(uri);
            }
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, listUri);
        } else {
            Uri uri = getUriForFile(context, new File(list.get(0)));
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags( Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        context.startActivity(intent);
    }

    // 打开相机进行拍照，照片保存到/storage/emulated/0/Android/data/${packagename}/files/Pictures目录下
    public static void jump2Camera(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            String fileName = System.currentTimeMillis() + ".jpg";
            String fileName = "wtx.mp4";
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
            if (file.exists()) {
                file.delete();
            }

            Uri uri = getUriForFile(context, file);
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);      // 拍照
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);        // 录像
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            Log.i(TAG, "filePath:" + file.getPath());
            Log.i(TAG, "uriPath:" + uri.toString());
            context.startActivity(intent);
        } else {
            Log.e(TAG,"sdcard not exists");
        }
    }

    // 查询所有视频
    public static void MakeCursor(Context context) {
        String[] projection = new String[] {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,       // 标题
                MediaStore.Video.Media.DATA,        // 路径
                MediaStore.Video.Media.MIME_TYPE,   // 类型
                MediaStore.Video.Media.ARTIST
        };
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor;
        String selection;
        String[] selectionArgs = {};
        String sortOrder;
        String filterString = "";
        Uri uri;
        if (null == resolver) {
            System.out.println("resolver = null");
        } else {
            sortOrder = MediaStore.Video.Media.TITLE + " COLLATE UNICODE";
            if (TextUtils.isEmpty(filterString)){
                selection = MediaStore.Video.Media.TITLE + " != ''";
            }else{
                selection = MediaStore.Video.Media.TITLE + " like '%" + filterString + "%'";
            }
            cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection , selectionArgs, sortOrder);
            while (cursor.moveToNext()) {
                int idColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media._ID);
                int id = cursor.getInt(idColumnIndex);
//                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
//                String DATA = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
//                String MIME_TYPE = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
//                String ARTIST = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST));
//                Log.i(TAG, "id:" + id);
//                Log.i(TAG, "title:" + title);
//                Log.i(TAG, "DATA:" + DATA);
//                Log.i(TAG, "MIME_TYPE:" + MIME_TYPE);
//                Log.i(TAG, "ARTIST:" + ARTIST);
                Uri baseUri = Uri.parse("content://media/external/video/media");
                uri = Uri.withAppendedPath(baseUri, "" + id);
                Log.w(TAG, "uri:" + uri.toString());
            }
            cursor.close();
        }
    }

    /**
     * 分享前执行本代码，主要用于兼容SDK18以上的系统
     * 否则会报android.os.FileUriExposedException: file:///xxx.pdf exposed beyond app through ClipData.Item.getUri()
     */
    private static void checkFileUriExposure() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
    }

    /**
     * 选择打开文件的方式
     * 兼容7.0
     * @param context     activity
     * @param file        File
     * @param contentType 文件类型如：文本（text/html）
     *                    当手机中没有一个app可以打开file时会抛ActivityNotFoundException
     */
    public static void openFileAsFormat(Context context, File file, String contentType) throws ActivityNotFoundException {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//增加读写权限
        intent.putExtra(Intent.EXTRA_STREAM, getUriForFile(context, file));
        intent.setDataAndType(getUriForFile(context, file), contentType);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /** 视频文件路径转Uri
     * 需在AndroidManifest.xml文件中声明android:authorities="${packagename}.fileprovider"，并在res/xml目录中添加filepaths.xml文件指明路径
     * 例：       file path:/storage/emulated/0/Android/data/${packagename}/files/${dirname}/1639728389396.jpg
     * 对应Uri    content://${packagename}.fileprovider/external_file_path/${dirname}/1639728389396.jpg
     */
    public static Uri getUriForFile(Context context, File file) {
        Uri uri = null;
        Log.w(TAG, "getUriForFile:" + file.getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (file.getPath().startsWith("/storage/emulated/0/Android/data/" + context.getPackageName()) ||
                    file.getPath().startsWith("/data/user/0/" + context.getPackageName())
            ) {            // 私有目录
                Log.i(TAG, "private dir.");
                uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
            } else {                                                                                                    // 公有目录
                Log.i(TAG, "public dir.");
                Cursor cursor = context.getContentResolver().query(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Video.Media._ID,
                                MediaStore.Video.Media.TITLE,
                                MediaStore.Video.Media.DATA,
                                MediaStore.Video.Media.MIME_TYPE,
                                MediaStore.Video.Media.ARTIST
                        },
                        MediaStore.Video.Media.DATA + " =? ",
                        new String[]{file.getPath()},
                        null);
                if (0 < cursor.getCount()) {
                    Log.i(TAG, "cursor.getCount():" + cursor.getCount());
//                    while (cursor.moveToNext()) {
//                        int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
//                        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
//                        String DATA = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
//                        String mime_type = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
//                        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST));
//                        Log.i(TAG, "id:" + id + ", artist:" + artist + ", mime_type:" + mime_type + ", DATA:" + DATA);
//                    }
                    if (cursor.moveToNext()) {
                        int columnIndex = cursor.getColumnIndex(MediaStore.Video.Media._ID);
                        if (0 <= columnIndex) {
                            int id = cursor.getInt(columnIndex);
                            Uri baseUri = Uri.parse("content://media/external/video/media");
                            uri = Uri.withAppendedPath(baseUri, "" + id);
                        }
                    }
                } else {
                    Log.e(TAG, "cursor.getCount() == 0.");
                }
                cursor.close();
            }
        } else {
            checkFileUriExposure();     // 报错可添加此句
            uri = Uri.fromFile(file);   // 高版本直接转报错
        }
        Log.i(TAG, "getUriForFile, file:" + file.getPath());
        Log.i(TAG, "--->");
        if (null != uri) {
            Log.i(TAG, "getUriForFile, uri:" + uri.toString());
        } else {
            Log.e(TAG, "getUriForFile, uri:null");
        }
        return uri;
    }

    private static void shareToEmail(Context context) {
        // 必须明确使用mailto前缀来修饰邮件地址,如果使用
        // intent.putExtra(Intent.EXTRA_EMAIL, email)，结果将匹配不到任何应用
        Uri uri = Uri.parse("mailto:3802**92@qq.com");
        String[] email = {"3802**92@qq.com"};
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
        intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分"); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分");    // 正文
        context.startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
    }

    public static void test(Context context) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "wtx.mp4");       // 私有目录中的文件
        boolean isExist = file.exists();
        Log.i(TAG, "--->isExist:" + isExist + ", path:" + file.getPath());

        Log.i(TAG, "--->" + getFileContentType("file.mpeg"));
        Log.i(TAG, "--->" + getFileContentType("file.mp3"));
        Log.i(TAG, "--->" + getFileContentType("file.mp4"));
        Log.i(TAG, "--->" + getFileContentType("file.avi"));
        Log.i(TAG, "--->" + getFileContentType("file.mkv"));
        Log.i(TAG, "--->" + getFileContentType("file.ape"));
        Log.i(TAG, "--->" + getFileContentType("file.flac"));

        shareToEmail(context);
    }
}
