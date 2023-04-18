package com.example.customutil.designpattern.factory.abstractfactory;

import com.example.customutil.designpattern.factory.factoryinterface.IDisplayCold;
import com.example.customutil.designpattern.factory.factoryinterface.IDisplayHot;

public class TextsizeStyleFactory extends AbstractFactory {

        @Override
        protected IDisplayCold setStyleCold() {
                // TODO Auto-generated method stub
                return new TextsizeStyleCold();
        }

        @Override
        protected IDisplayHot setStyleHot() {
                // TODO Auto-generated method stub
                return new TextsizeStyleHot();
        }

}
