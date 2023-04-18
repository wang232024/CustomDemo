package com.example.custom.span;

import android.graphics.Typeface;
import android.os.Parcel;
import android.text.style.StyleSpan;

import androidx.annotation.NonNull;

public class SpanBold extends StyleSpan implements ISpanConfig {
    private int mSpanConfigType;
    private int mSpanConfigValue;
//    public SpanBold(int style) {
//        super(style);
//    }

    public SpanBold(@NonNull Parcel src) {
        super(src);
    }

    public SpanBold(int value) {
        super(Typeface.BOLD);
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
