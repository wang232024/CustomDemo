package com.example.customutil.designpattern.factory.factorymethod;

import com.example.customutil.designpattern.factory.factoryinterface.IDisplay;
import com.example.customutil.designpattern.factory.simplefactory.Textcolor;

public class TextcolorStyle extends FactoryMethod {

        //访问权限必须为public才能被其他包种的TestFactoryDemo类访问到。
        @Override
        public IDisplay setStyle() {
                // TODO Auto-generated method stub
                return new Textcolor();
        }

}
