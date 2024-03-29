package com.example.common.util;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Uri转文件路径
 * 公共目录视频/音频/图片获取
 *
 * 通过Intent调用文件管理器获取content://com.android.providers.media.documents/document/video%3A103910
 * 通过EXTERNAL_CONTENT_URI查询，EXTERNAL_CONTENT_URI:content://media/external/video/media
 * 得到content://media/external/video/media/103910
 */
public class UriUtil {
    private static final String TAG = "wtx_UriUtil";
    public static final String BASE_URI_VIDEO = "content://media/external/video/media";
    public static final String BASE_URI_AUDIO = "content://media/external/audio/media";
    public static final String BASE_URI_IMAGE = "content://media/external/images/media";

    public static void test(Context context) {
        UriUtil.MakeCursorVideo(context, true);
        UriUtil.MakeCursorAudio(context, true);
        UriUtil.MakeCursorImage(context, true);
    }

    // 查询所有视频
    public static void MakeCursorVideo(Context context, boolean isCopyToPrivate) {
        makeCursor(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, BASE_URI_VIDEO, isCopyToPrivate);
    }

    // 查询所有音频
    public static void MakeCursorAudio(Context context, boolean isCopyToPrivate) {
        makeCursor(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, BASE_URI_AUDIO, isCopyToPrivate);
    }

    // 查询所有图片
    public static void MakeCursorImage(Context context, boolean isCopyToPrivate) {
        makeCursor(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, BASE_URI_IMAGE, isCopyToPrivate);
    }

    public static void makeCursor(Context context, Uri uri, String baseUriString, boolean isCopyToPrivate) {
        // 视频/音频/图片查询字段实际均相同
        String[] projection = new String[] {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,       // 标题
                MediaStore.Audio.Media.DATA,        // 路径
                MediaStore.Audio.Media.MIME_TYPE,   // 类型
                MediaStore.Audio.Media.ARTIST
        };
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor;
        String selection;
        String[] selectionArgs = {};
        String sortOrder;
        String filterString = "";
        if (null == resolver) {
            Log.e(TAG, "makeCursor, resolve = null.");
        } else {
            sortOrder = MediaStore.Audio.Media.TITLE + " COLLATE UNICODE";
            if (TextUtils.isEmpty(filterString)){
                selection = MediaStore.Audio.Media.TITLE + " != ''";
            } else {
                selection = MediaStore.Audio.Media.TITLE + " like '%" + filterString + "%'";
            }
            cursor = resolver.query(uri, projection, selection, selectionArgs, sortOrder);
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(projection[0]);
                int titleIndex = cursor.getColumnIndex(projection[1]);
                int dataIndex = cursor.getColumnIndex(projection[2]);
                int mimeTypeIndex = cursor.getColumnIndex(projection[3]);
                int artistIndex = cursor.getColumnIndex(projection[4]);

                int id = cursor.getInt(idIndex);
                String title = cursor.getString(titleIndex);
                String DATA = cursor.getString(dataIndex);
                String MIME_TYPE = cursor.getString(mimeTypeIndex);
                String ARTIST = cursor.getString(artistIndex);
                Log.i(TAG, "id:" + id + ", title:" + title + ", DATA:" + DATA + ", MIME_TYPE:" + MIME_TYPE + ", ARTIST:" + ARTIST);

                Uri baseUri = Uri.parse(baseUriString);
                uri = Uri.withAppendedPath(baseUri, "" + id);
                Log.w(TAG, "uri:" + uri.toString());

                if (isCopyToPrivate) {
                    uriToFileApiQ(context, uri);
                }
            }
            Log.i(TAG, "makeCursor finish, baseUriString:" + baseUriString);
            cursor.close();
        }
    }

    /**
     * 根据Uri获取文件绝对路径，解决Android4.4以上版本Uri转换 兼容Android 10
     *
     * @param context
     * @param uri
     */
    public static String getFileAbsolutePath(Context context, Uri uri) {
        if (context == null || uri == null) {
            return null;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return getRealFilePath(context, uri);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                String id = DocumentsContract.getDocumentId(uri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return uriToFileApiQ(context, uri);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    //此方法 只能用于4.4以下的版本
    private static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Log.w(TAG, "getRealFilePath, scheme:" + scheme);
            String[] projection = {MediaStore.Images.ImageColumns.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (null != cursor && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * Android 10 以上适配
     * 将Uri转换成文件路径，对应的文件复制到外部私有目录cache下
     * @param context
     * @param uri
     * @return
     */
//    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static String uriToFileApiQ(Context context, Uri uri) {
        // 解析uri编号用%分隔，随机编号用_分隔
        String contentIndex = Math.round((Math.random() + 1) * 1000) + "_";
        // 检查文件管理器提供的uri
        String[] uriString = uri.getPath().replace("/", ":").split(":");
        if (1 <= uriString.length) {
            contentIndex = uriString[uriString.length - 1] + "%";
        }

        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            File file = new File(uri.getPath());
            return file.getAbsolutePath();
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                int index_display_name = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                String displayName = cursor.getString(index_display_name);

                InputStream is = null;
                FileOutputStream fos = null;
                File fileCache = null;
                try {
                    is = contentResolver.openInputStream(uri);
                    fileCache = new File(context.getExternalCacheDir().getAbsolutePath(), contentIndex + displayName);
                    fos = new FileOutputStream(fileCache);
                    int len;
                    byte[] buffer = new byte[4096];
                    while (0 < (len = is.read(buffer))) {
                        fos.write(buffer, 0, len);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != is) {
                        try {
                            is.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != fos) {
                        try {
                            fos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (null != fileCache) {
                    Log.i(TAG, "uriToFileApiQ OK, from:" + uri.toString());
                    Log.i(TAG, "uriToFileApiQ OK,   to:" + fileCache.getAbsolutePath());
                    return fileCache.getAbsolutePath();
                }
            }
        }
        Log.w(TAG, "uriToFileApiQ failed.");
        return null;
    }

    /**
     * Intent携带单个/多个文件uri时，解析Intent中携带的Uri到列表中
     * @param data
     */
    private void intentProcess(Intent data) {
        if (null != data) {
            ClipData clipData = data.getClipData();
            List<Uri> uriList = new ArrayList<>();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    if (null != uri) {
                        uriList.add(uri);
                    }
                }
            } else {
                Uri uri = data.getData();
                if (null != uri) {
                    uriList.add(uri);
                }
            }

            if (0 < uriList.size()) {
                for (int i = 0; i < uriList.size(); i++) {
                    Log.i("wtx", i + ", uri:" + uriList.get(i));
                }
            } else {
                Log.w("wtx", "uriList is empty.");
            }
        }
    }


    /**
    当接收uri的activity销毁之后，这个uri也就失去权限，它是跟随接收它的activity生命周期的，那么我们只需要在接收的activity销毁之前，将权限传递给其他的activity即可：

        Intent intent = new Intent(MainActivity.this, BActivity.class);
        grantUriPermission(getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    */

}
