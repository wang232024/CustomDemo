package com.example.customutil.designpattern.singleton;

/*
 *      双重校验锁
 */
public class SingletonDoubleCheck {
        
        private static volatile SingletonDoubleCheck single;
        
        private SingletonDoubleCheck() {}
        
        public static SingletonDoubleCheck getInstance() {
                //第一次判断是了让以后获取单例对象时不用在synchronized，这很低效率。
                if (null ==single) {
                        synchronized (SingletonDoubleCheck.class) {
                                //在锁住SingletonDoubleCheck时，该对象可能已被其他线程完成初始化，故需要再次判断
                                //即若无synchronized，判断完null == single后线程可能被抢CPU，导致single被其他线程初始化。
                                if (null == single) {
                                        single = new SingletonDoubleCheck();
                                }
                        }
                }
                
                return single;
        }
}
