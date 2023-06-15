package com.example.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private static final String TAG = "wtx_ExcelUtils";

    /**
     * 读取Excel文件.
     * 保存为html文件
     * @return
     * @throws Exception
     */
    public static StringBuilder readExcel(InputStream inputStream) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        Log.i(TAG, "Sheet number:" + workbook.getNumberOfSheets());
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            String sheetName = workbook.getSheetName(sheetIndex);
            if (null != sheet) {
                int firstRowNum = sheet.getFirstRowNum();
                int lastRowNum = sheet.getLastRowNum();
                Log.i(TAG,  "sheetName:" + sheetName + ", firstRowNum:" + firstRowNum + ", lastRowNum:" + lastRowNum);
                for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                    if (null != sheet.getRow(rowNum)) {// 如果行不为空，
                        HSSFRow row = sheet.getRow(rowNum);
                        short firstCellNum = row.getFirstCellNum(); // 该行的第一个单元格
                        short lastCellNum = row.getLastCellNum(); // 该行的最后一个单元格

                        stringBuilder.append("index:" + rowNum);
                        for (short cellNum = firstCellNum; cellNum <= lastCellNum; cellNum++) { // 循环该行的每一个单元格
                            HSSFCell cell = row.getCell(cellNum);
                            if (cell != null) {
                                if (HSSFCell.CELL_TYPE_BLANK != cell.getCellType()) {
                                    stringBuilder.append(", " + getCellValue(cell));
                                }
                            }
                        }
                        stringBuilder.append("\n");
                    }
                }
            }
        }
        Log.i(TAG, stringBuilder.toString());
        return stringBuilder;
    }

    /**
     * 读取Excel文件.
     * 保存为html文件
     * @return
     * @throws Exception
     */
    public static StringBuffer readExcel(InputStream inputStream, boolean save) throws Exception {
        StringBuffer mStringBuffer = new StringBuffer();
        mStringBuffer.append("<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:excel' xmlns='http://www.w3.org/TR/REC-html40'>");
        mStringBuffer.append("<head><meta http-equiv=Content-Type content='text/html; charset=utf-8'><meta name=ProgId content=Excel.Sheet>");
        try {
            // 获整个Excel
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            Log.i(TAG, "Sheet number:" + workbook.getNumberOfSheets());
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                HSSFSheet sheet = workbook.getSheetAt(sheetIndex);// 获所有的sheet
                String sheetName = workbook.getSheetName(sheetIndex); // sheetName
                if (workbook.getSheetAt(sheetIndex) != null) {
                    sheet = workbook.getSheetAt(sheetIndex);// 获得不为空的这个sheet
                    if (sheet != null) {
                        int firstRowNum = sheet.getFirstRowNum(); // 第一行
                        int lastRowNum = sheet.getLastRowNum(); // 最后一行
                        // 构造Table
                        mStringBuffer.append("<table width=\"100%\" style=\"border:1px solid #000;border-width:1px 0 0 1px;margin:2px 0 2px 0;border-collapse:collapse;\">");
                        Log.i(TAG, "firstRowNum:" + firstRowNum + ", lastRowNum:" + lastRowNum);
                        for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                            if (sheet.getRow(rowNum) != null) {// 如果行不为空，
                                HSSFRow row = sheet.getRow(rowNum);
                                short firstCellNum = row.getFirstCellNum(); // 该行的第一个单元格
                                short lastCellNum = row.getLastCellNum(); // 该行的最后一个单元格
                                int height = (int) (row.getHeight() / 15.625); // 行的高度
                                mStringBuffer.append("<tr height=\"" + height + "\" style=\"border:1px solid #000;border-width:0 1px 1px 0;margin:2px 0 2px 0;\">");

                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("index:" + rowNum);
                                for (short cellNum = firstCellNum; cellNum <= lastCellNum; cellNum++) { // 循环该行的每一个单元格
                                    HSSFCell cell = row.getCell(cellNum);
                                    if (cell != null) {
                                        if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                                            continue;
                                        } else {
                                            StringBuffer tdStyle = new StringBuffer(
                                                    "<td style=\"border:1px solid #000; border-width:0 1px 1px 0;margin:2px 0 2px 0; ");
                                            HSSFCellStyle cellStyle = cell.getCellStyle();
                                            HSSFPalette palette = workbook.getCustomPalette(); // 类HSSFPalette用于求颜色的国际标准形式
                                            HSSFColor hColor = palette.getColor(cellStyle.getFillForegroundColor());
                                            HSSFColor hColor2 = palette.getColor(cellStyle.getFont(workbook).getColor());

                                            String bgColor = convertToStardColor(hColor);// 背景颜色
                                            short boldWeight = cellStyle.getFont(workbook).getBoldweight(); // 字体粗细
                                            short fontHeight = (short) (cellStyle.getFont(workbook).getFontHeight() / 2); // 字体大小
                                            String fontColor = convertToStardColor(hColor2); // 字体颜色
                                            if (bgColor != null && !"".equals(bgColor.trim())) {
                                                tdStyle.append(" background-color:" + bgColor + "; ");
                                            }
                                            if (fontColor != null && !"".equals(fontColor.trim())) {
                                                tdStyle.append(" color:" + fontColor + "; ");
                                            }
                                            tdStyle.append(" font-weight:" + boldWeight + "; ");
                                            tdStyle.append(" font-size: " + fontHeight + "%;");
                                            mStringBuffer.append(tdStyle + "\"");

                                            int width = (int) (sheet.getColumnWidth(cellNum) / 35.7); //
                                            int cellReginCol = getMergerCellRegionCol(sheet, rowNum, cellNum); // 合并的列（solspan）
                                            int cellReginRow = getMergerCellRegionRow(sheet, rowNum, cellNum);// 合并的行（rowspan）
                                            String align = convertAlignToHtml(cellStyle.getAlignment()); //
                                            String vAlign = convertVerticalAlignToHtml(cellStyle.getVerticalAlignment());

                                            mStringBuffer.append(" align=\"" + align + "\" valign=\"" + vAlign + "\" width=\"" + width + "\" ");
                                            mStringBuffer.append(" colspan=\"" + cellReginCol + "\" rowspan=\"" + cellReginRow + "\"");
                                            mStringBuffer.append(">" + getCellValue(cell) + "</td>");

                                            stringBuilder.append(", " + getCellValue(cell));
                                        }
                                    }
                                }
                                Log.i(TAG, stringBuilder.toString());
                                mStringBuffer.append("</tr>");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return mStringBuffer;
    }

    /**
     * 取得单元格的值
     *
     * @param cell
     * @return
     * @throws IOException
     */
    private static Object getCellValue(HSSFCell cell) throws IOException {
        Object value = "";
        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            value = cell.getRichStringCellValue().toString();
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                Date date = (Date) cell.getDateCellValue();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                value = sdf.format(date);
            } else {
                double value_temp = (double) cell.getNumericCellValue();
                BigDecimal bd = new BigDecimal(value_temp);
                BigDecimal bd1 = bd.setScale(3, bd.ROUND_HALF_UP);
                value = bd1.doubleValue();

                DecimalFormat format = new DecimalFormat("#0.###");
                value = format.format(cell.getNumericCellValue());
            }
        }
        if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
            value = "";
        }
        return value;
    }

    /**
     * 判断单元格在不在合并单元格范围内，如果是，获取其合并的列数。
     *
     * @param sheet   工作表
     * @param cellRow 被判断的单元格的行号
     * @param cellCol 被判断的单元格的列号
     * @return
     * @throws IOException
     */
    private static int getMergerCellRegionCol(HSSFSheet sheet, int cellRow, int cellCol) throws IOException {
        int retVal = 0;
        int sheetMergerCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            CellRangeAddress cra = (CellRangeAddress) sheet.getMergedRegion(i);
            int firstRow = cra.getFirstRow(); // 合并单元格CELL起始行
            int firstCol = cra.getFirstColumn(); // 合并单元格CELL起始列
            int lastRow = cra.getLastRow(); // 合并单元格CELL结束行
            int lastCol = cra.getLastColumn(); // 合并单元格CELL结束列
            if (cellRow >= firstRow && cellRow <= lastRow) { // 判断该单元格是否是在合并单元格中
                if (cellCol >= firstCol && cellCol <= lastCol) {
                    retVal = lastCol - firstCol + 1; // 得到合并的列数
                    break;
                }
            }
        }
        return retVal;
    }

    /**
     * 判断单元格是否是合并的单格，如果是，获取其合并的行数。
     *
     * @param sheet   表单
     * @param cellRow 被判断的单元格的行号
     * @param cellCol 被判断的单元格的列号
     * @return
     * @throws IOException
     */
    private static int getMergerCellRegionRow(HSSFSheet sheet, int cellRow,
                                              int cellCol) throws IOException {
        int retVal = 0;
        int sheetMergerCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            CellRangeAddress cra = (CellRangeAddress) sheet.getMergedRegion(i);
            int firstRow = cra.getFirstRow(); // 合并单元格CELL起始行
            int firstCol = cra.getFirstColumn(); // 合并单元格CELL起始列
            int lastRow = cra.getLastRow(); // 合并单元格CELL结束行
            int lastCol = cra.getLastColumn(); // 合并单元格CELL结束列
            if (cellRow >= firstRow && cellRow <= lastRow) { // 判断该单元格是否是在合并单元格中
                if (cellCol >= firstCol && cellCol <= lastCol) {
                    retVal = lastRow - firstRow + 1; // 得到合并的行数
                    break;
                }
            }
        }
        return retVal;
    }

    /**
     * 单元格背景色转换
     *
     * @param hc
     * @return
     */
    private static String convertToStardColor(HSSFColor hc) {
        StringBuffer sb = new StringBuffer("");
        if (hc != null) {
            int a = HSSFColor.AUTOMATIC.index;
            int b = hc.getIndex();
            if (a == b) {
                return null;
            }
            sb.append("#");
            for (int i = 0; i < hc.getTriplet().length; i++) {
                String str;
                String str_tmp = Integer.toHexString(hc.getTriplet()[i]);
                if (str_tmp != null && str_tmp.length() < 2) {
                    str = "0" + str_tmp;
                } else {
                    str = str_tmp;
                }
                sb.append(str);
            }
        }
        return sb.toString();
    }

    /**
     * 单元格水平对齐方式
     *
     * @param alignment
     * @return
     */
    private static String convertAlignToHtml(short alignment) {
        String align = "left";
        switch (alignment) {
            case HSSFCellStyle.ALIGN_LEFT:
                align = "left";
                break;
            case HSSFCellStyle.ALIGN_CENTER:
                align = "center";
                break;
            case HSSFCellStyle.ALIGN_RIGHT:
                align = "right";
                break;
            default:
                break;
        }
        return align;
    }

    /**
     * 单元格垂直对齐方式
     *
     * @param verticalAlignment
     * @return
     */
    private static String convertVerticalAlignToHtml(short verticalAlignment) {
        String valign = "middle";
        switch (verticalAlignment) {
            case HSSFCellStyle.VERTICAL_BOTTOM:
                valign = "bottom";
                break;
            case HSSFCellStyle.VERTICAL_CENTER:
                valign = "center";
                break;
            case HSSFCellStyle.VERTICAL_TOP:
                valign = "top";
                break;
            default:
                break;
        }
        return valign;
    }

    /**
     *  写入Excel文件
     * @param indexX            起始空indexX列
     * @param indexY            起始空indexY列
     * @param tableHead         表头内容
     * @param tableColumn       表列内容
     * @param content           数据
     * @param outputStream      输出流
     * @param sheetName         表单名称
     * @throws Exception
     */
    public static void writeExcel(int indexX, int indexY, String[] tableHead, String[] tableColumn,
                                  List<? extends Object> content, OutputStream outputStream, String sheetName) throws Exception {
        int positionX = indexX;
        int positionY = indexY;
        if (tableColumn != null)
            positionX = indexX + 1;
        if (tableHead != null)
            positionY = indexY + 1;

        // 创建Excel工作表
        WritableWorkbook wwb = Workbook.createWorkbook(outputStream);

        // 添加第一个工作表并设置第一个sheet的名称
        WritableSheet sheet = wwb.createSheet(sheetName, 0);

        Label label;
        // 添加表头
        if (tableHead != null) {
            for (int i = 0; i < tableHead.length; i++) {
                label = new Label(positionX + i, indexY, tableHead[i], getHeader());
                sheet.addCell(label);
            }
        }
        // 添加表列
        if (tableColumn != null) {
            for (int i = 0; i < tableColumn.length; i++) {
                label = new Label(indexX, positionY + i, tableColumn[i], getHeader());
                sheet.addCell(label);
            }
        }

        // 添加表内容
        for (int i = 0; i < content.size(); i++) {
            List<String> record = (List<String>) content.get(i);
            for (int j = 0; j < record.size(); j++) {
                Label labelContent = new Label(positionX + j, positionY + i, record.get(j));
                sheet.addCell(labelContent);
            }
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

    public static void testWriteExcel(String desFilePath) {
        try {
            List<List<String>> list = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                List<String> mList = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    mList.add(j + "_" + i);
                }
                list.add(mList);
            }

            writeExcel(0, 0,
                    new String[] {"111", "222", "333", "444"},
                    new String[] {"序号1", "序号2", "序号3", "序号4"},
                    list,
                    new FileOutputStream(desFilePath),
                    "First Sheet"
                    );
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

}