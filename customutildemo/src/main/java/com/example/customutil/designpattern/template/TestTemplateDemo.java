package com.example.customutil.designpattern.template;

public class TestTemplateDemo {

        public static void TestTemplate() {
                // TODO Auto-generated method stub
                TestTemplateDemo test = new TestTemplateDemo();
                
                DrawCircle c = test.new DrawCircle();
                System.out.println(c.getResult());
                System.out.println("-------------------");
                
                DrawRectangle r = test.new DrawRectangle();
                System.out.println(r.getResult());
        }
        
        class DrawCircle extends Template {

                @Override
                public void onDraw() {
                        // TODO Auto-generated method stub
                        System.out.println("Draw Circle");
                        try {
                                Thread.sleep(1000);
                        } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                }
                
        }
        
        class DrawRectangle extends Template {

                @Override
                public void onDraw() {
                        // TODO Auto-generated method stub
                        System.out.println("Draw Rectangle");
                        try {
                                Thread.sleep(3000);
                        } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                }
                
        }

}
