package com.example.custom.span;

import android.os.Parcel;
import android.text.style.AbsoluteSizeSpan;

import androidx.annotation.NonNull;

public class SpanSize extends AbsoluteSizeSpan implements ISpanConfig {
    private int mSpanConfigType;
    private int mSpanConfigValue;

    public SpanSize(int size) {
        super(size);
    }

    public SpanSize(int size, boolean dip) {
        super(size, dip);
    }

    public SpanSize(@NonNull Parcel src) {
        super(src);
    }

    public SpanSize(int begin, int end, int range, int value) {
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
