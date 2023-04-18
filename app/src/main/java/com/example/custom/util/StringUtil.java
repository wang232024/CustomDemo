package com.example.custom.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 将double型数据最多保留4位小数
     * @param num
     * @return
     */
    public static double getTwoDecimal(double num) {
        DecimalFormat decimalFormat = new DecimalFormat("#.0000");
        String yearString = decimalFormat.format(num);
        return Double.valueOf(yearString);
    }

    /**
     * 匹配是否为数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        String bigStr;
        try {
            // -19162431.1254，不使用BigDecimal的话，变成-1.91624311254E7
            // 111.转换为111
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            //异常,包含非数字
            return false;
        }

        return pattern.matcher(bigStr).matches();
    }

}