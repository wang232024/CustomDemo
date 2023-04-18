package com.example.customutil.designpattern.singleton;

public class TestSingletonDemo {
        public static void TestSingle() {
                
                Thread thread1 = new Thread(new Runnable() {
                        
                        @Override
                        public void run() {
                                // TODO Auto-generated method stub
                                for (int i = 0; i < 2; i++) {
                                        SingletonLazy single1 = SingletonLazy.getInstance();
                                        SingletonHurry single2 = SingletonHurry.getInstance();
                                        SingletonRegister single3 = SingletonRegister.getInstance();
                                        SingletonDoubleCheck single4 = SingletonDoubleCheck.getInstance();
                                        System.out.println("SingletonLazy: " + single1 + "************");
                                        System.out.println("SingletonHurry: " + single2 + "##########");
                                        System.out.println("SingletonRegister: " + single3 + "^^^^^^^^^^^^");
                                        System.out.println("SingletonDoubleCheck: " + single4 + "$$$$$$$$$$$$$$");
                                }
                        }
                });
                
                Thread thread2 = new Thread(new Runnable() {
                        
                        @Override
                        public void run() {
                                // TODO Auto-generated method stub
                                for (int i = 0; i < 2; i++) {
                                        SingletonLazy single1 = SingletonLazy.getInstance();
                                        SingletonHurry single2 = SingletonHurry.getInstance();
                                        SingletonRegister single3 = SingletonRegister.getInstance();
                                        SingletonDoubleCheck single4 = SingletonDoubleCheck.getInstance();
                                        System.out.println("SingletonLazy: " + single1 + "************");
                                        System.out.println("SingletonHurry: " + single2 + "##########");
                                        System.out.println("SingletonRegister: " + single3 + "^^^^^^^^^^^^");
                                        System.out.println("SingletonDoubleCheck: " + single4 + "$$$$$$$$$$$$$$");
                                }
                        }
                });

                thread1.start();
                thread2.start();
        }
}
