package com.example.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式
 */
public class RegularUtil {
    private static final String TAG = "wtx_RegularUtil";

    public static List<String> regEx(String textArea, String p) {
        String pattern = p;
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(textArea);
        List<String> targetList = new ArrayList<>();
        while (matcher.find()) {
            String substring = textArea.substring(matcher.start() + 1, matcher.end() - 1);
            targetList.add(substring);
        }
        return targetList;
    }

    public static void test() {
        // 邮件地址拆分
        String str = "way.ping.li <way.ping.li@gmail.com>, wang232024 <wang232024@163.com>, kingflute <kingflute@126.com>, wangtx <wangtx@126.com>";
        String pattern = "<.{1,}?>";    // <之后接任意字符，>收尾，非贪婪。
        regEx(str, pattern);

    }
}

/*
\   将下一字符标记为特殊字符、文本、反向引用或八进制转义符。例如， n匹配字符n。\n匹配换行符。序列\\\\匹配\\，\\(匹配(。
^   匹配输入字符串开始的位置。
$   匹配输入字符串结尾的位置。
*   零次或多次匹配前面的字符或子表达式。{0,}
+   一次或多次匹配前面的字符或子表达式。{1,}
?   零次或一次匹配前面的字符或子表达式。{0,1}	
?   当此字符紧随任何其他限定符（*、+、?、{n}、{n,}、{n,m}）之后时，匹配模式是"非贪心的"。"非贪心的"模式匹配搜索到的、尽可能短的字符串，而默认的"贪心的"模式匹配搜索到的、尽可能长的字符串。例如，在字符串"oooo"中，"o+?"只匹配单个"o"，而"o+"匹配所有"o"。
.   匹配除"\r\n"之外的任何单个字符。若要匹配包括"\r\n"在内的任意字符，请使用诸如"[\s\S]"之类的模式。
 */
