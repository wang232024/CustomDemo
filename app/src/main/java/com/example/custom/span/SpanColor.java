package com.example.custom.span;

import android.os.Parcel;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.NonNull;

public class SpanColor extends ForegroundColorSpan implements ISpanConfig {
    private int mSpanConfigType;
    private int mSpanConfigValue;
    public SpanColor(int color) {
        super(color);
        mSpanConfigValue = color;
    }

    public SpanColor(@NonNull Parcel src) {
        super(src);
    }

    public SpanColor(int begin, int end, int range, int value) {
        super(value);

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
