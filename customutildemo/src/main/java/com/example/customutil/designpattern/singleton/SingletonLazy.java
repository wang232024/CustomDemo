package com.example.customutil.designpattern.singleton;

/*
 *      懒汉式，线程安全，效率低。
 */
public class SingletonLazy {
        private static SingletonLazy single;
        private SingletonLazy() {}

        public static synchronized SingletonLazy getInstance() {
                if  (null == single) 
                        single = new SingletonLazy();

                return single;
        }
}
