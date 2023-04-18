package com.example.custom.utils;

public class UrlUtil {

    /**
     * 获取url的baseUrl
     * @param url
     * @return
     */
    public static String getBaseUrl(String url) {
        String head = "";
        int index = url.indexOf("://");
        if (-1 != index) {
            head = url.substring(0, index + 3);
            url = url.substring(index + 3);
        }
        index = url.indexOf("/");
        if (-1 != index) {
            url = url.substring(0, index + 1);
        }
        return head + url;
    }

    /**
     * 获取url指向的文件名
     * @return
     */
    public static String getUrlName(String url) {
        int index = url.lastIndexOf("/");
        if (-1 != index) {
            return url.substring(index + 1, url.length());
        }
        return "";
    }

}
