package com.example.custom.synchronizeddealwith;

/**
 * Created by Administrator on 2018/3/15.
 */

public class DataWrap implements Comparable<Object> {
    private String mData;

    public DataWrap(String data) {
        mData = data;
    }

    public String getData() {
        return mData;
    }

    public void setData(String mData) {
        this.mData = mData;
    }

    @Override
    public int compareTo(Object other) {
        String otherData = ((DataWrap) other).getData();
        return mData.compareTo(otherData);
    }
}
