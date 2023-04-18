package com.example.customutil.designpattern.adapter;

public class TestAdapterDemo {

        /**
         *      适配器模式种的三种对象(以蝴蝶效应为例子)：
         *      
         *      1.源(Adaptee)：手头上有的，但是不满足需求，需要进行包装(蝴蝶)
         *      
         *      2.目标(Target)：希望达成的接口(destory()，接口种的方法或者类中的方法)
         *      
         *      3.适配器(Adapter)：进行包装工作(产生蝴蝶效应)
         */
        public static void TestAdapter() {
                // TODO Auto-generated method stub
                
//                //不继承动物类，不实现destory接口(相当于整个工程中没有IDestory接口)。
                //针对Storm类，即destory()仅仅是Strom类种的一个普通方法。
                AdapterAnimalToStorm destory3 = new AdapterAnimalToStorm(new Butterfly(), new Storm());
                destory3.adapter();
                AdapterAnimalToStorm destory4 = new AdapterAnimalToStorm(new Ant(), new Storm());
                destory4.adapter();
                
                System.out.println("---------------------------------------");
                
                //继承自动物类，实现destory接口。
                //主要针对方法destory()。
                IDestroy destory1 = new AdapterAnimalToDestory(new Butterfly());
                destory1.destory();
                IDestroy destory2 = new AdapterAnimalToDestory(new Ant());
                destory2.destory();
                
        }

}
