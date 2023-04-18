package com.example.customutil.designpattern.factory.abstractfactory;

import com.example.customutil.designpattern.factory.factoryinterface.IDisplayCold;
import com.example.customutil.designpattern.factory.factoryinterface.IDisplayHot;

public class BackgroundStyleFactory extends AbstractFactory {

        @Override
        protected IDisplayCold setStyleCold() {
                // TODO Auto-generated method stub
                return new BackgroundStyleCold();
        }

        @Override
        protected IDisplayHot setStyleHot() {
                // TODO Auto-generated method stub
                return new BackgroundStyleHot();
        }

}
