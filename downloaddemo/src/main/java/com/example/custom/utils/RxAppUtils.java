package com.example.custom.utils;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.util.ArrayMap;
import android.util.AtomicFile;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Author:wenjunjie
 * Date:2022/1/4
 * Time:下午3:24
 * Description:
 **/
public class RxAppUtils {

    private static final String TAG = RxAppUtils.class.getSimpleName();



    public static Observable<ResolveInfo> getPkgList() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        // 桌面启动属性
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 使用 queryIntentActivities 获取应用名称和包名
        PackageManager packageManager = Utils.getContext().getPackageManager();
        List<ResolveInfo> mResolveInfos = packageManager.queryIntentActivities(intent,PackageManager.MATCH_ALL);
        return Observable.fromIterable(mResolveInfos);

    }


    public static Observable<PackageInfo> getInstallPkgList() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        // 桌面启动属性
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 使用 queryIntentActivities 获取应用名称和包名
        PackageManager packageManager = Utils.getContext().getPackageManager();
        List<ResolveInfo> mResolveInfos = packageManager.queryIntentActivities(intent,PackageManager.MATCH_ALL);
        List<PackageInfo> pakcageList =  new ArrayList<>();

        mResolveInfos.forEach(item ->{
            try {
                pakcageList.add(packageManager.getPackageInfo(item.activityInfo.packageName,0));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        });


        return Observable.fromIterable(pakcageList);

    }

    public static Observable<PackageInfo> getInstallPkgInfo(String packageName){
        PackageManager packageManager = Utils.getContext().getPackageManager();
        PackageInfo packageInfo = null;
        try {
         packageInfo = packageManager.getPackageInfo(packageName,0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return Observable.just(packageInfo);
    }


    public static int getPkgListSize() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        // 桌面启动属性
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 使用 queryIntentActivities 获取应用名称和包名
        PackageManager packageManager = Utils.getContext().getPackageManager();
        List<ResolveInfo> mResolveInfos = packageManager.queryIntentActivities(intent,PackageManager.MATCH_ALL);

        return mResolveInfos.size();

    }

    public static Observable<ResolveInfo> getPkgListByPackage(String packageName) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        // 桌面启动属性
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(packageName);
        // 使用 queryIntentActivities 获取应用名称和包名
        PackageManager packageManager = Utils.getContext().getPackageManager();
        List<ResolveInfo> mResolveInfos = packageManager.queryIntentActivities(intent,PackageManager.MATCH_ALL);

        return Observable.fromIterable(mResolveInfos);

    }


    public static boolean isSysApp(Context context, String packageName){
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1;
    }


    public static void goAppInfoPage(Activity context,String packageName){
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", packageName, null));
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName",packageName);
        }
        ((Activity)context).startActivity(localIntent);

    }





    public static Map getAppUseTime(){
        UsageStatsManager usm = (UsageStatsManager) Utils.getContext().getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        long startTime = calendar.getTimeInMillis();
        List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,System.currentTimeMillis());
        if (stats.isEmpty()) {
            return Collections.emptyMap();
        }
        ArrayMap<String, UsageStats> aggregatedStats = new ArrayMap<>();
        final int statCount = stats.size();
        for (int i = 0; i < statCount; i++) {
            UsageStats newStat = stats.get(i);
            UsageStats existingStat = aggregatedStats.get(newStat.getPackageName());
            if (existingStat == null) {
                aggregatedStats.put(newStat.getPackageName(), newStat);
            } else {
                existingStat.add(newStat);
            }
        }
        return aggregatedStats;

    }


    public static void print(){
        Calendar beginCal = Calendar.getInstance();
        beginCal.add(Calendar.DAY_OF_YEAR, -5);

        Calendar endCall = Calendar.getInstance();
        endCall.add(Calendar.DAY_OF_YEAR, -1);

        UsageStatsManager usm = (UsageStatsManager) Utils.getContext().getSystemService(Context.USAGE_STATS_SERVICE);
        AtomicFile a = new AtomicFile(new File("s"));
         final List<UsageStats> queryUsagesStats= usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginCal.getTimeInMillis(), System.currentTimeMillis());
        SimpleDateFormat df =

                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        for(UsageStats us: queryUsagesStats)
        {

            long beginTime = us.getFirstTimeStamp();
            String beginDate = df.format(new Date(beginTime));


            long endTime = us.getLastTimeStamp();
            String endDate = df.format(new Date(endTime));
            String lastTime = df.format(new Date(us.getLastTimeUsed()));
            long totalTimeInForeground = us.getTotalTimeInForeground()/1000L;
            Log.d(TAG,Log.getStackTraceString(new Throwable()));

            if(us.getPackageName().equals("com.tencent.weread")
                    ||us.getPackageName().equals("com.yinxiang")||us.getPackageName().equals("com.youdao.note")
                    ||us.getPackageName().equals("com.chaozh.iReaderFree"))
            System.out.println("usage pkg = " + us.getPackageName() + ", beginTime " + beginDate + ", endTime= " + endDate + ", lastTime = " + lastTime + ", totalTime = " + totalTimeInForeground);
        }
    }





}
