package com.example.custom.span;

import android.graphics.Typeface;
import android.os.Parcel;
import android.text.style.StyleSpan;

import androidx.annotation.NonNull;

public class SpanItalic extends StyleSpan implements ISpanConfig {
    private int mSpanConfigType;
    private int mSpanConfigValue;
//    public SpanItalic(int style) {
//        super(style);
//    }

    public SpanItalic(@NonNull Parcel src) {
        super(src);
    }

    public SpanItalic(int value) {
        super(Typeface.ITALIC);
    }

    @Override
    public int getSpanConfigType() {
        return mSpanConfigType;
    }

    @Override
    public void setSpanConfigType(int type) {
        mSpanConfigType = type;
    }

    @Override
    public int getSpanConfigValue() {
        return mSpanConfigValue;
    }

    @Override
    public void setSpanConfigValue(int value) {
        mSpanConfigValue = value;
    }

}
