package com.example.customutil.designpattern.factory.abstractfactory;

import com.example.customutil.designpattern.factory.factoryinterface.IDisplayCold;
import com.example.customutil.designpattern.factory.factoryinterface.IDisplayHot;

public class TextcolorStyleFactory extends AbstractFactory {

        @Override
        public IDisplayCold setStyleCold() {
                // TODO Auto-generated method stub
                return new TextcolorStyleCold();
        }

        @Override
        public IDisplayHot setStyleHot() {
                // TODO Auto-generated method stub
                return new TextcolorStyleHot();
        }

}
