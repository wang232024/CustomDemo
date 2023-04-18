package com.example.custom.span;

import android.text.style.UnderlineSpan;

public class SpanLine extends UnderlineSpan implements ISpanConfig {
    private int mSpanConfigType;
    private int mSpanConfigValue;
    public SpanLine(int value) {
        super();
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
