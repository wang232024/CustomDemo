package com.example.customutil.designpattern.template;

/**
 *      1.封装不变部分，扩展可变部分。
 *      2.不变部分由父类控制，可变部分由子类实现。
 *      3.为防止父类控制的不变部分被子类改变，一般可加上final关键字控制。
 * @author wtx
 *
 */
public abstract class Template {
        
        /**
         *      抽象方法，画图方式由各子类自己完成。
         */
        public abstract void onDraw();
        
        /**
         *      模板方法，各子类通用，由父类提取出来，可用关键字final修饰。
         * @return      返回画图所消耗的时间。
         */
        public final long getResult() {
                long i = System.currentTimeMillis();
                System.out.println("i:" + i);
                onDraw();
                long j = System.currentTimeMillis();
                System.out.println("j:" + j);
                return j - i;
        }
        
}
