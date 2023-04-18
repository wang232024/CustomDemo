package com.example.customutil.designpattern.singleton;

/*
 *      登记式，线程安全，改良自饿汉式，避免类装载就被实例化，达到懒汉式的效果
 *      实现简单，推荐使用
 */
public class SingletonRegister {
        private static class SingleHolder {
                private static final SingletonRegister single = new SingletonRegister();
        }
        
        private SingletonRegister() {}
        
        public static SingletonRegister getInstance() {
                
                return SingleHolder.single;
        }
}
