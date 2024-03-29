package com.example.common.util;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.FileInputStream;

public class XmlUtil {
    private static final String TAG = "wtx_XmlUtil";

    private static void getInfoFromXml(XmlPullParser xmlPullParser) {
        try {
            if (null == xmlPullParser) {
                return;
            }

            int xmlEventType;
            while (XmlResourceParser.END_DOCUMENT != (xmlEventType = xmlPullParser.next())) {
                Log.w("wtx", "xmlEventType:" + xmlEventType);
                switch (xmlEventType) {
                    case XmlResourceParser.START_TAG:
                        Log.e(TAG, "name:" + xmlPullParser.getName());

                        for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
                            String name = xmlPullParser.getAttributeName(i);
                            String value = xmlPullParser.getAttributeValue(i);
                            Log.i(TAG, i + ", " + name + ":" + value);
                        }

//                        // package="com.android.email"
//                        String value1 = xmlPullParser.getAttributeValue(null, "package");
//                        Log.i(TAG, "0--->" + value1);

//                        // android:name="android.permission.ACCESS_NETWORK_STATE"
//                        String value2 = xmlPullParser.getAttributeValue("http://schemas.android.com/apk/res/android", "name");
//                        Log.i(TAG, "1--->" + value2);
                        break;
                    case XmlResourceParser.END_TAG:

                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getInfoFromXml(String xmlFilePath) {
        try {
            //获得工厂
            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            //获得xml的解析器
            XmlPullParser xmlPullParser = parserFactory.newPullParser();
            //给解析器设置一个输入源
            xmlPullParser.setInput(new FileInputStream(xmlFilePath), "UTF-8");

            getInfoFromXml(xmlPullParser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getInfoFromXml(Context context, int xmlId) {
        XmlPullParser xmlPullParser = context.getResources().getXml(xmlId);

        getInfoFromXml(xmlPullParser);
    }

    public static void test(Context context) {
//        getInfoFromXml(context, R.xml.filepaths);
    }
}
