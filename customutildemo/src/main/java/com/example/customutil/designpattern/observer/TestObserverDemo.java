package com.example.customutil.designpattern.observer;

import com.example.customutil.designpattern.observer.buttonlistener.Button;
import com.example.customutil.designpattern.observer.buttonlistener.Button.OnClickListener;

/**
 * @author wtx
 */
public class TestObserverDemo implements Button.OnClickListener {
        
        public void TestObserver() {
                // TODO Auto-generated method stub
                
                //      场景：多个赌徒gambler观察赌场casino的结果。
                //      gambleA win
                //      gambleB lost
                Casino casino = new Casino();
                
                GamblerA a = new GamblerA();
                GamblerB b = new GamblerB();
                casino.casinolist.add(a);
                casino.casinolist.add(b);
                
                casino.setString("5");
                
                System.out.println("----------------------------------------");
                
                //按键类实现点击和长按的监听接口，一个TestObserverDemo观察多个按键的结果。
                Button btn = new Button();
                btn.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onLongClick() {
                                // TODO Auto-generated method stub
                                System.out.println("onLongClick");
                        }
                        
                        @Override
                        public void onCLick() {
                                // TODO Auto-generated method stub
                                System.out.println("onCLick");
                        }
                });
                btn.clickonce(1);
                btn.clickonce(2);
                
                fun();
                
        }

        private void fun() {
                // TODO Auto-generated method stub
                Button btn1 = new Button();
                // 此时需要TestObserverDemo实现Button类中的OnClickListener，并且不能在静态方法种被调用。
                btn1.setOnClickListener(this);
                btn1.clickonce(1);
                btn1.clickonce(2);
        }

        @Override
        public void onCLick() {
                // TODO Auto-generated method stub
                System.out.println("aaaaaaaaaa onClick");
        }

        @Override
        public void onLongClick() {
                // TODO Auto-generated method stub
                System.out.println("bbbbbbbbb onLongClick");
        }
        
}
