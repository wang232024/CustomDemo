package com.example.customutil.designpattern.observer;

public class Gambler implements IUpdate {
        
        // 子类都有而且相同，完全不需要改变的方法有父类实现，子类直接调用。
        // 子类都有而且不同的方法设置成接口，由子类各自实现。
        public static void result(String string, String gString) {
                // TODO Auto-generated method stub
                if (Integer.valueOf(string) <= Integer.valueOf(gString)) {
                        System.out.println("win");
                } else {
                        System.out.println("lost");
                }
        }

        @Override
        public void update(String string) {
                // TODO Auto-generated method stub
                
        }
}
