package com.example.customutil.designpattern.singleton;

/*
 *      饿汉式，线程安全，静态初始化，一般情况下使用这种。
 */
public class SingletonHurry {
        private static SingletonHurry single = new SingletonHurry();
        
        private SingletonHurry () {}
        
        public  static SingletonHurry getInstance () {
                
                return single;
        }
}
