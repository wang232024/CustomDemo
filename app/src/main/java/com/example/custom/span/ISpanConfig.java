package com.example.custom.span;

public interface ISpanConfig {
    public static final int SIZE = 0;
    public static final int BOLD = 1;
    public static final int ITALIC = 2;
    public static final int LINE = 3;
    public static final int COLOR = 4;
    public static final int MAX = 5;

    int getSpanConfigType();
    void setSpanConfigType(int type);

    int getSpanConfigValue();
    void setSpanConfigValue(int value);
}
