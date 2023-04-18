package com.example.custom.download;

public class DownloadContant {

    public static final String[] DOWNLOAD_SRC = {
            "https://pics0.baidu.com/feed/2f738bd4b31c870111bddad9cb7f84260708ff94.jpeg@f_auto?token=3df01d02e2f9e9ed95fe34b634f4e45d",

            "https://image.baidu.com/search/down?tn=download&word=download&ie=utf8&fr=detail&url=https%3A%2F%2Fpic.rmb.bdstatic.com%2Fa09108f788792a9f8c55da4112aa0fb5.jpeg&thumburl=https%3A%2F%2Fimg0.baidu.com%2Fit%2Fu%3D2902595229%2C3607614383%26fm%3D253%26fmt%3Dauto%26app%3D138%26f%3DJPEG%3Fw%3D800%26h%3D500",

            "https://qmuiteam.com/download/android/qmui_2.0.0-alpha07.apk",
    };
    // curl -I https://pics0.baidu.com/feed/2f738bd4b31c870111bddad9cb7f84260708ff94.jpeg@f_auto?token=3df01d02e2f9e9ed95fe34b634f4e45d
    // accept-ranges: bytes表示该响应头表明服务器支持Range请求,以及服务器所支持的单位是字节(这也是唯一可用的单位)
    /**
    70111bddad9cb7f84260708ff94.jpeg@f_auto?token=3df01d02e2f9e9ed95fe34b634f4e45d
    HTTP/2 200
    server: JSP3/2.0.14
    date: Tue, 27 Sep 2022 09:57:53 GMT
    content-type: image/jpeg
    content-length: 152740
    expires: Mon, 24 Oct 2022 15:52:01 GMT
    last-modified: Sun, 04 Jan 1970 00:00:00 GMT
    etag: eb3edd978f559137a26ab4bcfd19128a
    age: 237952
    accept-ranges: bytes
    access-control-allow-origin: *
    timing-allow-origin: *
    ohc-cache-hit: shaoxct53 [1], xaix53 [3]
    ohc-file-size: 152740
    x-cache-status: HIT
    **/
}
