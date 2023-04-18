package com.example.customutil.designpattern.factory;

import com.example.customutil.designpattern.factory.abstractfactory.TextcolorStyleFactory;
import com.example.customutil.designpattern.factory.factoryinterface.IDisplay;
import com.example.customutil.designpattern.factory.factoryinterface.IDisplayCold;
import com.example.customutil.designpattern.factory.factoryinterface.IDisplayHot;
import com.example.customutil.designpattern.factory.factorymethod.BackgroundStyle;
import com.example.customutil.designpattern.factory.factorymethod.FontStyle;
import com.example.customutil.designpattern.factory.factorymethod.TextcolorStyle;
import com.example.customutil.designpattern.factory.factorymethod.TextsizeStyle;
import com.example.customutil.designpattern.factory.simplefactory.SimpleStyleFactory;

public class TestFactoryDemo {

        public static void TestFactory() {
                // TODO Auto-generated method stub
                TestSimpleFactory();
//                TestFactoryMethod();
//                TestAbstractFactory();
        }

        private static void TestAbstractFactory() {
                // TODO Auto-generated method stub
                TextcolorStyleFactory textcolorStyle = new TextcolorStyleFactory();
                IDisplayCold textcolorStyleCold = textcolorStyle.setStyleCold();
                IDisplayHot textcolorStyleHot = textcolorStyle.setStyleHot();
                textcolorStyleCold.display();
                textcolorStyleHot.display();
        }

        private static void TestFactoryMethod() {
                // TODO Auto-generated method stub
                IDisplay textcolor = new TextcolorStyle().setStyle();
                textcolor.display();
                IDisplay textsize = new TextsizeStyle().setStyle();
                textsize.display();
                IDisplay font = new FontStyle().setStyle();
                font.display();
                IDisplay background = new BackgroundStyle().setStyle();
                background.display();
        }

        private static void TestSimpleFactory() {
                // TODO Auto-generated method stub
                IDisplay textcolor = SimpleStyleFactory.setStyle("textcolor");
                textcolor.display();
                IDisplay textsize = SimpleStyleFactory.setStyle("textsize");
                textsize.display();
                IDisplay background = SimpleStyleFactory.setStyle("background");
                background.display();
        }

}
