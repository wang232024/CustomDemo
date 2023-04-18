package com.example.customutil.designpattern.factory.factorymethod;

import com.example.customutil.designpattern.factory.factoryinterface.IDisplay;

/**
 *      此时工厂方法不再负责设置所有属性，而是抽象出设置属性的方法setStyle()
 *      让其他工厂类实现，方便进行扩展。
 * @author wtx
 *
 */
public abstract class FactoryMethod {
        protected abstract  IDisplay setStyle();
}
