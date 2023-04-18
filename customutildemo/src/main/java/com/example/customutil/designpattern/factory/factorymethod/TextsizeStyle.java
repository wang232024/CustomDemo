package com.example.customutil.designpattern.factory.factorymethod;

import com.example.customutil.designpattern.factory.factoryinterface.IDisplay;
import com.example.customutil.designpattern.factory.simplefactory.Textsize;

public class TextsizeStyle extends FactoryMethod {

        @Override
        public IDisplay setStyle() {
                // TODO Auto-generated method stub
                return new Textsize();
        }

}
