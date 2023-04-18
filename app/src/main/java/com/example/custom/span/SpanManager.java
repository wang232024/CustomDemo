package com.example.custom.span;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.EditText;

import com.example.custom.R;

import java.util.ArrayList;
import java.util.List;

public class SpanManager {
    private static final String TAG = "wtx_SpanManager";
    private static SpanManager instance;
    private static final boolean isSpanWithFirstWord = true;    // 光标位于首位时，是否跟随第一个字符的span
    private Editable mEditable;

    private SpanManager() {
    }

    public static SpanManager getInstance() {
        if (null == instance) {
            synchronized (SpanManager.class) {
                if (null == instance) {
                    instance = new SpanManager();
                }
            }
        }
        return instance;
    }

    public void setSpanConfig(Editable editable, int type, int start, int end, int value) {

    }

    // 记录设置span组, index:type    0:start     1:value
    private int[][] currentSpanConfig = new int[ISpanConfig.MAX][2];

    public void setSpanConfig(EditText editText, int type, int start, int end, int value) {
        Editable editable = editText.getText();
        Log.i(TAG, "type:" + type + ", value:" + value + ", [" + start + ", " + end + "]");
        Log.i(TAG, "text:" + editable.toString());
        if (null == mEditable) {
            mEditable = editable;
        }

        ISpanConfig[] spansAll = editable.getSpans(0, editable.length(), ISpanConfig.class);

        if (start > end) {
            Log.e(TAG, "error, start is bigger than end, start:" + start + ", end:" + end);
            return ;
        } else if (start == end) {         // 未选中内容
            setCursorSpan(editable, type, start, end, value);
            // --->
            // 改为记录此次span
            currentSpanConfig[type][0] = start;
            currentSpanConfig[type][1] = value;
        } else {                            // 选中一段内容
            ISpanConfig[] spans = getSpanConfigType(editable.getSpans(start, end, ISpanConfig.class), type);

            if (0 == spans.length) {    // 所选范围内无span
                if (isSpanWithFirstWord) {
                    if (0 == editText.getSelectionStart()) {
                        editable.setSpan(newSpanConfig(type, value), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    } else {
                        editable.setSpan(newSpanConfig(type, value), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    }
                } else {
                    editable.setSpan(newSpanConfig(type, value), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                }
            } else if (1 == spans.length) {    // 所选范围内仅1个span
                int spanBegin = editable.getSpanStart(spans[0]);
                int spanEnd = editable.getSpanEnd(spans[0]);
                int spanFlag = editable.getSpanFlags(spans[0]);
                int spanValue = spans[0].getSpanConfigValue();
                Log.i(TAG, "value:" + value + ", spanValue:" + spanValue);

                if (ISpanConfig.BOLD == type || ISpanConfig.ITALIC == type || ISpanConfig.LINE == type) {   // BOLD，ITALIC，LINE，仅2个值
                    editable.removeSpan(spans[0]);
                    if (start == spanBegin && end == spanEnd) {
                        ;
                    } else if (start == spanBegin && end < spanEnd) {          // 所选范围位于span内的几种情况
                        editable.setSpan(newSpanConfig(type, value), start, spanEnd, spanFlag);
                    } else if (start > spanBegin && end < spanEnd) {
                        editable.setSpan(newSpanConfig(type, value), spanBegin, start, spanFlag);
                        editable.setSpan(newSpanConfig(type, value), end, spanEnd, spanFlag);
                    } else if (start > spanBegin && end == spanEnd) {
                        editable.setSpan(newSpanConfig(type, value), spanBegin, end, spanFlag);
                    } else if (start > spanBegin && end >= spanEnd) {           // 所选范围与span重叠的几种情况
                        editable.setSpan(newSpanConfig(type, value), spanBegin, end, spanFlag);
                    } else if (start <= spanBegin && end < spanEnd) {
                        if (isSpanWithFirstWord) {
                            if (0 == editText.getSelectionStart()) {// 选择内容到了第一个字符
                                editable.setSpan(newSpanConfig(type, value), start, spanEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            } else {
                                editable.setSpan(newSpanConfig(type, value), start, spanEnd, spanFlag);
                            }
                        } else {
                            editable.setSpan(newSpanConfig(type, value), start, spanEnd, spanFlag);
                        }
                    }
                } else if (ISpanConfig.COLOR == type || ISpanConfig.SIZE == type) {                         // COLOR, SIZE等，多个值
                    Log.i(TAG, "span:[" + spanBegin + ", " + spanEnd + "], select:[" + start + ", " + end + "]");
                    editable.removeSpan(spans[0]);
                    if (start == spanBegin && end == spanEnd) {
                        editable.setSpan(newSpanConfig(type, value), start, end, spanFlag);
                    } else if (start == spanBegin && end < spanEnd) {          // 所选范围位于span内的几种情况
                        editable.setSpan(newSpanConfig(type, value), start, end, spanFlag);
                        editable.setSpan(newSpanConfig(type, spanValue), end, spanEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    } else if (start > spanBegin && end < spanEnd) {
                        editable.setSpan(newSpanConfig(type, spanValue), spanBegin, start, spanFlag);
                        editable.setSpan(newSpanConfig(type, value), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        editable.setSpan(newSpanConfig(type, spanValue), end, spanEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    } else if (start > spanBegin && end == spanEnd) {
                        editable.setSpan(newSpanConfig(type, spanValue), spanBegin, start, spanFlag);
                        editable.setSpan(newSpanConfig(type, value), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    } else if (start > spanBegin && end >= spanEnd) {           // 所选范围与span重叠的几种情况
                        editable.setSpan(newSpanConfig(type, spanValue), spanBegin, start, spanFlag);
                        editable.setSpan(newSpanConfig(type, value), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    } else if (start <= spanBegin && end < spanEnd) {
                        if (isSpanWithFirstWord) {
                            if (0 == editText.getSelectionStart()) {// 选择内容到了第一个字符
                                editable.setSpan(newSpanConfig(type, value), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            } else {
                                editable.setSpan(newSpanConfig(type, value), start, end, spanFlag);
                            }
                        } else {
                            editable.setSpan(newSpanConfig(type, value), start, end, spanFlag);
                        }
                        editable.setSpan(newSpanConfig(type, spanValue), end, spanEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    }
                }
            } else if (1 < spans.length) {  // 所选范围内有多个span
                int len = spans.length;
                int spanFirstStart = editable.getSpanStart(spans[0]);
//                int spanFirstEnd = editable.getSpanEnd(spans[0]);
                int spanFirstValue = spans[0].getSpanConfigValue();
                int spanFirstFlag = editable.getSpanFlags(spans[0]);
//                int spanLastStart = editable.getSpanStart(spans[len - 1]);
                int spanLastEnd = editable.getSpanEnd(spans[len - 1]);
                int spanLastValue = spans[len - 1].getSpanConfigValue();

                for (ISpanConfig span : spans) {
                    editable.removeSpan(span);
                }

                if (start == spanFirstStart && end == spanLastEnd) {
                    editable.setSpan(newSpanConfig(type, value), start, end, spanFirstFlag);
                } else if (start == spanFirstStart && end < spanLastEnd) {      // 所选范围位于span内的几种情况
                    editable.setSpan(newSpanConfig(type, value), start, end, spanFirstFlag);
                    editable.setSpan(newSpanConfig(type, spanLastValue), end, spanLastEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                } else if (start > spanFirstStart && end < spanLastEnd) {
                    editable.setSpan(newSpanConfig(type, spanFirstValue), spanFirstStart, start, spanFirstFlag);
                    editable.setSpan(newSpanConfig(type, value), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    editable.setSpan(newSpanConfig(type, spanLastValue), end, spanLastEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                } else if (start > spanFirstStart && end == spanLastEnd) {
                    editable.setSpan(newSpanConfig(type, spanFirstValue), spanFirstStart, start, spanFirstFlag);
                    editable.setSpan(newSpanConfig(type, value), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                } else if (start <= spanFirstStart && end < spanLastEnd) {                              // 所选范围与span重叠的几种情况
                    if (isSpanWithFirstWord) {
                        if (0 == editText.getSelectionStart()) {// 选择内容到了第一个字符
                            editable.setSpan(newSpanConfig(type, value), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        } else {
                            editable.setSpan(newSpanConfig(type, value), start, end, spanFirstFlag);
                        }
                    } else {
                        editable.setSpan(newSpanConfig(type, value), start, end, spanFirstFlag);
                    }
                    editable.setSpan(newSpanConfig(type, spanLastValue), end, spanLastEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                } else if (start > spanFirstStart && end >= spanLastEnd) {
                    editable.setSpan(newSpanConfig(type, spanFirstValue), spanFirstStart, start, spanFirstFlag);
                    editable.setSpan(newSpanConfig(type, value), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                }
            }
            sortSpanConfig(editable, type);
        }

        printSpansArrays(editable, 0, editable.length());
    }

    // 输入文字变动后，进行修复
    public void fixError(int start, int count) {

        // 测试颜色
        int type = ISpanConfig.COLOR;
        ISpanConfig[] spansAll = mEditable.getSpans(0, mEditable.length(), ISpanConfig.class);
        ISpanConfig[] spanConfigType = getSpanConfigType(spansAll, type);
        SpannableStringBuilder text = (SpannableStringBuilder) mEditable;
        for (int i = 0; i < spanConfigType.length; i++) {
            int spanStart = text.getSpanStart(spanConfigType[i]);
            int spanEnd = text.getSpanEnd(spanConfigType[i]);
            int spanFlag = text.getSpanFlags(spanConfigType[i]);
            if (Spannable.SPAN_INCLUSIVE_EXCLUSIVE == spanFlag) {

            }
        }

//        mEditable;
    }

    public void test(EditText editText) {
        printSpansArrays(editText.getText(), 0, editText.getText().length());
    }

    public void printSpansArrays(Editable editable, int start, int end) {
        SpannableStringBuilder text = (SpannableStringBuilder) editable;
        ISpanConfig[] spans = editable.getSpans(start, end, ISpanConfig.class);
        if (0 == spans.length) {
            Log.e(TAG, "printSpansArrays, spans is empty");
        } else {
            Log.e(TAG, "printSpansArrays, spans.length:" + spans.length);
            for (int i = 0; i < spans.length; i++) {
                int s = text.getSpanStart(spans[i]);
                int e = text.getSpanEnd(spans[i]);
                int flag = text.getSpanFlags(spans[i]);
                int value = spans[i].getSpanConfigValue();
                Log.e(TAG, "printSpansArrays, i:" + i + ", [" + s + ", " + e + "], flag:" + flag + ", value:" + value);
            }
        }
    }

    public void setCursorSpan(Editable editable, int type, int start, int end, int value) {
        printSpansArrays(editable, 0, editable.length());

        ISpanConfig[] spansAll = editable.getSpans(0, editable.length(), ISpanConfig.class);
        ISpanConfig[] spanConfigType = getSpanConfigType(spansAll, type);
        SpannableStringBuilder text = (SpannableStringBuilder) editable;

        boolean isAdd = false;
        int spanStart;
        int spanEnd;
        int spanType;
        int spanValue;
        int spanFlag;
        for (int i = 0; i < spanConfigType.length; i++) {
            spanStart = text.getSpanStart(spanConfigType[i]);
            spanEnd = text.getSpanEnd(spanConfigType[i]);
            spanFlag = text.getSpanFlags(spanConfigType[i]);

            spanType = spanConfigType[i].getSpanConfigType();
            spanValue = spanConfigType[i].getSpanConfigValue();
            //text.setSpan(newSpanConfig(type, orders[i][3]), orders[i][1], orders[i][2], Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            Log.i("wtx", "--->[" + spanStart + ", " + spanEnd + "], spanValue:" + spanValue + ", value:" + value + "->[" + start + ", " + end + "]");
            if (start == spanStart) {
                Log.e(TAG, "head.");
                editable.setSpan(newSpanConfig(type, value), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                if (0 == start) {
                    editable.removeSpan(spanConfigType[i]);
                    editable.setSpan(newSpanConfig(spanType, spanValue), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                }
                isAdd = true;
                break;
            } else if (start > spanStart && start < spanEnd) {
                Log.e(TAG, "between. [" + spanStart + ", " + spanEnd + "], start:" + start + ", spanType:" + spanType + ", spanValue:" + spanValue);
                editable.removeSpan(spanConfigType[i]);
                editable.setSpan(newSpanConfig(spanType, spanValue), spanStart, start, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                editable.setSpan(newSpanConfig(spanType, value), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                editable.setSpan(newSpanConfig(spanType, spanValue), start, spanEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                isAdd = true;
                break;
            }
        }
        if (!isAdd) {
            editable.setSpan(newSpanConfig(type, value), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        sortSpanConfig(editable, type);
        // check
    }

    // 将span按照[start end]中start的顺序进行排序
    public void sortSpanConfig(Editable editable, int type) {
        ISpanConfig[] spansAll = editable.getSpans(0, editable.length(), ISpanConfig.class);
        ISpanConfig[] spanConfigType = getSpanConfigType(spansAll, type);
        SpannableStringBuilder text = (SpannableStringBuilder) editable;

        if (1 >= spanConfigType.length) {
            return ;
        }

        // 根据span的start进行从小到大排序
        int[][] orders = new int[spanConfigType.length][5];     // 0记录原索引，1记录start值,2记录stop值，3记录value值, 4记录flag值
        for (int i = 0; i < orders.length; i++) {
            orders[i][0] = i;
            orders[i][1] = text.getSpanStart(spanConfigType[i]);
            orders[i][2] = text.getSpanEnd(spanConfigType[i]);
            orders[i][3] = spanConfigType[i].getSpanConfigValue();
            orders[i][4] = text.getSpanFlags(spanConfigType[i]);
        }
        int[] temp = new int[5];
        for (int s = 0; s < spanConfigType.length - 1; s++) {
            for (int t = s + 1; t < spanConfigType.length; t++) {
                if (orders[s][1] > orders[t][1]) {
                    temp = orders[s].clone();
                    orders[s] = orders[t].clone();
                    orders[t] = temp.clone();
                }
            }
        }

        // 清理spans
        for (ISpanConfig noteTextStyleSpan : spanConfigType) {
            text.removeSpan(noteTextStyleSpan);
        }

        // 重新按照顺序进行设置，因为span的顺序与添加的顺序有关
        for (int i = 0; i < orders.length; i++) {
            if (0 <= orders[i][1] && 0 <= orders[i][2] && orders[i][1] <= orders[i][2]) {
                if (isSpanWithFirstWord) {
                    if (0 == orders[i][1] && orders[i][1] < orders[i][2]) {
                        text.setSpan(newSpanConfig(type, orders[i][3]), orders[i][1], orders[i][2], orders[i][4]);
                    } else {
                        text.setSpan(newSpanConfig(type, orders[i][3]), orders[i][1], orders[i][2], orders[i][4]);
                    }
                } else {
                    text.setSpan(newSpanConfig(type, orders[i][3]), orders[i][1], orders[i][2], orders[i][4]);    // 重新调整为SPAN_EXCLUSIVE_INCLUSIVE
                }
                Log.w(TAG, "--->" + orders[i][0] + "[" + orders[i][1] + ", " + orders[i][2] + "], value:" + orders[i][3] + ", flag:" + orders[i][4]);
            } else {
                Log.w(TAG, "span warning, index:" + orders[i][0] + "[" + orders[i][1] + ", " + orders[i][2] + "], value:" + orders[i][3] + ", flag:" + orders[i][4]);
            }
        }
    }

    public ISpanConfig[] getSpanConfigType(ISpanConfig[] spansAll, int type) {
        List<ISpanConfig> list = new ArrayList<>();
        for (int i = 0; i < spansAll.length; i++) {
            if (type == spansAll[i].getSpanConfigType()) {
                list.add(spansAll[i]);
            }
        }
        return list.toArray(new ISpanConfig[0]);
    }

    public ISpanConfig newSpanConfig(int type, int value) {
        ISpanConfig iSpanConfig = null;
        switch (type) {
            case ISpanConfig.BOLD:
                iSpanConfig = new SpanBold(value);
                iSpanConfig.setSpanConfigType(ISpanConfig.BOLD);
                iSpanConfig.setSpanConfigValue(value);
                break;
            case ISpanConfig.ITALIC:
                iSpanConfig = new SpanItalic(value);
                iSpanConfig.setSpanConfigType(ISpanConfig.ITALIC);
                iSpanConfig.setSpanConfigValue(value);
                break;
            case ISpanConfig.LINE:
                iSpanConfig = new SpanLine(value);
                iSpanConfig.setSpanConfigType(ISpanConfig.LINE);
                iSpanConfig.setSpanConfigValue(value);
                break;
            case ISpanConfig.COLOR:
                iSpanConfig = new SpanColor(value);
                iSpanConfig.setSpanConfigType(ISpanConfig.COLOR);
                iSpanConfig.setSpanConfigValue(value);
                break;
            case ISpanConfig.SIZE:
                iSpanConfig = new SpanSize(value);
                iSpanConfig.setSpanConfigType(ISpanConfig.SIZE);
                iSpanConfig.setSpanConfigValue(value);
                break;
        }
        return iSpanConfig;
    }

    //        // 前景色
    //        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLUE);
    //        // 背景色
    //        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.YELLOW);
    //        // 字体大小
    //        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(16);
    //        // 粗体/斜体
    //        StyleSpan StyleSpanItalic = new StyleSpan(Typeface.ITALIC);
    //        StyleSpan StyleSpanBold = new StyleSpan(Typeface.BOLD);
    //        // 删除线
    //        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
    //        // 下划线
    //        UnderlineSpan underlineSpan = new UnderlineSpan();

    private void testEditText(Context context, int index) {
//        //改变字体颜色
//        // 1.先构造SpannableString
        SpannableString spanString = new SpannableString("testEditText");
//        // 2.再构造一个改变字体颜色的Span
//        ForegroundColorSpan span = new ForegroundColorSpan(Color.BLUE);
//        // 3.将这个Span应用于指定范围的字体
//        spanString.setSpan(span, 1, 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        // 4.设置给EditText显示出来
//        editText.setText(spanString);

        // 前景色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLUE);
        // 背景色
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.YELLOW);
        // 字体大小
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(16);
        // 粗体/斜体
        StyleSpan StyleSpan = new StyleSpan(Typeface.BOLD_ITALIC);
        // 删除线
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        // 下划线
        UnderlineSpan underlineSpan = new UnderlineSpan();
        // 图片置换
        Drawable d = context.getResources().getDrawable(R.drawable.ic_launcher_foreground);
        d.setBounds(0, 0, d.getIntrinsicWidth() / 2, d.getIntrinsicHeight() / 2);
        ImageSpan imageSpan = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);

//        switch (index) {
//            case 0:
//                spanString.setSpan(foregroundColorSpan, 1, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//                editText.setText(spanString);
//                break;
//            case 1:
//                spanString.setSpan(backgroundColorSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                editText.setText(spanString);
//                break;
//            case 2:
//                spanString.setSpan(absoluteSizeSpan, 2, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                editText.setText(spanString);
//                break;
//            case 3:
//                spanString.setSpan(StyleSpan, 1, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                editText.setText(spanString);
//                break;
//            case 4:
//                spanString.setSpan(strikethroughSpan, 2, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                editText.setText(spanString);
//                break;
//            case 5:
//                spanString.setSpan(underlineSpan, 1, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                editText.setText(spanString);
//                break;
//            case 6:
//                spanString.setSpan(imageSpan, 2, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                editText.setText(spanString);
//                break;
//        }

    }

    // 对新输入的数据设置span
    public void addCursorTextSpan(CharSequence s, int start, int count) {
        Log.i(TAG, "start:" + start + ", count:" + count + ", s:" + s);
        // start处没有span

        // start处于span之间

        // start处之前刚好有span

    }
}
