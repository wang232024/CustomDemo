package com.example.common.util;

import android.util.Log;
import java.util.Arrays;

public class ByteArrayUtil {
    private static final String TAG = "wtx_ByteArrayUtil";

    public static void printByteArray(String tag, byte[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append(tag).append(", [");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (array.length - 1 != i) {
                sb.append("\t");
            }
        }
        sb.append("]\n");

        Log.e(TAG, sb.toString());
    }

    public static void printShortArray(String tag, short[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append(tag).append(", [");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (array.length - 1 != i) {
                sb.append("\t");
            }
        }
        sb.append("]\n");

        Log.e(TAG, sb.toString());
    }

    /**
     * 将byte数组转为short数组
     * @param src 源byte数组
     * @param des 转换后的short数组
     * @param isBig 是否为大端模式
     * @return 0：转换成功   -1：转换失败
     */
    public static int byteArray2ShortArray(byte[] src, short[] des, boolean isBig) {
        int len = des.length;
        if (len * 2 != src.length) {
            Log.e(TAG, "byteArray2ShortArray error");
            return - 1;
        }
        for (int i = 0; i < len; i++) {
            if (!isBig) {
                des[i] = (short) ((src[i * 2] & 0xff) | (src[i * 2 + 1] & 0xff) << 8);
            } else {
                des[i] = (short) ((src[i * 2 + 1] & 0xff) | (src[i * 2] & 0xff) << 8);
            }
        }
        return 0;
    }

    /**
     * 将short数组转为byte数组
     * @param src 源short数组
     * @param des 转换后的byte数组
     * @param isBig 是否为大端模式
     * @return 0：转换成功   -1：转换失败
     */
    public static int shortArray2ByteArray(short[] src, byte[] des, boolean isBig) {
        int len = src.length;
        if (len * 2 != des.length) {
            Log.e(TAG, "shortArray2ByteArray error");
            return - 1;
        }
        for (int i = 0; i < len; i++) {
            if (!isBig) {
                des[2 * i] = (byte) (src[i] & 0xff);
                des[2 * i + 1] = (byte) ((src[i] & 0xff00) >> 8);
            } else {
                des[2 * i + 1] = (byte) (src[i] & 0xff);
                des[2 * i] = (byte) ((src[i] & 0xff00) >> 8);
            }
        }
        return 0;
    }

    /**
     * 数据拆分 (A0B0C0D0A1B1C1D1...) -> (A0A1...B0B1...C0C1...D0D1...)
     * 将src中的(len * num)个数据拆分到des数组中, 共有num个数组, 每个数组中存储len个数据
     */
    public void dataSplit(short[] src, short[][] des, int len, int num) {
        int i, j;
        for (i = 0; i < num; i++) {
            for (j = 0; j < len; j++) {
                System.arraycopy(src, num * j + i, des[i], j, 1);
            }
        }
    }

    private static int offsetBufferLen = 0;          // 计算偏移量
    private static byte[] cacheBuffer = null;      // 用于缓存遗留数据和新输入数据

    /**
     * 每次输入10个数据，每次输出4或24个数据。通过返回值确定des二维数组应该取多少行数据。
     *
     * @param src       源数据，一维
     * @param srcLen    每次输入src的数据量
     * @param des       目标数据，二维，行数表示输出数据量的组数，根据返回值确定，长度为(((perSrc / perDes) + 1) * desLen)
     * @param desLen    每次输出的数据量
     * @return  1:返回src中有(srcLen / desLen)行     2:返回src中有(srcLen / desLen + 1)行，需要多取一行。
     */
    public static int offsetBuffer(byte[] src, int srcLen, byte[][] des, int desLen) {
        int row = srcLen / desLen;
        if (0 == offsetBufferLen) {
            for (int i = 0; i < row; i++) {
                System.arraycopy(src, i * desLen, des[i], 0, desLen);
            }
            offsetBufferLen = srcLen - row * desLen;
            if (null == cacheBuffer) {
                cacheBuffer = new byte[srcLen + desLen];
            }
            System.arraycopy(src, row * desLen, cacheBuffer, 0, offsetBufferLen);
            return 1;
        } else {
            System.arraycopy(src, 0, cacheBuffer, offsetBufferLen, srcLen);
            for (int i = 0; i < row; i++) {
                System.arraycopy(cacheBuffer, i * desLen, des[i], 0, desLen);
            }
            offsetBufferLen = (srcLen + offsetBufferLen) - row * desLen;
            if (desLen <= offsetBufferLen) {
                System.arraycopy(cacheBuffer, row * desLen, des[row], 0, desLen);
                System.arraycopy(cacheBuffer, (row + 1) * desLen, cacheBuffer, 0, offsetBufferLen - desLen);
                offsetBufferLen = offsetBufferLen - desLen;
                return 2;
            } else {
                if (0 < row) {
                    System.arraycopy(cacheBuffer, row * desLen, cacheBuffer, 0, offsetBufferLen);
                }
                return 1;
            }
        }
    }

    /******** 测试 *********/
    private static final int perSrc = 10;       // 每次输入数量
    private static final int mul = 10;          // 总共输入次数
    private static final int perDes = 6;       // 每次输出数量
    public static void testByte() {
        byte[] src = new byte[perSrc * mul];
        // 初始化测试数据
        for (int i = 0; i < perSrc * mul; i++) {
            src[i] = (byte) i;
        }

        byte[] buf = new byte[perSrc];

        byte[][] ddd = new byte[(perSrc / perDes) + 1][perDes];
        for (int i = 0; i < mul; i++) {
            // 每次取用测试数据中的perSrc个
            System.arraycopy(src, i * perSrc, buf, 0, perSrc);

            int m = offsetBuffer(buf, perSrc, ddd, perDes);
            int row = 0;
            if (1 == m) {
                row = perSrc / perDes;
            } else if (2 == m) {
                row = (perSrc / perDes) + 1;
            }
            for (int j = 0; j < row; j++) {
                Log.i(TAG, "[" + i + "][" + j + "] : " + Arrays.toString(ddd[j]));
            }
        }
    }
}