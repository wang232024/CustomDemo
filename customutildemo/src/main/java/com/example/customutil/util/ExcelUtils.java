package com.example.customutil.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * 该工具类需要导入jxl.jar包
 */
public class ExcelUtils {
    private final static String TAG = "ExcelUtils";
    private static String[] tableColumn = new String[] {"111", "222", "333", "444"};

    public ExcelUtils() {
    }

    /**
     * 向名为fileName的excel文件中写入数据，文件名为fileName.xls
     * @param context       上下文对象
     * @param indexX        起始空indexX列
     * @param indexY        起始空indexY列
     * @param tableHead     表头内容
     * @param tableColumn   表列内容
     * @param records       数据
     * @param fileName      文件名
     * @throws Exception
     */
    public static void writeExcel(Context context, int indexX, int indexY, String[] tableHead, String[] tableColumn,
                                  List<? extends Object> records, String fileName) throws Exception {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "SDCard not Found!", Toast.LENGTH_SHORT).show();
            return ;
        }

        File dir = new File(Environment.getExternalStorageDirectory(), "111");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        int fixX = indexX;
        int fixY = indexY;
        if (tableColumn != null)
            fixX = indexX + 1;
        if (tableHead != null)
            fixY = indexY + 1;

        // 创建Excel工作表
        WritableWorkbook wwb;
        wwb = Workbook.createWorkbook(new BufferedOutputStream(new FileOutputStream(new File(dir, fileName + ".xls"))));

        // 添加第一个工作表并设置第一个sheet的名称
        WritableSheet sheet = wwb.createSheet("First Sheet", 0);

        Label label;
        // 添加表头
        if (tableHead != null) {
            for (int i = 0; i < tableHead.length; i++) {
                label = new Label(fixX + i, indexY, tableHead[i], getHeader());
                sheet.addCell(label);
            }
        }
        // 添加表列
        if (tableColumn != null) {
            for (int i = 0; i < tableColumn.length; i++) {
                label = new Label(indexX, fixY + i, tableColumn[i], getHeader());
                sheet.addCell(label);
            }
        }

        // 添加表内容
        for (int i = 0; i < records.size(); i++) {
            List<String> record = (List<String>) records.get(i);
            Label label1 = new Label(fixX, fixY + i, record.get(0));
            Label label2 = new Label(fixX + 1, fixY + i, record.get(1));
            Label label3 = new Label(fixX + 2, fixY + i, record.get(2));
            Label label4 = new Label(fixX + 3, fixY + i, record.get(3));
            sheet.addCell(label1);
            sheet.addCell(label3);
            sheet.addCell(label2);
            sheet.addCell(label4);
        }

        wwb.write();
        wwb.close();
    }

    private static CellFormat getHeader() {
        // 字体 大小 加粗
        WritableFont font = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD);// 定义字体

        try {
            font.setColour(Colour.BLUE);// 蓝色字体
        } catch (WriteException e1) {
            e1.printStackTrace();
        }
        WritableCellFormat format = new WritableCellFormat(font);
        try {
            format.setAlignment(jxl.format.Alignment.CENTRE);// 左右居中
            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 上下居中
            // format.setBorder(Border.ALL, BorderLineStyle.THIN,
            // Colour.BLACK);// 黑色边框
            // format.setBackground(Colour.YELLOW);// 黄色背景
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    public static void testWriteExcel(Context context) {
        try {
            List<List<String>> list = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                List<String> mList = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    mList.add(j + "_" + i);
                }
                list.add(mList);
            }

            writeExcel(context, 0, 0,
                    new String[] {"111", "222", "333", "444"},
                    new String[] {"序号1", "序号2", "序号3", "序号4"},
                    list,
                    "testExcel"
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}