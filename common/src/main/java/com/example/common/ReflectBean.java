package com.example.common;

public class ReflectBean {
    private String name;
    private static int num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int getNum() {
        return num;
    }

    public static void setNum(int num) {
        ReflectBean.num = num;
    }

    public static class ReflectInner extends ReflectFather {
        public String innerPublicField;
    }
}
