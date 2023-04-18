package com.example.customutil.designpattern.factory.factorymethod;

import com.example.customutil.designpattern.factory.factoryinterface.IDisplay;
import com.example.customutil.designpattern.factory.simplefactory.Background;

public class BackgroundStyle extends FactoryMethod {

        @Override
        public IDisplay setStyle() {
                // TODO Auto-generated method stub
                return new Background();
        }

}
